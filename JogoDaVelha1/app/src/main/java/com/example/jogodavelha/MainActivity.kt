package com.example.jogodavelha

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.jogodavelha.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val tabuleiro = arrayOf(
        arrayOf("", "", ""),
        arrayOf("", "", ""),
        arrayOf("", "", "")
    )
    private var jogadorAtual = "X"
    private var dificuldade: String = "easy"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recebe a dificuldade do Intent
        dificuldade = intent.getStringExtra("DIFFICULTY") ?: "easy"
        Log.d("MainActivity", "Received difficulty: $dificuldade")

        setupButtons()
        setupReturnButton()
    }

    fun buttonClick(view: View) {
        val buttonSelecionado = view as Button

        when (buttonSelecionado.id) {
            binding.buttonZero.id -> tabuleiro[0][0] = jogadorAtual
            binding.buttonUm.id -> tabuleiro[0][1] = jogadorAtual
            binding.buttonDois.id -> tabuleiro[0][2] = jogadorAtual
            binding.buttonTres.id -> tabuleiro[1][0] = jogadorAtual
            binding.buttonQuatro.id -> tabuleiro[1][1] = jogadorAtual
            binding.buttonCinco.id -> tabuleiro[1][2] = jogadorAtual
            binding.buttonSeis.id -> tabuleiro[2][0] = jogadorAtual
            binding.buttonSete.id -> tabuleiro[2][1] = jogadorAtual
            binding.buttonOito.id -> tabuleiro[2][2] = jogadorAtual
        }

        if (jogadorAtual == "X") {
            buttonSelecionado.setBackgroundResource(R.drawable.rocket) // Imagem de foguete para jogador humano
        } else {
            buttonSelecionado.setBackgroundResource(R.drawable.meteor) // Imagem de meteoro para jogador computador
        }

        buttonSelecionado.isEnabled = false

        val vencedor = verificaVencedor(tabuleiro)
        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            reiniciarJogo()
            return
        }

        jogadorAtual = "O"

        lifecycleScope.launch {
            delay(1000)
            jogadaComputador()

            val novoVencedor = verificaVencedor(tabuleiro)
            if (!novoVencedor.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, "Vencedor: $novoVencedor", Toast.LENGTH_LONG).show()
                reiniciarJogo()
            } else {
                jogadorAtual = "X"
            }
        }
    }

    private fun jogadaComputador() {
        Log.d("MainActivity", "Computer playing with difficulty: $dificuldade")
        when (dificuldade) {
            "easy" -> jogadaFacil()
            "normal" -> jogadaNormal()
            "hard" -> jogadaDificil()
        }
    }

    private fun jogadaFacil() {
        var rX: Int
        var rY: Int

        do {
            rX = Random.nextInt(0, 3)
            rY = Random.nextInt(0, 3)
        } while (tabuleiro[rX][rY].isNotEmpty())

        tabuleiro[rX][rY] = jogadorAtual
        val buttonComputador = getButtonByPosition(rX, rY)
        buttonComputador.setBackgroundResource(R.drawable.meteor) // Imagem de meteoro para jogador computador
        buttonComputador.isEnabled = false
    }

    private fun jogadaNormal() {
        // Lógica intermediária: bloquear o jogador humano
        if (!tentarVencerOuBloquear("X")) {
            jogadaFacil() // Se não houver necessidade de bloqueio, faz uma jogada fácil
        }
    }

    private fun jogadaDificil() {
        // Lógica avançada: tentar vencer ou bloquear
        if (!tentarVencerOuBloquear("O")) {
            if (!tentarVencerOuBloquear("X")) {
                jogadaFacil() // Se não houver necessidade de bloquear ou vencer, faz uma jogada fácil
            }
        }
    }

    // Função para tentar vencer ou bloquear
    private fun tentarVencerOuBloquear(simbolo: String): Boolean {
        // Verifica linhas e colunas para possível jogada
        for (i in 0 until 3) {
            // Verifica linhas
            if (tabuleiro[i][0] == simbolo && tabuleiro[i][1] == simbolo && tabuleiro[i][2].isEmpty()) {
                fazerJogada(i, 2)
                return true
            }
            if (tabuleiro[i][0] == simbolo && tabuleiro[i][2] == simbolo && tabuleiro[i][1].isEmpty()) {
                fazerJogada(i, 1)
                return true
            }
            if (tabuleiro[i][1] == simbolo && tabuleiro[i][2] == simbolo && tabuleiro[i][0].isEmpty()) {
                fazerJogada(i, 0)
                return true
            }
            // Verifica colunas
            if (tabuleiro[0][i] == simbolo && tabuleiro[1][i] == simbolo && tabuleiro[2][i].isEmpty()) {
                fazerJogada(2, i)
                return true
            }
            if (tabuleiro[0][i] == simbolo && tabuleiro[2][i] == simbolo && tabuleiro[1][i].isEmpty()) {
                fazerJogada(1, i)
                return true
            }
            if (tabuleiro[1][i] == simbolo && tabuleiro[2][i] == simbolo && tabuleiro[0][i].isEmpty()) {
                fazerJogada(0, i)
                return true
            }
        }
        // Verifica diagonais
        if (tabuleiro[0][0] == simbolo && tabuleiro[1][1] == simbolo && tabuleiro[2][2].isEmpty()) {
            fazerJogada(2, 2)
            return true
        }
        if (tabuleiro[0][0] == simbolo && tabuleiro[2][2] == simbolo && tabuleiro[1][1].isEmpty()) {
            fazerJogada(1, 1)
            return true
        }
        if (tabuleiro[1][1] == simbolo && tabuleiro[2][2] == simbolo && tabuleiro[0][0].isEmpty()) {
            fazerJogada(0, 0)
            return true
        }
        if (tabuleiro[0][2] == simbolo && tabuleiro[1][1] == simbolo && tabuleiro[2][0].isEmpty()) {
            fazerJogada(2, 0)
            return true
        }
        if (tabuleiro[0][2] == simbolo && tabuleiro[2][0] == simbolo && tabuleiro[1][1].isEmpty()) {
            fazerJogada(1, 1)
            return true
        }
        if (tabuleiro[1][1] == simbolo && tabuleiro[2][0] == simbolo && tabuleiro[0][2].isEmpty()) {
            fazerJogada(0, 2)
            return true
        }
        return false
    }

    private fun fazerJogada(x: Int, y: Int) {
        tabuleiro[x][y] = jogadorAtual
        val buttonComputador = getButtonByPosition(x, y)
        buttonComputador.setBackgroundResource(if (jogadorAtual == "X") R.drawable.rocket else R.drawable.meteor)
        buttonComputador.isEnabled = false
    }

    private fun getButtonByPosition(x: Int, y: Int): Button {
        return when (x * 3 + y) {
            0 -> binding.buttonZero
            1 -> binding.buttonUm
            2 -> binding.buttonDois
            3 -> binding.buttonTres
            4 -> binding.buttonQuatro
            5 -> binding.buttonCinco
            6 -> binding.buttonSeis
            7 -> binding.buttonSete
            8 -> binding.buttonOito
            else -> throw IllegalArgumentException("Posição inválida")
        }
    }

    private fun verificaVencedor(tabuleiro: Array<Array<String>>): String? {
        for (i in 0 until 3) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0].isNotEmpty()) {
                return tabuleiro[i][0]
            }
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i].isNotEmpty()) {
                return tabuleiro[0][i]
            }
        }
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0].isNotEmpty()) {
            return tabuleiro[0][0]
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2].isNotEmpty()) {
            return tabuleiro[0][2]
        }
        if (tabuleiro.all { linha -> linha.all { it.isNotEmpty() } }) {
            return "Empate"
        }
        return null
    }

    private fun reiniciarJogo() {
        tabuleiro.forEachIndexed { linhaIndex, linha ->
            linha.forEachIndexed { colunaIndex, _ ->
                val button = getButtonByPosition(linhaIndex, colunaIndex)
                if (tabuleiro[linhaIndex][colunaIndex].isEmpty()) {
                    button.setBackgroundResource(android.R.color.transparent)
                } else {
                    if (tabuleiro[linhaIndex][colunaIndex] == "X") {
                        button.setBackgroundResource(R.drawable.rocket)
                    } else {
                        button.setBackgroundResource(R.drawable.meteor)
                    }
                }
                button.isEnabled = true
            }
        }
        jogadorAtual = "X"
    }

    private fun setupButtons() {
        binding.buttonZero.setOnClickListener { buttonClick(it) }
        binding.buttonUm.setOnClickListener { buttonClick(it) }
        binding.buttonDois.setOnClickListener { buttonClick(it) }
        binding.buttonTres.setOnClickListener { buttonClick(it) }
        binding.buttonQuatro.setOnClickListener { buttonClick(it) }
        binding.buttonCinco.setOnClickListener { buttonClick(it) }
        binding.buttonSeis.setOnClickListener { buttonClick(it) }
        binding.buttonSete.setOnClickListener { buttonClick(it) }
        binding.buttonOito.setOnClickListener { buttonClick(it) }
    }

    private fun setupReturnButton() {
        binding.buttonReturn?.setOnClickListener {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
