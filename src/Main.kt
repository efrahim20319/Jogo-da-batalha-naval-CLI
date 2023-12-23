import java.io.File

val abecedarioASCII = 65..90
const val NOT_IMPLEMENTED = 501
const val SUCCESS = 200
const val REDIRECTED = 300
const val REDIRECTION_VALUE = -1
var numLinhas = -1
var numColunas = -1
var tabuleiroHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroComputador: Array<Array<Char?>> = emptyArray()

var tabuleiroPalpitesDoHumano: Array<Array<Char?>> = emptyArray()
var tabuleiroPalpitesDoComputador: Array<Array<Char?>> = emptyArray()


fun dimensoesValidas(numLinhas: Int, numColunas: Int, dimensao: Int) = numLinhas == numColunas &&
        numLinhas == dimensao


fun tamanhoTabuleiroValido(numLinhas: Int, numColunas: Int) = dimensoesValidas(numLinhas, numColunas, 4) ||
        dimensoesValidas(numLinhas, numColunas, 5) ||
        dimensoesValidas(numLinhas, numColunas, 7) ||
        dimensoesValidas(numLinhas, numColunas, 8) ||
        dimensoesValidas(numLinhas, numColunas, 10)

fun ausenciaDeImplementacao(): Int {
    println("!!! POR IMPLEMENTAR, tente novamente")
    menuPrincipal()
    return NOT_IMPLEMENTED // 501 Not implemented http status code
}

fun opcaoInvalida(): Int {
    println("!!! Opcao invalida, tente novamente")
    return 0
}

fun coordenadaValida(coordenadas: String, numLinhas: Int, numColunas: Int): Boolean {
    var eixoX = ""
    var eixoY = ""
    var count = 0
    eixoY += coordenadas.last()
    if ((!(eixoY[0].isLetter())) || (coordenadas.length < 3) || !(',' in coordenadas)) {
        return false
    }
    while (coordenadas[count] != ',') {
        eixoX += coordenadas[count]
        count++
    }
    return (eixoY[0].code in abecedarioASCII) &&
            eixoX.toInt() <= numLinhas &&
            eixoY[0].code <= ((numColunas + 65) - 1)
}

fun processaCoordenadas(coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    if (coordenadaValida(coordenadas, numLinhas, numColunas)) {
        val linhas = coordenadas.split(',')[0].toInt()
        val colunas = coordenadas.split(',')[1]
        return Pair(linhas, (colunas[0].code - 65) + 1)
    }
    return null
}

fun configuracaoNumNavios(dimensao: Int): String {
    when (dimensao) {
        4 -> {
            return "2000"
        }

        5 -> {
            return "1110"
        }

        7 -> {
            return "2111"
        }

        8 -> {
            return "2211"
        }

        10 -> {
            return "3211"
        }

        else -> return ""
    }
}

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int> {
    val arrayRetorno = Array(4) { 0 }
    var configuracaoArrayRetorno = ""
    if (tamanhoTabuleiroValido(numLinhas, numColunas)) {
        configuracaoArrayRetorno = configuracaoNumNavios(numLinhas) // Ou numColunas, nao importa - 2000
        for (index in 0..configuracaoArrayRetorno.length - 1) {
            arrayRetorno[index] = "${configuracaoArrayRetorno[index]}".toInt()
        }
    }
    return arrayRetorno
}

fun calculaTotalNavioAhInserir(confirguracaoNavio: Array<Int>): Int {
    var total = 0
    for (valor in confirguracaoNavio) {
        total += valor
    }
    return total
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int) = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }

fun coordenadaContida(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
    val dimensao = tabuleiro.size
    return (numLinhas >= 1 && numColunas >= 1) && (numLinhas <= dimensao && numColunas <= dimensao)
}

fun limparCoordenadasVazias(coordenadas: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    var numCoordenadaOcupadas = 0
    for (coordenada in coordenadas) {
        if (coordenada.first == 0 && coordenada.second == 0) {
            numCoordenadaOcupadas++
        }
    }
    val arrayRetorno = Array(coordenadas.size - numCoordenadaOcupadas) { Pair(0, 0) } //arrayOfNulls<Pair<Int, Int>>(coordenadas.size - numCoordenadaOcupadas)
    var count = 0
    for (coordenada in coordenadas) {
        if (!(coordenada.first == 0 && coordenada.second == 0)) {
            arrayRetorno[count] = coordenada
            count++
        }
    }
    return arrayRetorno
}

