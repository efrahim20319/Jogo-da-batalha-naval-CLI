val abecedarioASCII = 65..90
const val NOT_IMPLEMENTED = 501
const val SUCCESS = 200
const val REDIRECTED = 300
const val REDIRECTION_VALUE = -1

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
    return  NOT_IMPLEMENTED // 501 Not implemented http status code
}

fun opcaoInvalida(): Int {
    println("!!! Opcao invalida, tente novamente")
    return 0
}

fun coordenadaValida(coordenadas: String, numLinhas: Int, numColunas: Int): Boolean {
    var eixoX = ""
    var eixoY= ""
    var count = 0
    eixoY += coordenadas.last()
    if ((!(eixoY[0].isLetter())) || (coordenadas.length < 3) || !(',' in coordenadas)) {
        return false
    }
    while (coordenadas[count] != ',') {
        eixoX += coordenadas[count]
        count++
    }
    return  (eixoY[0].code in abecedarioASCII) &&
            eixoX.toInt() <= numLinhas &&
            eixoY[0].code <= ((numColunas + 65) - 1)
}

fun processaCoordenadas (coordenadas: String, numLinhas: Int, numColunas: Int): Pair<Int, Int>? {
    if (coordenadaValida(coordenadas, numLinhas, numColunas)) {
        val linhas = coordenadas.split(',')[0].toInt()
        val colunas = coordenadas.split(',')[1]
        return Pair(linhas, (colunas[0].code - 65) + 1)
    }
    return null
}

fun configuracaoNumNavios (dimensao: Int): String {
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

fun calculaNumNavios(numLinhas: Int, numColunas: Int): Array<Int?> {
    val arrayRetorno = arrayOfNulls<Int>(4)
    var configuracaoArrayRetorno = ""
    if (tamanhoTabuleiroValido(numLinhas, numColunas)) {
        configuracaoArrayRetorno = configuracaoNumNavios(numLinhas) // Ou numColunas, nao importa
        for (index in 0..configuracaoArrayRetorno.length - 1) {
            arrayRetorno[index] = "${configuracaoArrayRetorno[index]}".toInt()
        }
    }
    return arrayRetorno
}

fun criaTabuleiroVazio(numLinhas: Int, numColunas: Int) = Array(numLinhas) { arrayOfNulls<Char?>(numColunas) }

fun coordenadaContida(tabuleiro:Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
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
    val arrayRetorno =  Array(coordenadas.size - numCoordenadaOcupadas) { Pair(0,0) } //arrayOfNulls<Pair<Int, Int>>(coordenadas.size - numCoordenadaOcupadas)
    var count = 0
    for (coordenada in coordenadas) {
        if (!(coordenada.first == 0 && coordenada.second == 0)) {
            arrayRetorno[count] = coordenada
            count++
        }
    }
    return arrayRetorno
}

fun  juntarCoordenadas(arr1: Array<Pair<Int, Int>>, arr2: Array<Pair<Int, Int>>): Array<Pair<Int, Int>> {
    val arr1Copia = limparCoordenadasVazias(arr1)
    val arr2Copia = limparCoordenadasVazias(arr2)
    val tamanhoTotal = arr1Copia.size + arr2Copia.size
    val arrayRetorno = Array(tamanhoTotal) { Pair(0,0) }
    var count = 0
    for (index in arr1Copia) {
        arrayRetorno[count] = index
        count++
    }
    for (index in arr2Copia) {
        arrayRetorno[count] = index
        count++
    }
    return  arrayRetorno
}

fun gerarCoordenadasNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    val dimensaoTabuleiro = tabuleiro.size //Assumindo que eh um tabuleiro valido
    val arrayRetorno = Array(dimensao) { Pair(0,0) }
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
    val arrPosicoesCantos = Array(numCasasCantos) { Pair(0,0) }
    val arrPosicoesExtremos = Array(numCasasExtremos) { Pair(0,0) }
    val arrCoordenadasRedor /* Esquerda e Direita, ou cima e baixo, dependendo da orientacao */ =Array(dimensao * 2) { Pair(0,0) }
    val arrCoordenadasTotal = Array(numCoordenadasRedor) { Pair(0,0) }
    val orientacaoHorizontal: Boolean =
        primeiraPosicaoNavio.first == ultimaPosicaoNavio.first // true=horizontal, false=vertical

    if (orientacao == "O" || orientacao == "N") {
        val temporario = primeiraPosicaoNavio
        primeiraPosicaoNavio = ultimaPosicaoNavio
        ultimaPosicaoNavio = temporario
    }

    if (orientacaoHorizontal) {
        arrPosicoesCantos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second - 1))
        arrPosicoesCantos[1] =  retornaPosicaoSeExiste(tabuleiro,  Pair(ultimaPosicaoNavio.first - 1, ultimaPosicaoNavio.second + 1))
        arrPosicoesCantos[2] =  retornaPosicaoSeExiste(tabuleiro,  Pair(primeiraPosicaoNavio.first + 1, primeiraPosicaoNavio.second - 1))
        arrPosicoesCantos[3] =  retornaPosicaoSeExiste(tabuleiro,  Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second + 1))
    } else {
        arrPosicoesCantos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second - 1))
        arrPosicoesCantos[1] =  retornaPosicaoSeExiste(tabuleiro,  Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second + 1))
        arrPosicoesCantos[2] =  retornaPosicaoSeExiste(tabuleiro,  Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second - 1))
        arrPosicoesCantos[3] =  retornaPosicaoSeExiste(tabuleiro,  Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second + 1))
    }

    if (orientacaoHorizontal) {
        arrPosicoesExtremos[0] =  retornaPosicaoSeExiste(tabuleiro,  Pair(primeiraPosicaoNavio.first, primeiraPosicaoNavio.second - 1))
        arrPosicoesExtremos[1] =  retornaPosicaoSeExiste(tabuleiro,  Pair(ultimaPosicaoNavio.first, ultimaPosicaoNavio.second + 1))
    } else {
        arrPosicoesExtremos[0] =  retornaPosicaoSeExiste(tabuleiro,  Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second))
        arrPosicoesExtremos[1] =  retornaPosicaoSeExiste(tabuleiro,  Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second))
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


