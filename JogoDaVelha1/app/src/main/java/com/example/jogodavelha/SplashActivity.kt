package com.example.jogodavelha

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jogodavelha.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonEasy.setOnClickListener { startGame("easy") }
        binding.buttonNormal.setOnClickListener { startGame("normal") }
        binding.buttonHard.setOnClickListener { startGame("hard") }
    }

    private fun startGame(difficulty: String) {
        Log.d("SplashActivity", "Selected difficulty: $difficulty")
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("DIFFICULTY", difficulty)
        startActivity(intent)
        finish()
    }
}