fun juntarCoordenadas(arr1: Array<Pair<Int, Int>>, arr2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val arr1Copia = limparCoordenadasVazias(arr1)
    val arr2Copia = limparCoordenadasVazias(arr2)
    val tamanhoTotal = arr1Copia.size + arr2Copia.size
    val arrayRetorno = Array(tamanhoTotal) { Pair(0, 0) }
    var count = 0
    for (index in arr1Copia) {
        arrayRetorno[count] = index
        count++
    }
    for (index in arr2Copia) {
        arrayRetorno[count] = index
        count++
    }
    return arrayRetorno
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    val dimensaoTabuleiro = tabuleiro.size //Assumindo que eh um tabuleiro valido
    val arrayRetorno = Array(dimensao) { Pair(0, 0) }
    var count = 0
    var countColunas = 0
    var countLinhas = 0
    var casasAhAvancar = dimensao
    if (coordenadaContida(tabuleiro, numLinhas, numColunas)) {
        if (dimensao == 1) {
            arrayRetorno[count] = Pair(numLinhas, numColunas)
            return arrayRetorno
        }
        if (orientacao == "E") {
            countColunas = numColunas
            while (count < dimensao) {
                if (coordenadaContida(tabuleiro, numLinhas, countColunas)) {
                    arrayRetorno[count] = Pair(numLinhas, countColunas)
                    countColunas++
                    count++
                } else {
                    return emptyArray()
                }
            }
        }
        if (orientacao == "S") {
            countLinhas = numLinhas
            while (count < dimensao) {
                if (coordenadaContida(tabuleiro, countLinhas, numColunas)) {
                    arrayRetorno[count] = Pair(countLinhas, numColunas)
                    countLinhas++
                    count++
                } else {
                    return emptyArray()
                }
            }
        }
        if (orientacao == "O") {
            countColunas = numColunas
            while (casasAhAvancar > 0) {
                if (coordenadaContida(tabuleiro, numLinhas, countColunas)) {
                    arrayRetorno[count] = Pair(numLinhas, countColunas)
                    count++
                    casasAhAvancar--
                    countColunas--
                } else {
                    return emptyArray()
                }
            }
        }
        if (orientacao == "N") {
            countLinhas = numLinhas
            while (casasAhAvancar > 0) {
                if (coordenadaContida(tabuleiro, countLinhas, numColunas)) {
                    arrayRetorno[count] = Pair(countLinhas, numColunas)
                    count++
                    casasAhAvancar--
                    countLinhas--
                } else {
                    return emptyArray()
                }
            }
        }
    }
    return arrayRetorno
}

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    val dimensaoTabuleiro = tabuleiro.size //Assumindo que eh um tabuleiro valido
    val numCasasCantos = 4 //O numero de casas nos cantos num quadrado ou retangulo eh sempre 4
    val numCasasExtremos = 2 //O numero de casas nos extremos(direita e esquerda ou cima e baixo, dependendo da orientacao) num quadrado ou retangulo eh sempre 2
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
    val numCoordenadasRedor = dimensao * 2 + numCasasCantos + numCasasExtremos //O numero de coordenadas ao redor obedece sempre a essa formula
    var primeiraPosicaoNavio = coordenadasNavio.first()
    var ultimaPosicaoNavio = coordenadasNavio.last()
    val arrPosicoesCantos = Array(numCasasCantos) { Pair(0, 0) }
    val arrPosicoesExtremos = Array(numCasasExtremos) { Pair(0, 0) }
    val arrCoordenadasRedor /* Esquerda e Direita, ou cima e baixo, dependendo da orientacao */ = Array(dimensao * 2) { Pair(0, 0) }
    val arrCoordenadasTotal = Array(numCoordenadasRedor) { Pair(0, 0) }
    val orientacaoHorizontal: Boolean =
            primeiraPosicaoNavio.first == ultimaPosicaoNavio.first // true=horizontal, false=vertical

    if (orientacao == "O" || orientacao == "N") {
        val temporario = primeiraPosicaoNavio
        primeiraPosicaoNavio = ultimaPosicaoNavio
        ultimaPosicaoNavio = temporario
    }

    if (orientacaoHorizontal) {
        arrPosicoesCantos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second - 1))
        arrPosicoesCantos[1] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first - 1, ultimaPosicaoNavio.second + 1))
        arrPosicoesCantos[2] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first + 1, primeiraPosicaoNavio.second - 1))
        arrPosicoesCantos[3] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second + 1))
    } else {
        arrPosicoesCantos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second - 1))
        arrPosicoesCantos[1] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second + 1))
        arrPosicoesCantos[2] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second - 1))
        arrPosicoesCantos[3] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second + 1))
    }

    if (orientacaoHorizontal) {
        arrPosicoesExtremos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first, primeiraPosicaoNavio.second - 1))
        arrPosicoesExtremos[1] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first, ultimaPosicaoNavio.second + 1))
    } else {
        arrPosicoesExtremos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second))
        arrPosicoesExtremos[1] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second))
    }

    if (orientacaoHorizontal) {
        val casaAcimaDoPrimeiro = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second))
        var count = 0
        if (coordenadaContida(tabuleiro, casaAcimaDoPrimeiro.first, casaAcimaDoPrimeiro.second)) {
            if (dimensao == 1) {
                arrCoordenadasRedor[count] = casaAcimaDoPrimeiro
                count++
            } else {
                for (index in 0..dimensao - 1) {
                    arrCoordenadasRedor[count] = retornaPosicaoSeExiste(tabuleiro, Pair(casaAcimaDoPrimeiro.first, casaAcimaDoPrimeiro.second + index))
                    count++
                }
            }
        }
        val casaAbaixoDoPrimeiro = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first + 1, primeiraPosicaoNavio.second))
        if (coordenadaContida(tabuleiro, casaAbaixoDoPrimeiro.first, casaAbaixoDoPrimeiro.second)) {
            if (dimensao == 1) {
                arrCoordenadasRedor[count] = casaAbaixoDoPrimeiro
            } else {
                for (index in 0..dimensao - 1) {
                    arrCoordenadasRedor[count] = retornaPosicaoSeExiste(tabuleiro, Pair(casaAbaixoDoPrimeiro.first, casaAbaixoDoPrimeiro.second + index))
                    count++
                }
            }
        }
    } else {
        val casaEsquerdaDoPrimeiro = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first, primeiraPosicaoNavio.second - 1))
        var count = 0
        if (coordenadaContida(tabuleiro, casaEsquerdaDoPrimeiro.first, casaEsquerdaDoPrimeiro.second)) {
            if (dimensao == 1) {
                arrCoordenadasRedor[count] = casaEsquerdaDoPrimeiro
                count++
            } else {
                for (index in 0..dimensao - 1) {
                    arrCoordenadasRedor[count] = retornaPosicaoSeExiste(tabuleiro, Pair(casaEsquerdaDoPrimeiro.first + index, casaEsquerdaDoPrimeiro.second))
                    count++
                }
            }
        }
        val casaDireitaDoPrimeiro = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first, primeiraPosicaoNavio.second + 1))
        if (coordenadaContida(tabuleiro, casaDireitaDoPrimeiro.first, casaDireitaDoPrimeiro.second)) {
            if (dimensao == 1) {
                arrCoordenadasRedor[count] = casaDireitaDoPrimeiro
            } else {
                for (index in 0..dimensao - 1) {
                    arrCoordenadasRedor[count] = retornaPosicaoSeExiste(tabuleiro, Pair(casaDireitaDoPrimeiro.first + index, casaDireitaDoPrimeiro.second))
                    count++
                }
            }
        }
    }

    return limparCoordenadasVazias(juntarCoordenadas(juntarCoordenadas(arrPosicoesCantos, arrPosicoesExtremos), arrCoordenadasRedor))
}


fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, confirguracaoNavio: Array<Int>) {
    val dimensaoTabuleiro = tabuleiro.size
    var linhaAleatoria: Int
    var colunaAleatoria: Int
    var orientacaoAleatoria: String
    var dimensao: Int
    for (index in 0 until confirguracaoNavio.size) {
        linhaAleatoria = (1..dimensaoTabuleiro).random()
        colunaAleatoria = (1..dimensaoTabuleiro).random()
        orientacaoAleatoria = arrayOf("N", "S", "E", "O")[(0..3).random()]
        dimensao = if (confirguracaoNavio[index] != 0) index + 1 else 0
        for (posicao in 0 until confirguracaoNavio[index]) {
            while (!(insereNavio(tabuleiro, linhaAleatoria, colunaAleatoria, orientacaoAleatoria, dimensao))) {
                linhaAleatoria = (1..dimensaoTabuleiro).random()
                colunaAleatoria = (1..dimensaoTabuleiro).random()
                orientacaoAleatoria = arrayOf("N", "S", "E", "O")[(0..3).random()]
            }
        }
    }
}


fun restoDoNavioCompleto(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
    //Está funcao somente funciona assumindo que o par par linha e coluna aponanta sempre para ou a ultima posicao do array
    if (tabuleiro[numLinhas - 1][numColunas - 1] == '1') {
        return true
    }
    if (tabuleiro[numLinhas - 1][numColunas - 1] == null) {
        return false
    }
    val dimensao = tabuleiro[numLinhas - 1][numColunas - 1]!!.code - 48
    val posicoesEste = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "E", dimensao)
    val posicoesOeste = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "O", dimensao)
    val posicoesNorte = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "N", dimensao)
    val posicoesSul = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "S", dimensao)
    val valorPosicao = tabuleiro[numLinhas - 1][numColunas - 1]
    var count: Int
    for (posicoes in arrayOf(posicoesEste, posicoesNorte, posicoesOeste, posicoesSul)) {
        count = 0
        for (posicao in posicoes) {
            if (tabuleiro[posicao.first - 1][posicao.second - 1] == valorPosicao) {
                count++
            }
            if (count == dimensao) {
                return true
            }
        }
    }
    return false
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
    val coordenadasDeTodosNavios = retornaCoordenadasDeTodosNaviosCompletos(tabuleiro)
    for (coordenadas in coordenadasDeTodosNavios) {
        if (Pair(numLinhas, numColunas) in coordenadas) {
            return true
        }
    }
    return false
}

