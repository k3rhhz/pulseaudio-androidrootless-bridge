package com.example.pulseaudio

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var tvStatus: TextView
    private var pulseAudioServer: PulseAudioServer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)
        tvStatus = findViewById(R.id.tvStatus)

        // Initialize server on port 4713
        pulseAudioServer = PulseAudioServer(4713)

        btnStart.setOnClickListener {
            pulseAudioServer?.start()
            tvStatus.text = "Estado: Servidor Corriendo (Puerto 4713)"
            btnStart.isEnabled = false
            btnStop.isEnabled = true
        }

        btnStop.setOnClickListener {
            pulseAudioServer?.stop()
            tvStatus.text = "Estado: Servidor Detenido"
            btnStart.isEnabled = true
            btnStop.isEnabled = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure server stops when app closes
        pulseAudioServer?.stop()
    }
}
