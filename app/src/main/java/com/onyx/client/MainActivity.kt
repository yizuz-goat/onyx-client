package com.onyx.client

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val launch = findViewById<TextView>(R.id.launchButton)
        val modules = findViewById<TextView>(R.id.modulesNav)
        val huds = findViewById<TextView>(R.id.hudsNav)
        val visuals = findViewById<TextView>(R.id.visualsNav)
        val settings = findViewById<TextView>(R.id.settingsNav)
        val account = findViewById<TextView>(R.id.accountNav)

        launch.setOnClickListener {
            Toast.makeText(this, "Launching GOONER CLIENT", Toast.LENGTH_SHORT).show()
        }

        listOf(modules, huds, visuals, settings, account).forEach { item ->
            item.setOnClickListener {
                Toast.makeText(this, "${item.text} screen ready for next pass", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