fun orientacaoNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): String { //Essa funcao so funciona assumindo que e a primeira ou a ultima posicao do navio
    val dimensao = tabuleiro[numLinhas - 1][numColunas - 1]!!.code - 48
    val posicoesEste = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "E", dimensao)
    val posicoesOeste = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "O", dimensao)
    val posicoesNorte = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "N", dimensao)
    val posicoesSul = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, "S", dimensao)
    val valorPosicao = tabuleiro[numLinhas - 1][numColunas - 1]
    var count: Int
    val arrOrientacoes = arrayOf("E", "O", "N", "S")
    var countPosicoes = 0
    for (posicoes in arrayOf(posicoesEste, posicoesNorte, posicoesOeste, posicoesSul)) {
        count = 0
        for (posicao in posicoes) {
            if (tabuleiro[posicao.first - 1][posicao.second - 1] == valorPosicao) {
                count++
            }
            if (count == dimensao) {
                return arrOrientacoes[countPosicoes]
            }
        }
        countPosicoes++
    }
    return "" //Orientacao invalida
}


fun retornaCoordenadasDeTodosNaviosCompletos(tabuleiro: Array<Array<Char?>>): Array<Array<Pair<Int, Int>>> {
    val dimensao = tabuleiro.size
    val totalPosicoes = Array(calculaTotalNavioAhInserir(calculaNumNavios(dimensao, dimensao))) { emptyArray<Pair<Int, Int>>() }
    var dimensaoDoNavio: Int
    var orientacaoDoNavio: String
    var coordenadasNavio: Array<Pair<Int, Int>>
    var coordenadasAhIgnorar: Array<Pair<Int, Int>> = emptyArray()
    var count = 0
    for (countLinha in 1..dimensao) {
        for (countColuna in 1..dimensao) {
            if (!(Pair(countLinha, countColuna) in coordenadasAhIgnorar)) {
                if (restoDoNavioCompleto(tabuleiro, countLinha, countColuna)) {
                    dimensaoDoNavio = "${tabuleiro[countLinha - 1][countColuna - 1]}".toInt()
                    orientacaoDoNavio = orientacaoNavio(tabuleiro, countLinha, countColuna)
                    coordenadasNavio = gerarCoordenadasNavio(tabuleiro, countLinha, countColuna, orientacaoDoNavio, dimensaoDoNavio)
                    totalPosicoes[count] = coordenadasNavio
                    coordenadasAhIgnorar = juntarCoordenadas(coordenadasAhIgnorar, coordenadasNavio)
                    count++
                }
            }
        }
    }
    var totalNaviosCompletos = 0
    for (coordenadas in totalPosicoes) {
        if (coordenadas.size > 0) {
            totalNaviosCompletos++
        }
    }
    var totalPosicoesFinal = Array<Array<Pair<Int, Int>>>(totalNaviosCompletos) { emptyArray() }
    count = 0
    for (coordenadas in totalPosicoes) {
        if (coordenadas.size > 0) {
            totalPosicoesFinal[count] = coordenadas
            count++
        }
    }
    return totalPosicoesFinal
}

fun lancarTiro(tabuleiroRealAdversario: Array<Array<Char?>>, tabuleiroPalpitesJogador: Array<Array<Char?>>, coordenada: Pair<Int, Int>): String {
    val valorTabuleiroReal = tabuleiroRealAdversario[coordenada.first - 1][coordenada.second - 1]
    val tipoNavio = when (valorTabuleiroReal) {
        '1' -> "submarino."
        '2' -> "contra-torpedeiro."
        '3' -> "navio-tanque."
        '4' -> "porta-avioes."
        null -> "Agua."
        else -> "desconhecido."
    }
    if (tipoNavio == "Agua.") {
        tabuleiroPalpitesJogador[coordenada.first - 1][coordenada.second - 1] = 'X'
    } else {
        tabuleiroPalpitesJogador[coordenada.first - 1][coordenada.second - 1] = valorTabuleiroReal
    }
    if (tipoNavio == "Agua.") {
        return tipoNavio
    }
    return "Tiro num $tipoNavio"
}

fun geraTiroComputador(tabuleiroPalpitesComputador: Array<Array<Char?>>): Pair<Int, Int> {
    val dimensaoTabuleiro = tabuleiroPalpitesComputador.size
    var linhaAleatoria = (1..dimensaoTabuleiro).random()
    var colunaAleatoria = (1..dimensaoTabuleiro).random()
    while (tabuleiroPalpitesComputador[linhaAleatoria - 1][colunaAleatoria - 1] != null) {
        linhaAleatoria = (1..dimensaoTabuleiro).random()
        colunaAleatoria = (1..dimensaoTabuleiro).random()
    }
    return Pair(linhaAleatoria, colunaAleatoria)
}