fun preecheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, confirguracaoNavio: Array<Int>) {
    val dimensaoTabuleiro = tabuleiro.size
    var linhaAleatoria: Int
    var colunaAleatoria: Int
    var orientacaoAleatoria: String
    var dimensao: Int
    for (index in 0 until  confirguracaoNavio.size) {
        linhaAleatoria = (1..dimensaoTabuleiro).random()
        colunaAleatoria = (1..dimensaoTabuleiro).random()
        orientacaoAleatoria = arrayOf("N", "S", "E", "O")[(0..3).random()]
        dimensao = if (confirguracaoNavio[index] != 0) index+1 else 0
        for (posicao in 0 until confirguracaoNavio[index]) {
            while (!(insereNavio(tabuleiro, linhaAleatoria, colunaAleatoria, orientacaoAleatoria, dimensao))) {
                linhaAleatoria = (1..dimensaoTabuleiro).random()
                colunaAleatoria = (1..dimensaoTabuleiro).random()
                orientacaoAleatoria = arrayOf("N", "S", "E", "O")[(0..3).random()]
            }
        }
    }
}


fun navioCompleto(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
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
            if (tabuleiro[posicao.first - 1][posicao.second - 1] == valorPosicao){
                count++
            }
            if (count == dimensao) {
                return true
            }
        }
    }
    return false
}

//fun obtemMapa(): Boolean {
//    val dimensaoTabuleiro = tabuleiro.size
//    val mapa = Array(dimensaoTabuleiro) { Array(dimensaoTabuleiro) { "" } }
//    for (linha in 0 until tabuleiro.size) {
//
//
//    }
//}

fun retornaPosicaoSeDisponivel(tabuleiro: Array<Array<Char?>>, posicao: Pair<Int, Int>): Pair<Int, Int> {
    if (estaLivre(tabuleiro, arrayOf(posicao))) return posicao
    return Pair(0,0)
}

fun retornaPosicaoSeExiste(tabuleiro: Array<Array<Char?>>, posicao: Pair<Int, Int>): Pair<Int, Int> {
    if (coordenadaContida(tabuleiro, posicao.first, posicao.second)) return posicao
    return Pair(0,0)
}

