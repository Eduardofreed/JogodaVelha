package com.example.jogodavelha

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.jogodavelha.databinding.ActivityMainBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // Vetor bidimensional que representará o tabuleiro de jogo
    private val tabuleiro = arrayOf(
        arrayOf("", "", ""),
        arrayOf("", "", ""),
        arrayOf("", "", "")
    )

    // Qual o Jogador está jogando
    private var jogadorAtual = "X"
    private var dificuldade: String = "easy" // Dificuldade padrão

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge() // Habilitar bordas para uma melhor experiência de usuário

        // Recebe a dificuldade do Intent
        dificuldade = intent.getStringExtra("DIFFICULTY") ?: "easy"

        // Configura os botões
        setupButtons()
    }

    // Função associada com todos os botões @param view é o botão clicado
    fun buttonClick(view: View) {
        val buttonSelecionado = view as Button

        // Atualiza o tabuleiro e o estado do botão baseado no ID do botão clicado
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

        // Atualiza a aparência do botão clicado
        buttonSelecionado.setBackgroundColor(Color.BLUE)
        buttonSelecionado.isEnabled = false

        // Verifica se há um vencedor
        val vencedor = verificaVencedor(tabuleiro)

        if (!vencedor.isNullOrBlank()) {
            Toast.makeText(this, "Vencedor: $vencedor", Toast.LENGTH_LONG).show()
            reiniciarJogo()
            return
        }

        // Troca para o jogador "O"
        jogadorAtual = "O"

        // Adiciona um delay antes da jogada do computador
        lifecycleScope.launch {
            delay(1000) // Delay de 1 segundo
            jogadaComputador()

            // Verifica novamente se há um vencedor
            val novoVencedor = verificaVencedor(tabuleiro)
            if (!novoVencedor.isNullOrBlank()) {
                Toast.makeText(this@MainActivity, "Vencedor: $novoVencedor", Toast.LENGTH_LONG).show()
                reiniciarJogo()
            } else {
                jogadorAtual = "X" // Retorna para o jogador "X"
            }
        }
    }

    // Função que realiza a jogada do computador baseada na dificuldade
    private fun jogadaComputador() {
        when (dificuldade) {
            "easy" -> jogadaFacil()
            "normal" -> jogadaNormal()
            "hard" -> jogadaDificil()
        }
    }

    // Jogada fácil: Escolhe uma posição aleatória
    private fun jogadaFacil() {
        var rX: Int
        var rY: Int

        do {
            rX = Random.nextInt(0, 3)
            rY = Random.nextInt(0, 3)
        } while (tabuleiro[rX][rY].isNotEmpty())

        tabuleiro[rX][rY] = jogadorAtual
        val buttonComputador = getButtonByPosition(rX, rY)
        buttonComputador.setBackgroundColor(Color.RED)
        buttonComputador.isEnabled = false
    }

    // Jogada normal: Pode ser melhorada com lógica intermediária
    private fun jogadaNormal() {
        jogadaFacil() // Temporariamente usando jogadaFacil
    }

    // Jogada difícil: Pode ser melhorada com lógica avançada
    private fun jogadaDificil() {
        jogadaFacil() // Temporariamente usando jogadaFacil
    }

    // Mapeia a posição da matriz para o botão correspondente
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

    // Verifica se há um vencedor
    private fun verificaVencedor(tabuleiro: Array<Array<String>>): String? {
        // Verifica linhas e colunas
        for (i in 0 until 3) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0].isNotEmpty()) {
                return tabuleiro[i][0]
            }
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i].isNotEmpty()) {
                return tabuleiro[0][i]
            }
        }
        // Verifica diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0].isNotEmpty()) {
            return tabuleiro[0][0]
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2].isNotEmpty()) {
            return tabuleiro[0][2]
        }
        // Verifica empate
        if (tabuleiro.all { linha -> linha.all { it.isNotEmpty() } }) {
            return "Empate"
        }
        // Nenhum vencedor
        return null
    }

    // Reinicia o jogo
    private fun reiniciarJogo() {
        tabuleiro.forEach { linha ->
            linha.fill("")
        }
        binding.buttonZero.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonUm.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonDois.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonTres.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonQuatro.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonCinco.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonSeis.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonSete.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        binding.buttonOito.apply {
            setBackgroundColor(Color.LTGRAY)
            isEnabled = true
        }
        jogadorAtual = "X" // Reinicia para o jogador "X"
    }

    // Configura os listeners dos botões
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
}