fun contarNaviosDeDimensao(tabuleiro: Array<Array<Char?>>, dimensao: Int): Int {
    val dimensaoTabuleiro = tabuleiro.size
    var coordenadasAhIgnorar = emptyArray<Pair<Int, Int>>()
    var coordenadasDoNavio = emptyArray<Pair<Int, Int>>()
    var valorPosicao: Char? = ' '
    var countNavios = 0
    var orientacaoNavio = ""
    for (countLinha in 1..dimensaoTabuleiro) {
        for (countColuna in 1..dimensaoTabuleiro) {
            valorPosicao = tabuleiro[countLinha - 1][countColuna - 1] ?: '0'
            if (!(Pair(countLinha, countColuna) in coordenadasAhIgnorar) && valorPosicao != 'X') {
                if ("$valorPosicao".toInt() == dimensao) { //navioCompleto(tabuleiro, countLinha, countColuna)
                    orientacaoNavio = orientacaoNavio(tabuleiro, countLinha, countColuna)
                    coordenadasDoNavio = gerarCoordenadasNavio(tabuleiro, countLinha, countColuna, orientacaoNavio, dimensao)
                    if (orientacaoNavio != "" && "$valorPosicao".toInt() != 1 && !estaLivre(tabuleiro, coordenadasDoNavio)) {
                        coordenadasAhIgnorar = juntarCoordenadas(coordenadasAhIgnorar, coordenadasDoNavio)
                        countNavios++
                    }
                    if ("$valorPosicao".toInt() == 1) {
                        countNavios++
                    }
                }
            }
        }
    }
    return countNavios
}

fun venceu(tabuleiro: Array<Array<Char?>>): Boolean {
    val dimensaoTabuleiro = tabuleiro.size
    val numNavios = calculaNumNavios(dimensaoTabuleiro, dimensaoTabuleiro)
    for (index in 0 until numNavios.size) {
        if (contarNaviosDeDimensao(tabuleiro, index + 1) != numNavios[index]) {
            return false
        }
    }
    return true
}

fun retornaPosicaoSeDisponivel(tabuleiro: Array<Array<Char?>>, posicao: Pair<Int, Int>): Pair<Int, Int> {
    if (estaLivre(tabuleiro, arrayOf(posicao))) return posicao
    return Pair(0, 0)
}

