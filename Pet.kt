import kotlin.concurrent.fixedRateTimer

class Pet(
    val nome: String,
    var nivelDeFome: Int = 50,
    var nivelDeFelicidade: Int = 50,
    var nivelDeCansaco: Int = 0,
    var vontadeDeIrAoBanheiro: Int = 0,
    var nivelDeSujeira: Int = 0,
    var idade: Int = 0
) {

    // Método para alimentar o animal
    fun alimentar(amount: Int) {
        nivelDeFome = if (nivelDeFome - amount < 0) 0 else nivelDeFome - amount
        vontadeDeIrAoBanheiro += 10  // Comer aumenta a vontade de ir ao banheiro
        println("$nome foi alimentado! Nível de fome: $nivelDeFome, Vontade de ir ao banheiro: $vontadeDeIrAoBanheiro")
    }

    // Método para brincar com o animal
    fun brincar(amount: Int) {
        nivelDeFome += amount
        nivelDeCansaco += amount / 2  // Brincar aumenta o cansaço
        nivelDeSujeira += 15  // Brincar aumenta a sujeira
        println("Você brincou com $nome! Nível de felicidade: $nivelDeFelicidade, Nível de cansaço: $nivelDeCansaco, Nível de sujeira: $nivelDeSujeira")
    }

    // Método para descansar
    fun descansar(hours: Int) {
        nivelDeCansaco -= hours * 10
        if (nivelDeCansaco < 0) nivelDeCansaco = 0  // Descanso reduz o cansaço
        println("$nome descansou por $hours horas! Nível de cansaço: $nivelDeCansaco")
    }

    // Método para ir ao banheiro
    fun IrAoBanheiro() {
        vontadeDeIrAoBanheiro = 0  // Reseta o medidor de vontade de ir ao banheiro
        println("$nome foi ao banheiro! Nível de vontade de ir ao banheiro: $vontadeDeIrAoBanheiro")
    }

    // Método para tomar banho
    fun tomarBanho() {
        nivelDeSujeira = 0  // Reseta o medidor de sujeira
        println("$nome tomou um banho! Nível de sujeira: $nivelDeSujeira")
    }

    // Método para exibir o status do animal
    fun mostrarStatus() {
        println("Nome: $nome\nNível de Fome: $nivelDeFome\n" +
                "Nível de Felicidade: $nivelDeFelicidade\n" +
                "Nível de Cansaço: $nivelDeCansaco\n" +
                "Idade: $idade\n" +
                "Vontade de ir ao banheiro: $vontadeDeIrAoBanheiro\n" +
                "Nível de Sujeira: $nivelDeSujeira")
    }

    // Simulação da passagem do tempo (aumenta a fome, idade e cheque as condições de perda)
    fun tempoPassando() {
        nivelDeFome += 5
        idade += 1  // Aumenta a idade
        if (nivelDeFome > 100) nivelDeFome = 100
        println("O tempo passou... Nível de fome de $nome: $nivelDeFome, Idade: $idade")
    }

    // Verifica se o pet perdeu
    fun verificarSePerdeu(): Boolean {
        return nivelDeFome == 0 || nivelDeCansaco >= 100 || vontadeDeIrAoBanheiro >= 100 || nivelDeSujeira >= 100
    }
}

// Função para mostrar o menu e permitir interação
fun showMenu(pet: Pet): Boolean {
    println("\nO que você quer fazer?")
    println("1. Alimentar")
    println("2. Brincar")
    println("3. Descansar")
    println("4. Ir ao banheiro")
    println("5. Dar banho")
    println("6. Verificar status")
    println("7. Sair")

    when (readLine()?.toInt()) {
        1 -> {
            println("Quanto quer alimentar?")
            val amount = readLine()?.toInt() ?: 10
            pet.alimentar(amount)
        }
        2 -> {
            println("Quanto tempo quer brincar?")
            val amount = readLine()?.toInt() ?: 10
            pet.brincar(amount)
        }
        3 -> {
            println("Quantas horas quer descansar?")
            val hours = readLine()?.toInt() ?: 1
            pet.descansar(hours)
        }
        4 -> pet.IrAoBanheiro()
        5 -> pet.tomarBanho()
        6 -> pet.mostrarStatus()
        7 -> return false  // Sai do jogo
        else -> println("Escolha inválida!")
    }
    return true  // Continua o jogo
}

// Função principal que inicializa o pet e a interface
fun main() {
    val pet = Pet("Bill")

    // Timer que simula o tempo passando e aumenta a fome e idade a cada 10 segundos
    val timer = fixedRateTimer("timer", false, 0L, 10000L) {
        pet.tempoPassando()
        if (pet.verificarSePerdeu()) {
            println("\nOh não! O bichinho ${pet.nome} perdeu o jogo.")
            cancel()
        }
    }

    // Loop do jogo até o jogador decidir sair ou perder
    var continuePlaying = true
    while (continuePlaying && !pet.verificarSePerdeu()) {
        continuePlaying = showMenu(pet)
    }

    if (!pet.verificarSePerdeu()) {
        println("Você saiu do jogo.")
    } else {
        println("Fim de jogo!")
    }
}
