package com.example.pulseaudio

import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private var pulseAudioServer: PulseAudioServer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        pulseAudioServer = PulseAudioServer(4713)

        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(40, 40, 40, 40)

        val title = TextView(this)
        title.text = "PulseAudio TCP Receiver"
        title.textSize = 24f

        val status = TextView(this)
        status.text = "Estado: detenido"

        val startButton = Button(this)
        startButton.text = "Iniciar servidor"

        val stopButton = Button(this)
        stopButton.text = "Detener servidor"
        stopButton.isEnabled = false

        startButton.setOnClickListener {
            pulseAudioServer?.start()
            status.text = "Servidor activo en puerto 4713"
            startButton.isEnabled = false
            stopButton.isEnabled = true
        }

        stopButton.setOnClickListener {
            pulseAudioServer?.stop()
            status.text = "Servidor detenido"
            startButton.isEnabled = true
            stopButton.isEnabled = false
        }

        layout.addView(title)
        layout.addView(status)
        layout.addView(startButton)
        layout.addView(stopButton)

        setContentView(layout)
    }

    override fun onDestroy() {
        super.onDestroy()
        pulseAudioServer?.stop()
    }
}
