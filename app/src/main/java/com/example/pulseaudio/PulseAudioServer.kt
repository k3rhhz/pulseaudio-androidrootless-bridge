package com.example.pulseaudio

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Log
import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

class PulseAudioServer(private val port: Int = 4713) {

    private var isRunning = false
    private var serverSocket: ServerSocket? = null
    private var audioTrack: AudioTrack? = null
    
    // Format typically used by PulseAudio default simple protocol
    private val sampleRate = 44100
    private val channelConfig = AudioFormat.CHANNEL_OUT_STEREO
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val bufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat)

    fun start() {
        if (isRunning) return
        isRunning = true

        thread(start = true) {
            try {
                serverSocket = ServerSocket(port)
                Log.i("PulseAudioServer", "Server started on port $port")

                while (isRunning) {
                    try {
                        val clientSocket = serverSocket?.accept()
                        Log.i("PulseAudioServer", "Client connected: ${clientSocket?.inetAddress?.hostAddress}")
                        handleClient(clientSocket)
                    } catch (e: Exception) {
                        if (isRunning) {
                            Log.e("PulseAudioServer", "Error accepting client: ${e.message}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("PulseAudioServer", "Error starting server: ${e.message}")
            } finally {
                stop()
            }
        }
    }

    private fun handleClient(clientSocket: Socket?) {
        clientSocket ?: return
        
        // Initialize AudioTrack for streaming PCM data
        audioTrack = AudioTrack(
            AudioManager.STREAM_MUSIC,
            sampleRate,
            channelConfig,
            audioFormat,
            bufferSize,
            AudioTrack.MODE_STREAM
        )

        audioTrack?.play()

        thread(start = true) {
            try {
                val inputStream: InputStream = clientSocket.getInputStream()
                val buffer = ByteArray(bufferSize)
                var bytesRead: Int

                // Read incoming TCP bytes and feed them directly to the AudioTrack
                while (isRunning && inputStream.read(buffer).also { bytesRead = it } != -1) {
                    audioTrack?.write(buffer, 0, bytesRead)
                }
            } catch (e: Exception) {
                Log.e("PulseAudioServer", "Error reading audio stream: ${e.message}")
            } finally {
                Log.i("PulseAudioServer", "Client disconnected")
                clientSocket.close()
                audioTrack?.stop()
                audioTrack?.release()
                audioTrack = null
            }
        }
    }

    fun stop() {
        isRunning = false
        try {
            serverSocket?.close()
        } catch (e: Exception) {
            Log.e("PulseAudioServer", "Error closing server socket: ${e.message}")
        }
        audioTrack?.stop()
        audioTrack?.release()
        audioTrack = null
        Log.i("PulseAudioServer", "Server stopped")
    }
}