fun estaLivre(tabuleiro: Array<Array<Char?>>, posicoes: Array<Pair<Int, Int>>): Boolean {
    for (posicao in posicoes) {
        if (!(coordenadaContida(tabuleiro, posicao.first, posicao.second))) {
            return false
        } // Um AND nao simplificaria isso, na verdade daria erro pois estaria acessando uma posicao do tabuleiro que nao existe
        if ((tabuleiro[posicao.first - 1][posicao.second - 1] != null )) {
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
        for( coordenada in coordenadasNavio) {
            tabuleiro[coordenada.first - 1][coordenada.second - 1] = (dimensao + 48).toChar() //48 == 0 Na tabela ASCII
        }
        return true
    }
    return false
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, dimensao: Int) = insereNavio(tabuleiro, numLinhas, numColunas, "E", dimensao)


fun criaLegendaHorizontal(numColunas: Int) : String {
    var count = 0
    var legendaHorizontal = ""
    while (count < numColunas) {
        legendaHorizontal += (count + 65).toChar()
        if ((count != numColunas - 1)) { legendaHorizontal += " | " }
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
    var legendaHorizontal = criaLegendaHorizontal(numColunas)
    var legendaVertical = ""
    val mapa: Array<String> = Array(numLinhas + 1) { "" }
//    var countLinhas = 0
//    var countColunas = 0
    var valorEntreColuna: Char?
    var linha = ""
    var navioCompleto: Boolean
    for (countLinhas in 1.. numLinhas) {
        linha = ""
        for (countColunas in 1..numColunas) {
            if (tabuleiro[countLinhas - 1][countColunas - 1] == null) {
                valorEntreColuna = if (tabuleiroReal) {
                    '~'
                } else {
                    '?'
                }
            } else {
                if (tabuleiroReal) {
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
                            else -> null
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
//
//    legendaHorizontal = if (tamanhoTabuleiroValido(numLinhas, numColunas)) {
//        "| $legendaHorizontal |\n"
//    } else {
//        "\n| $legendaHorizontal |\n"
//    }
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
        if (podeRedirecionar(variavelDeRetorno)) { return -1 }
        if (variavelDeRetorno == null) { println("!!! ${mensagemErro}, tente novamente") }
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
    println("${'\n'}> > Batalha Naval < <${'\n'}")
    println("Defina o tamanho do tabuleiro:")
    while (!tamanhoTabuleiroValido) {
        linhas = pedirAtehAcertar("Quantas linhas?", "Número de linhas invalidas")
        if (podeRedirecionar(linhas)) { return REDIRECTED}
        colunas = pedirAtehAcertar("Quantas colunas?", "Número de colunas invalidas")
        if (podeRedirecionar(colunas)) { return REDIRECTED}
        if (!(tamanhoTabuleiroValido(linhas, colunas))) {
            println("Tamanho de tabuleiro Invalido, tente novamente")
            linhas = null
            colunas = null
        } else tamanhoTabuleiroValido = true
    }
    println()
//    println(criaTerreno(linhas!!, colunas!!))
    println("Insira as coordenadas do navio:")
    while (!coordenadaValida) {
        println("Coordenadas? (ex: 6,G)")
        coordenadas = readln()
        coordenadaValida = coordenadaValida(coordenadas, linhas!!, colunas!!)
        if (podeRedirecionar(coordenadas.toIntOrNull())) { return REDIRECTED }
        if (!(coordenadaValida)) { println("!!! Coordenadas invalidas, tente novamente") }
    }
    println("Insira a orientacao do navio:")
    while (!orientacaoValida) {
        println("Orientacao? (N, S, E, O)")
        orientacao = readln()
        orientacaoValida = veridicaOrientacao(orientacao)
        if (podeRedirecionar(orientacao.toIntOrNull())) { return REDIRECTED }
        if (!(orientacaoValida)) { println("!!! Orientacao invalida, tente novamente") }
    }
    return SUCCESS
}

fun printlnArray(arr: Array<Int?>) {
    for (index in arr) {
        print(index)
    }
    println()
}

fun main() {

    val arrayVazio = criaTabuleiroVazio(5,5)
//    insereNavio(arrayVazio, 1,4,"O",1)
//    insereNavio(arrayVazio, 5,2,"O",2)
//    insereNavio(arrayVazio, 4,3,"N",3)
//    insereNavioSimples(arrayVazio, 3, 1, 1)
    insereNavio(arrayVazio, 2,5,"S", 2)
    insereNavio(arrayVazio, 2,2,"S", 1)
    insereNavio(arrayVazio, 5,1,"S", 1)
    insereNavio(arrayVazio, 5,3,"E", 3)
    arrayVazio[4][4] = null
    arrayVazio[1][4] = null
    println(navioCompleto(arrayVazio, 5, 4))
    for (index in arrayVazio) {
        for (zinx in index) {
            print(zinx ?: '~')
            print(" ")
        }
        println()
    }


    for (linha in obtemMapa(arrayVazio, true)) {
        println(linha)
    }

//    var menuAtual: Int? = menuPrincipal()
//     while(true) {
//         menuAtual = readln().toIntOrNull()
//         menuAtual = when(menuAtual) {
//             1 -> menuDefinirTabuleiro()
//             2 -> ausenciaDeImplementacao()
//             3 -> ausenciaDeImplementacao()
//             4 -> ausenciaDeImplementacao()
//             0 -> return
//             else -> opcaoInvalida()
//         }
//         if (menuAtual == SUCCESS || menuAtual == REDIRECTED) { menuPrincipal() }
//     }
}