fun retornaPosicaoSeExiste(tabuleiro: Array<Array<Char?>>, posicao: Pair<Int, Int>): Pair<Int, Int> {
    if (coordenadaContida(tabuleiro, posicao.first, posicao.second)) return posicao
    return Pair(0, 0)
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, posicoes: Array<Pair<Int, Int>>): Boolean {
    for (posicao in posicoes) {
        if (!(coordenadaContida(tabuleiro, posicao.first, posicao.second))) {
            return false
        } // Um AND nao simplificaria isso, na verdade daria erro pois estaria acessando uma posicao do tabuleiro que nao existe
        if ((tabuleiro[posicao.first - 1][posicao.second - 1] != null)) {
            return false
        }
    }
    return true
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String, dimensao: Int): Boolean {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
    if (coordenadasNavio.isEmpty()) {
        return false
    }
    val coordenadasAhVolta = gerarCoordenadasFronteira(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
    val juncaoCoordenadas = juntarCoordenadas(coordenadasAhVolta, coordenadasNavio)

    if (estaLivre(tabuleiro, juncaoCoordenadas)) {
        for (coordenada in coordenadasNavio) {
            tabuleiro[coordenada.first - 1][coordenada.second - 1] = (dimensao + 48).toChar() //48 == 0 Na tabela ASCII
        }
        return true
    }
    return false
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, dimensao: Int) = insereNavio(tabuleiro, numLinhas, numColunas, "E", dimensao)


fun criaLegendaHorizontal(numColunas: Int): String {
    var count = 0
    var legendaHorizontal = ""
    while (count < numColunas) {
        legendaHorizontal += (count + 65).toChar()
        if ((count != numColunas - 1)) {
            legendaHorizontal += " | "
        }
        count++

    }
    return legendaHorizontal
}

fun criaLinha(numLinhas: Int, numColunas: Int, valorEntreColuna: Char): String {
    var countLinha = 0
    var countColuna = 0
    var linha = ""
    while (countLinha < numLinhas) {
        while (countColuna < numColunas) {
            linha += "|   "
            if ((countColuna == numColunas - 1)) {
                linha += "|"
            }
            countColuna++
        }
        countLinha++
    }
    return linha
}

fun obtemMapa(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean): Array<String> {
    val dimensaoTabuleiro = tabuleiro.size
    val numLinhas = dimensaoTabuleiro
    val numColunas = dimensaoTabuleiro
    val mapa: Array<String> = Array(numLinhas + 1) { "" }
    var valorEntreColuna: Char?
    var linha = ""
    var navioCompleto: Boolean
    for (countLinhas in 1..numLinhas) {
        linha = ""
        for (countColunas in 1..numColunas) {
            if (tabuleiro[countLinhas - 1][countColunas - 1] == null) {
                valorEntreColuna = if (tabuleiroReal) {
                    '~'
                } else {
                    '?'
                }
            } else {
                if (tabuleiroReal || (tabuleiro[countLinhas - 1][countColunas - 1] == 'X')) {
                    valorEntreColuna = tabuleiro[countLinhas - 1][countColunas - 1]
                } else {
                    navioCompleto = navioCompleto(tabuleiro, countLinhas, countColunas)
                    valorEntreColuna = if ((navioCompleto)) {
                        tabuleiro[countLinhas - 1][countColunas - 1]
                    } else {
                        when (tabuleiro[countLinhas - 1][countColunas - 1]) {
                            '2' -> '\u2082'
                            '3' -> '\u2083'
                            '4' -> '\u2084'
                            else -> ' '
                        }
                    }
                }
            }
            linha += "| $valorEntreColuna "
        }
        linha += "| $countLinhas"
        mapa[countLinhas] = linha

    }
    mapa[0] = "| ${criaLegendaHorizontal(numColunas)} |"
    return mapa
}

fun podeRedirecionar(inputUtilizador: Int?) = inputUtilizador == REDIRECTION_VALUE

fun menuPrincipal(): Int {
    var menuString = ""
    menuString += "\n"
    menuString += "> > Batalha Naval < <" + "\n\n"
    menuString += "1 - Definir Tabuleiro e Navios" + "\n"
    menuString += "2 - Jogar" + "\n"
    menuString += "3 - Gravar" + "\n"
    menuString += "4 - Ler" + "\n"
    menuString += "0 - Sair"
    menuString += "\n"
    println(menuString)
    return 10
}

fun veridicaOrientacao(orientacao: String) = orientacao.length == 1 && orientacao[0] == 'N' ||
        orientacao[0] == 'S' ||
        orientacao[0] == 'E' ||
        orientacao[0] == 'O'


fun pedirAtehAcertar(mensagemPropt: String, mensagemErro: String): Int {
    var variavelDeRetorno: Int? = null
    while (variavelDeRetorno == null) {
        println(mensagemPropt)
        variavelDeRetorno = readln().toIntOrNull()
        if (podeRedirecionar(variavelDeRetorno)) {
            return -1
        }
        if (variavelDeRetorno == null) {
            println("!!! ${mensagemErro}, tente novamente")
        }
    }

    return variavelDeRetorno
}

fun menuDefinirTabuleiro(): Int {
    var linhas: Int? = null
    var colunas: Int? = null
    var coordenadas = ""
    var orientacao: String? = null
    var coordenadaValida = false
    var tamanhoTabuleiroValido = false
    var orientacaoValida = false
    var naviosAhInserir = 0
    var confirguracaoNavio: Array<Int>
    val arrayDeNomeDeNavio = arrayOf("submarino", "contra-torpedeiro", "navio-tanque", "porta-avioes")
    var navioInserido = false
    println("${'\n'}> > Batalha Naval < <${'\n'}")
    println("Defina o tamanho do tabuleiro:")
    while (!tamanhoTabuleiroValido) {
        linhas = pedirAtehAcertar("Quantas linhas?", "Número de linhas invalidas")
        if (podeRedirecionar(linhas)) {
            return REDIRECTED
        }
        colunas = pedirAtehAcertar("Quantas colunas?", "Número de colunas invalidas")
        if (podeRedirecionar(colunas)) {
            return REDIRECTED
        }
        if (!(tamanhoTabuleiroValido(linhas, colunas))) {
            println("Tamanho de tabuleiro Invalido, tente novamente")
            linhas = null
            colunas = null
        } else tamanhoTabuleiroValido = true
    }
    tabuleiroHumano = criaTabuleiroVazio(linhas!!, colunas!!)
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(linhas!!, colunas!!)
    tabuleiroComputador = criaTabuleiroVazio(linhas!!, colunas!!)
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(linhas!!, colunas!!)
    confirguracaoNavio = calculaNumNavios(linhas, colunas)
    naviosAhInserir = calculaTotalNavioAhInserir(confirguracaoNavio)
    mostraMapa(tabuleiroHumano, true)
    var naviosAhInserirNaquelaPosicao = 0
    var count = 0
    while (naviosAhInserir > 0) {
        naviosAhInserirNaquelaPosicao = confirguracaoNavio[count]
        var coordenadasProcessadas: Pair<Int, Int>? = null
        while (naviosAhInserirNaquelaPosicao > 0) {
            while (!coordenadaValida) {
                println("Insira as coordenadas de um ${arrayDeNomeDeNavio[count]}:")
                println("Coordenadas? (ex: 6,G)")
                coordenadas = readln()
                coordenadaValida = coordenadaValida(coordenadas, linhas!!, colunas!!)
                if (podeRedirecionar(coordenadas.toIntOrNull())) {
                    return REDIRECTED
                }
                if ((coordenadaValida)) {
                    coordenadasProcessadas = processaCoordenadas(coordenadas, linhas, colunas)
                } else {
                    println("!!! Coordenadas invalidas, tente novamente")
                }
            }
            if (count >= 1) {
                while (!orientacaoValida) {
                    println("Insira a orientacao do navio:")
                    println("Orientacao? (N, S, E, O)")
                    orientacao = readln()
                    orientacaoValida = veridicaOrientacao(orientacao)
                    if (podeRedirecionar(orientacao.toIntOrNull())) {
                        return REDIRECTED
                    }
                    if (!(orientacaoValida)) {
                        println("!!! Orientacao invalida, tente novamente")
                    }
                }
                if (insereNavio(tabuleiroHumano, coordenadasProcessadas!!.first, coordenadasProcessadas.second, orientacao!!, count + 1)) {
                    navioInserido = true
                    naviosAhInserirNaquelaPosicao--
                    naviosAhInserir--
                }
            } else if (insereNavio(tabuleiroHumano, coordenadasProcessadas!!.first, coordenadasProcessadas.second, "N", count + 1)) {
                navioInserido = true
                naviosAhInserirNaquelaPosicao--
                naviosAhInserir--
            }
            if (navioInserido) {
                mostraMapa(tabuleiroHumano, true)
            } else {
                println("!!! Posicionamento invalido, tente novamente")
            }
            coordenadaValida = false
            orientacaoValida = false
            navioInserido = false
        }
        count++
    }
    preencheTabuleiroComputador(tabuleiroComputador, confirguracaoNavio)
    println("Pretende ver o mapa gerado pelo computador? (S/N)")
    var respostaVerMapaComputador = readln()
    if (respostaVerMapaComputador == "S") {
        mostraMapa(tabuleiroComputador, true)
    }
    return SUCCESS
}

fun menuJogar(tabuleiroDefinido: Boolean): Int {
    if (!tabuleiroDefinido) {
        printErroTabuleiroNaoDefinido()
        return REDIRECTED
    }
    while (true) {
        var coordenadas: String
        var coordenadaValida = false
        var coordenadasProcessadas: Pair<Int, Int>? = null
        val dimensaoTabuleiro = tabuleiroHumano.size
        var alvoAtingido = ""
        var alvoAtingidoComputador: Pair<Int, Int>
        while (!coordenadaValida) {
            mostraMapa(tabuleiroPalpitesDoHumano, false)
            println("Insira a posição em que pretende atingir")
            println("Coordenadas? (ex: 6,G)")
            coordenadas = readln()
            coordenadaValida = coordenadaValida(coordenadas, dimensaoTabuleiro, dimensaoTabuleiro)
            if (podeRedirecionar(coordenadas.toIntOrNull())) {
                return REDIRECTED
            }
            if ((coordenadaValida)) {
                coordenadasProcessadas = processaCoordenadas(coordenadas, dimensaoTabuleiro, dimensaoTabuleiro)
            } else {
                println("!!! Coordenadas invalidas, tente novamente")
            }
        }
        alvoAtingido = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas!!)
        println(">>> HUMANO >>>${alvoAtingido}${if (navioCompleto(tabuleiroPalpitesDoHumano, coordenadasProcessadas.first, coordenadasProcessadas.second)) " Navio ao fundo!" else ""}")
        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("PARABENS! Venceu o jogo")
            println("Prima enter para voltar ao menu principal")
            readln()
            return SUCCESS
        }
        alvoAtingidoComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
        println("Computador lancou tiro para a posicao (${alvoAtingidoComputador.first}, ${alvoAtingidoComputador.second})")
        alvoAtingido = lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, alvoAtingidoComputador)
        println(">>> COMPUTADOR >>>${alvoAtingido}${if (navioCompleto(tabuleiroPalpitesDoComputador, alvoAtingidoComputador.first, alvoAtingidoComputador.second)) " Navio ao fundo!" else ""}")
        if (venceu(tabuleiroPalpitesDoComputador)) {
            println("PARABENS! Venceu o jogo")
            println("Prima enter para voltar ao menu principal")
            readln()
            return SUCCESS
        }
    }
}

