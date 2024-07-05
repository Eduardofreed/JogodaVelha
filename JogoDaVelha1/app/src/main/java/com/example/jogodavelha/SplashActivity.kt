package com.example.jogodavelha

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.jogodavelha.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    // Variável para vincular o layout
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Infla o layout usando View Binding
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura os listeners para os botões de seleção de dificuldade
        binding.buttonEasy.setOnClickListener { startGame("easy") }
        binding.buttonNormal.setOnClickListener { startGame("normal") }
        binding.buttonHard.setOnClickListener { startGame("hard") }
    }

    // Inicia a MainActivity com a dificuldade selecionada
    private fun startGame(difficulty: String) {
        val intent = Intent(this, MainActivity::class.java)
        // Passa a dificuldade selecionada para a MainActivity
        intent.putExtra("DIFFICULTY", difficulty)
        startActivity(intent)
        // Finaliza a SplashActivity para que não possa ser retornada pressionando o botão Voltar
        finish()
    }
}