fun menuGravarJogo(tabuleiroDefinido: Boolean): Int {
    if (!tabuleiroDefinido) {
        printErroTabuleiroNaoDefinido()
        return REDIRECTED
    }
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readln()
    val dimensao = tabuleiroHumano.size
    gravarJogo(nomeDoFicheiro, tabuleiroHumano, tabuleiroPalpitesDoHumano, tabuleiroComputador, tabuleiroPalpitesDoComputador)
    println("Tabuleiro ${dimensao}x${dimensao} gravado com sucesso")
    return SUCCESS
}

fun menuLerJogo(): Int {
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readln()
    val tabuleiros = arrayOf(tabuleiroHumano, tabuleiroPalpitesDoHumano, tabuleiroComputador, tabuleiroPalpitesDoComputador)
    for (countTabuleiro in 0 until tabuleiros.size) {
        tabuleiros[countTabuleiro] = lerJogo(nomeDoFicheiro, countTabuleiro + 1)
    }
    val dimensao = tabuleiroHumano.size
    println("Tabuleiro ${dimensao}x${dimensao} lido com sucesso")
    mostraMapa(tabuleiroHumano, true)
    return SUCCESS
}

fun tipoNavioAtingido(tabuleiro: Array<Array<Char?>>, coordenada: Pair<Int, Int>): String {
    val valorTabuleiro = tabuleiro[coordenada.first - 1][coordenada.second - 1]
    val tipoNavio = when (valorTabuleiro) {
        '1' -> "submarino."
        '2' -> "contra-torpedeiro."
        '3' -> "navio-tanque."
        '4' -> "porta-avioes."
        null -> "Agua."
        else -> "desconhecido."
    }
    return tipoNavio
}

fun lerJogo(nomeDoFicheiro: String, tipoDoTabuleiro: Int): Array<Array<Char?>> {
    val linhasFicheiro = File(nomeDoFicheiro).readLines()
    val dimensao = "${linhasFicheiro[0][0]}".toInt()
    val primeiraLinhasDosTabuleiros = arrayOf(5, (5) + (dimensao + 3) * 1, (5) + (dimensao + 3) * 2, (5) + (dimensao + 3) * 3)
    tabuleiroHumano = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroComputador = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(dimensao, dimensao)
    val tabuleiros = arrayOf(tabuleiroHumano, tabuleiroPalpitesDoHumano, tabuleiroComputador, tabuleiroPalpitesDoComputador)
    var count = 0
    var countLinha: Int
    var countColuna: Int
    for (primeiraLinhaDeTabuleiro in primeiraLinhasDosTabuleiros) {
        countLinha = 0
        for (linha in primeiraLinhaDeTabuleiro until primeiraLinhaDeTabuleiro + dimensao) {
            countColuna = 0
            for (coluna in linhasFicheiro[linha - 1]) {
                if (coluna == ',') {
                    countColuna++
                }
                if (coluna.isDigit() || coluna == 'X') {
                    tabuleiros[count][countLinha][countColuna] = coluna
                }
            }
            countLinha++
        }
        count++
    }
    return tabuleiros[tipoDoTabuleiro - 1]
}

fun gravarJogo(nomeDoFicheiro: String, tabuleiroRealHumano: Array<Array<Char?>>, tabuleiroPalpitesHumano: Array<Array<Char?>>, tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesComputador: Array<Array<Char?>>) {
    val dimensao = tabuleiroRealHumano.size
    val escreveFicheiro = File(nomeDoFicheiro).printWriter()
    val tabuleirosJogador = arrayOf(tabuleiroRealHumano, tabuleiroPalpitesHumano)
    val tabuleirosComputador = arrayOf(tabuleiroRealComputador, tabuleiroPalpitesComputador)
    val opcoesTabuleiro = arrayOf("Real", "Palpites")
    var count = 0
    escreveFicheiro.println("$dimensao,$dimensao")
    escreveFicheiro.println()
    for (tabuleiro in tabuleirosJogador) {
        escreveFicheiro.println("Jogador")
        escreveFicheiro.println(opcoesTabuleiro[count])
        for (countLinha in 0 until tabuleiro.size) {
            for (countColuna in 0 until tabuleiro.size) {
                escreveFicheiro.print((tabuleiro[countLinha][countColuna]) ?: (""))
                if (countColuna != tabuleiro.size - 1) {
                    escreveFicheiro.print(",")
                }
            }
            escreveFicheiro.println()
        }
        count++
        escreveFicheiro.println()
    }
    count = 0
    for (tabuleiro in tabuleirosComputador) {
        escreveFicheiro.println("Computador")
        escreveFicheiro.println(opcoesTabuleiro[count])
        for (countLinha in 0 until tabuleiro.size) {
            for (countColuna in 0 until tabuleiro.size) {
                escreveFicheiro.print((tabuleiro[countLinha][countColuna]) ?: (""))
                if (countColuna != tabuleiro.size - 1) {
                    escreveFicheiro.print(",")
                }
            }
            escreveFicheiro.println()
        }
        if (count != tabuleirosComputador.size - 1) {
            escreveFicheiro.println()
        }
        count++
    }
    escreveFicheiro.close()
}

fun mostraMapa(tabuleiro: Array<Array<Char?>>, tabuleiroReal: Boolean) {
    for (linha in obtemMapa(tabuleiro, tabuleiroReal)) {
        println(linha)
    }
}

fun printErroTabuleiroNaoDefinido() {
    println("!!! Tem que primeiro definir o tabuleiro do jogo, tente novamente")
}

fun main() {
    var menuAtual: Int? = menuPrincipal()
    var tabuleiroDefinido = false
    while (true) {
        menuAtual = readln().toIntOrNull()
        menuAtual = when (menuAtual) {
            1 -> menuDefinirTabuleiro()
            2 -> menuJogar(tabuleiroDefinido)
            3 -> menuGravarJogo(tabuleiroDefinido)
            4 -> menuLerJogo()
            0 -> return
            else -> opcaoInvalida()
        }
        if (menuAtual == SUCCESS || menuAtual == REDIRECTED) {
            if (menuAtual == SUCCESS) {
                tabuleiroDefinido = true
            }
            menuPrincipal()
        }
    }
}
