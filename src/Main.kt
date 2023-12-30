import java.io.File

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


fun opcaoInvalida(): Int {
    println("!!! Opcao invalida, tente novamente")
    return 0
}

fun coordenadaValida(coordenadas: String, numLinhas: Int, numColunas: Int): Boolean {
    val abecedarioASCII = 65..90
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
    val arrayRetorno = Array(coordenadas.size - numCoordenadaOcupadas) { Pair(0, 0) }
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
    val arrayRetorno = Array(dimensao) { Pair(0, 0) }
    var count = 0
    if (!coordenadaContida(tabuleiro, numLinhas, numColunas)) {
        return emptyArray()
    }
    if (coordenadaContida(tabuleiro, numLinhas, numColunas)) {
        if (dimensao == 1) {
            arrayRetorno[count] = Pair(numLinhas, numColunas)
            return arrayRetorno
        }
        for (index in 0 until dimensao) {
            when (orientacao) {
                "E" -> {
                    if (coordenadaContida(tabuleiro, numLinhas, numColunas+index)) {
                        arrayRetorno[count] = Pair(numLinhas, numColunas+index)
                    } else {
                        return emptyArray()
                    }
                }
                "S" -> {
                    if (coordenadaContida(tabuleiro, numLinhas+index, numColunas)) {
                        arrayRetorno[count] = Pair(numLinhas+index, numColunas)
                    } else {
                        return emptyArray()
                    }
                }
                "O" -> {
                    if (coordenadaContida(tabuleiro, numLinhas, numColunas - index)) {
                        arrayRetorno[count] = Pair(numLinhas, numColunas - index)
                    } else {
                        return emptyArray()
                    }
                }
                "N" -> {
                    if (coordenadaContida(tabuleiro, numLinhas - index, numColunas)) {
                        arrayRetorno[count] = Pair(numLinhas - index, numColunas)
                    } else {
                        return emptyArray()
                    }
                }
            }
            count++
        }
    }
    return arrayRetorno
}

fun obterPosicoesCantos(tabuleiro: Array<Array<Char?>>,
                        orientacaoHorizontal: Boolean,
                        primeiraPosicaoNavio: Pair<Int, Int>,
                        ultimaPosicaoNavio: Pair<Int, Int>): Array<Pair<Int, Int>> {
    val arrPosicoesCantos = Array(4) { Pair(0, 0) }
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
    return arrPosicoesCantos
}

fun obterPosicoesExtremos(tabuleiro: Array<Array<Char?>>,
                          orientacaoHorizontal: Boolean,
                          primeiraPosicaoNavio: Pair<Int, Int>,
                          ultimaPosicaoNavio: Pair<Int, Int>): Array<Pair<Int, Int>> {
    val arrPosicoesExtremos = Array(2) { Pair(0, 0) }
    if (orientacaoHorizontal) {
        arrPosicoesExtremos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first, primeiraPosicaoNavio.second - 1))
        arrPosicoesExtremos[1] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first, ultimaPosicaoNavio.second + 1))
    } else {
        arrPosicoesExtremos[0] = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second))
        arrPosicoesExtremos[1] = retornaPosicaoSeExiste(tabuleiro, Pair(ultimaPosicaoNavio.first + 1, ultimaPosicaoNavio.second))
    }
    return arrPosicoesExtremos
}

fun obterPosicoesRedor(tabuleiro: Array<Array<Char?>>, orientacaoHorizontal: Boolean,
                       primeiraPosicaoNavio: Pair<Int, Int>, dimensao: Int): Array<Pair<Int, Int>> {
    val arrCoordenadasRedor = Array(dimensao * 2) { Pair(0, 0) }
    if (orientacaoHorizontal) {
        val casaAcimaDoPrimeiro = retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first - 1, primeiraPosicaoNavio.second))
        var count = 0
        if (coordenadaContida(tabuleiro, casaAcimaDoPrimeiro.first, casaAcimaDoPrimeiro.second)) {
            if (dimensao == 1) {
                arrCoordenadasRedor[count] = casaAcimaDoPrimeiro
                count++
            } else {
                for (index in 0..dimensao - 1) {
                    arrCoordenadasRedor[count] =
                            retornaPosicaoSeExiste(tabuleiro, Pair(casaAcimaDoPrimeiro.first, casaAcimaDoPrimeiro.second + index))
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
                    arrCoordenadasRedor[count] =
                            retornaPosicaoSeExiste(tabuleiro, Pair(casaAbaixoDoPrimeiro.first, casaAbaixoDoPrimeiro.second + index))
                    count++
                }
            }
        }
    } else {
        val casaEsquerdaDoPrimeiro =
                retornaPosicaoSeExiste(tabuleiro, Pair(primeiraPosicaoNavio.first, primeiraPosicaoNavio.second - 1))
        var count = 0
        if (coordenadaContida(tabuleiro, casaEsquerdaDoPrimeiro.first, casaEsquerdaDoPrimeiro.second)) {
            if (dimensao == 1) {
                arrCoordenadasRedor[count] = casaEsquerdaDoPrimeiro
                count++
            } else {
                for (index in 0..dimensao - 1) {
                    arrCoordenadasRedor[count] =
                            retornaPosicaoSeExiste(tabuleiro, Pair(casaEsquerdaDoPrimeiro.first + index, casaEsquerdaDoPrimeiro.second))
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
                    arrCoordenadasRedor[count] =
                            retornaPosicaoSeExiste(tabuleiro, Pair(casaDireitaDoPrimeiro.first + index, casaDireitaDoPrimeiro.second))
                    count++
                }
            }
        }
    }
    return arrCoordenadasRedor
} //Esta funcao podia ser mais refatorada, mas tive preguica

fun gerarCoordenadasFronteira(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int,
                              orientacao: String, dimensao: Int): Array<Pair<Int, Int>> {
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
    var primeiraPosicaoNavio = coordenadasNavio.first()
    var ultimaPosicaoNavio = coordenadasNavio.last()
    val arrPosicoesCantos: Array<Pair<Int, Int>>
    val arrPosicoesExtremos: Array<Pair<Int, Int>>
    val arrCoordenadasRedor: Array<Pair<Int, Int>>
    val orientacaoHorizontal: Boolean = primeiraPosicaoNavio.first == ultimaPosicaoNavio.first
    if (orientacao == "O" || orientacao == "N") {
        val temporario = primeiraPosicaoNavio
        primeiraPosicaoNavio = ultimaPosicaoNavio
        ultimaPosicaoNavio = temporario
    }
    arrPosicoesCantos = obterPosicoesCantos(tabuleiro, orientacaoHorizontal, primeiraPosicaoNavio, ultimaPosicaoNavio)
    arrPosicoesExtremos = obterPosicoesExtremos(tabuleiro, orientacaoHorizontal, primeiraPosicaoNavio, ultimaPosicaoNavio)
    arrCoordenadasRedor = obterPosicoesRedor(tabuleiro, orientacaoHorizontal, primeiraPosicaoNavio, dimensao)
    return limparCoordenadasVazias(juntarCoordenadas(juntarCoordenadas(arrPosicoesCantos, arrPosicoesExtremos), arrCoordenadasRedor))
}


fun obterConfiguracoesDisponiveisParaInserir(tabuleiro: Array<Array<Char?>>, dimensao: Int): Array<Triple<Int, Int, String>> {
    if (dimensao == 0) {
        return emptyArray()
    }
    var configuracoesDisponiveisParaInserir: Array<Triple<Int, Int, String>> = emptyArray()
    val orientacoes = arrayOf("N", "S", "E", "O")
    for (countLinha in 1..tabuleiro.size) {
        for (countColuna in 1..tabuleiro.size) {
            for (orientacao in orientacoes) {
                if (navioPodeSerInserido(tabuleiro, countLinha, countColuna, orientacao, dimensao)) {
                    configuracoesDisponiveisParaInserir += arrayOf(Triple(countLinha, countColuna, orientacao))
                }
            }
        }
    }
    return configuracoesDisponiveisParaInserir
}

fun preencheTabuleiroComputador(tabuleiro: Array<Array<Char?>>, confirguracaoNavio: Array<Int>) {
    var dimensao: Int
    for (index in (0 until confirguracaoNavio.size).reversed()) {
        dimensao = if (confirguracaoNavio[index] != 0) index + 1 else 0
        val configuracoesDisponivelParaInserir = obterConfiguracoesDisponiveisParaInserir(tabuleiro, dimensao)
        if (configuracoesDisponivelParaInserir.isNotEmpty()) {
            var configuracaoDisponivel = configuracoesDisponivelParaInserir.random()
            for (posicao in 0 until confirguracaoNavio[index]) {
                while (!(insereNavio(tabuleiro, configuracaoDisponivel.first, configuracaoDisponivel.second,
                                configuracaoDisponivel.third, dimensao))) {
                    configuracaoDisponivel = configuracoesDisponivelParaInserir.random()
                }
            }
        }
    }
}

fun obterCoordenadasDasLinhasEhColunas(tabuleiro: Array<Array<Char?>>,
                                       numLinhas: Int, numColunas: Int): Pair<Array<Pair<Int, Int>>, Array<Pair<Int, Int>>> {
    val arrayDasLinhas = Array<Pair<Int, Int>>(tabuleiro.size) { Pair(0, 0) }
    val arrayDasColunas = Array<Pair<Int, Int>>(tabuleiro.size) { Pair(0, 0) }
    for (countColuna in 1..tabuleiro.size) { //no array das linhas, quem muda sao as colunas
        arrayDasLinhas[countColuna - 1] = Pair(numLinhas, countColuna)
    }
    for (countLinha in 1..tabuleiro.size) {  //no array das colunas, quem muda sao as linhas
        arrayDasColunas[countLinha - 1] = Pair(countLinha, numColunas)
    }
    return Pair(arrayDasLinhas, arrayDasColunas)
}

fun valorTodasCoordenadasIguais(tabuleiro: Array<Array<Char?>>, coordenadas: Array<Pair<Int, Int>>, tipoDoNavio: Int): Boolean {
    for (coordenada in coordenadas) {
        if ("${tabuleiro[coordenada.first - 1][coordenada.second - 1]}".toIntOrNull() != tipoDoNavio) {
            return false
        }
    }
    return true
}

fun verificaNavioNasCoordenadas(tabuleiro: Array<Array<Char?>>, coordenadaPrincipal: Pair<Int, Int>,
                                coordenadas: Array<Pair<Int, Int>>, dimensao: Int, orientacao: String): Boolean {
    var coordenadasFatiado: Array<Pair<Int, Int>>
    for (coordenada in coordenadas) {
        coordenadasFatiado = gerarCoordenadasNavio(tabuleiro, coordenada.first, coordenada.second, orientacao, dimensao)
        if (valorTodasCoordenadasIguais(tabuleiro, coordenadasFatiado, dimensao) && coordenadaPrincipal in coordenadasFatiado) {
            return true
        }
    }
    return false
}

fun navioCompleto(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int): Boolean {
    val arrayDasLinhasEhColunas = obterCoordenadasDasLinhasEhColunas(tabuleiro, numLinhas, numColunas)
    val coordenadaPrincipal = Pair(numLinhas, numColunas)
    val tipoDoNavio = "${tabuleiro[numLinhas - 1][numColunas - 1]}".toIntOrNull()
    val arrayDasLinhas = arrayDasLinhasEhColunas.first
    val arrasDasColunas = arrayDasLinhasEhColunas.second
    if (tipoDoNavio != null) {
        return verificaNavioNasCoordenadas(tabuleiro, coordenadaPrincipal, arrayDasLinhas, tipoDoNavio, "E") ||
                verificaNavioNasCoordenadas(tabuleiro, coordenadaPrincipal, arrasDasColunas, tipoDoNavio, "S")
    }
    return false
}

fun orientacaoNavio(tabuleiro: Array<Array<Char?>>,
                    numLinhas: Int, numColunas: Int): String { //Essa funcao so funciona assumindo que e a primeira ou a ultima posicao do navio
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

fun lancarTiro(tabuleiroRealAdversario: Array<Array<Char?>>,
               tabuleiroPalpitesJogador: Array<Array<Char?>>, coordenada: Pair<Int, Int>): String {
    val valorTabuleiroReal = tabuleiroRealAdversario[coordenada.first - 1][coordenada.second - 1]
    val valorTiroAgua = "Agua." //So criei esta variavel porque o dropproject estava a reclamar
    val tipoNavio = when (valorTabuleiroReal) {
        '1' -> "submarino."
        '2' -> "contra-torpedeiro."
        '3' -> "navio-tanque."
        '4' -> "porta-avioes."
        null -> valorTiroAgua
        else -> "desconhecido."
    }
    if (tipoNavio == valorTiroAgua) {
        tabuleiroPalpitesJogador[coordenada.first - 1][coordenada.second - 1] = 'X'
        return tipoNavio
    } else {
        tabuleiroPalpitesJogador[coordenada.first - 1][coordenada.second - 1] = valorTabuleiroReal
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
    var valorPosicaoInt: Int?
    for (countLinha in 1..dimensaoTabuleiro) {
        for (countColuna in 1..dimensaoTabuleiro) {
            valorPosicao = tabuleiro[countLinha - 1][countColuna - 1] ?: '0'
            if (Pair(countLinha, countColuna) !in coordenadasAhIgnorar && valorPosicao != 'X') {
                valorPosicaoInt = "$valorPosicao".toIntOrNull()
                if (valorPosicaoInt == dimensao) { //navioCompleto(tabuleiro, countLinha, countColuna)
                    orientacaoNavio = orientacaoNavio(tabuleiro, countLinha, countColuna)
                    coordenadasDoNavio = gerarCoordenadasNavio(tabuleiro, countLinha, countColuna, orientacaoNavio, dimensao)
                    if (orientacaoNavio != "" && valorPosicaoInt != 1 && !estaLivre(tabuleiro, coordenadasDoNavio)) {
                        coordenadasAhIgnorar = juntarCoordenadas(coordenadasAhIgnorar, coordenadasDoNavio)
                        countNavios++
                    }
                    if (valorPosicaoInt == 1) {
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

fun navioPodeSerInserido(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String, dimensao: Int): Boolean {
    if (!coordenadaContida(tabuleiro, numLinhas, numColunas)) {
        return false
    }
    val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
    if (coordenadasNavio.isEmpty()) {
        return false
    }
    val coordenadasAhVolta = gerarCoordenadasFronteira(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
    val juncaoCoordenadas = juntarCoordenadas(coordenadasAhVolta, coordenadasNavio)
    return estaLivre(tabuleiro, juncaoCoordenadas)
}

fun insereNavio(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, orientacao: String, dimensao: Int): Boolean {
    if (navioPodeSerInserido(tabuleiro, numLinhas, numColunas, orientacao, dimensao)) {
        val coordenadasNavio = gerarCoordenadasNavio(tabuleiro, numLinhas, numColunas, orientacao, dimensao)
        for (coordenada in coordenadasNavio) {
            tabuleiro[coordenada.first - 1][coordenada.second - 1] = (dimensao + 48).toChar() //48 == 0 Na tabela ASCII
        }
        return true
    }
    return false
}

fun insereNavioSimples(tabuleiro: Array<Array<Char?>>, numLinhas: Int, numColunas: Int, dimensao: Int) =
        insereNavio(tabuleiro, numLinhas, numColunas, "E", dimensao)


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

fun podeRedirecionar(inputUtilizador: Int?) = inputUtilizador == -1

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


fun solicitarCoordenadas(dimensao: Int, tipoNavio: String): Pair<Int, Int>? {
    var coordenadaValida = false
    var coordenadas = ""
    val coordenadasProcessadas: Pair<Int, Int>? = null
    do {
        println("Insira as coordenadas de um ${tipoNavio}:\nCoordenadas? (ex: 6,G)")
        coordenadas = readln()
        coordenadaValida = coordenadaValida(coordenadas, dimensao, dimensao)
        if (podeRedirecionar(coordenadas.toIntOrNull())) {
            return null
        }
        if ((coordenadaValida)) {
            return processaCoordenadas(coordenadas, dimensao, dimensao)
        } else {
            println("!!! Coordenadas invalidas, tente novamente")
        }
    } while (!coordenadaValida)
    return coordenadasProcessadas
}

fun solicitarOrientacao(): String? {
    var orientacaoValida = false
    var orientacao = ""
    do {
        println("Insira a orientacao do navio:\nOrientacao? (N, S, E, O)")
        orientacao = readln()
        orientacaoValida = veridicaOrientacao(orientacao)
        if (podeRedirecionar(orientacao.toIntOrNull())) {
            return null
        }
        if (!(orientacaoValida)) {
            println("!!! Orientacao invalida, tente novamente")
        }
    } while (!orientacaoValida)
    return orientacao
}

fun iniciarTabuleiros(dimensao: Int) {
    tabuleiroHumano = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroPalpitesDoHumano = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroComputador = criaTabuleiroVazio(dimensao, dimensao)
    tabuleiroPalpitesDoComputador = criaTabuleiroVazio(dimensao, dimensao)
}

fun solicitarTabuleiro(): Int? {
    var tamanhoTabuleiroValido = false
    var linhas: Int?
    var colunas: Int?
    do {
        linhas = pedirAtehAcertar("Quantas linhas?", "Número de linhas invalidas")
        if (podeRedirecionar(linhas)) {
            return null
        }
        colunas = pedirAtehAcertar("Quantas colunas?", "Número de colunas invalidas")
        if (podeRedirecionar(colunas)) {
            return null
        }
        if (!(tamanhoTabuleiroValido(linhas, colunas))) {
            println("Tamanho de tabuleiro Invalido, tente novamente")
            linhas = null
            colunas = null
        } else tamanhoTabuleiroValido = true
    } while (!tamanhoTabuleiroValido)
    return linhas
}

fun menuDefinirTabuleiro(): Int {
    var orientacao: String? = null
    var naviosAhInserir = 0
    val arrayDeNomeDeNavio = arrayOf("submarino", "contra-torpedeiro", "navio-tanque", "porta-avioes")
    var navioInserido = false
    println("${'\n'}> > Batalha Naval < <${'\n'}${'\n'}Defina o tamanho do tabuleiro:")
    val dimensao = solicitarTabuleiro() ?: return 300
    iniciarTabuleiros(dimensao)
    val confirguracaoNavio: Array<Int> = calculaNumNavios(dimensao, dimensao)
    naviosAhInserir = calculaTotalNavioAhInserir(confirguracaoNavio)
    mostraMapa(tabuleiroHumano, true)
    var naviosAhInserirNaquelaPosicao = 0
    var count = 0
    while (naviosAhInserir > 0) {
        naviosAhInserirNaquelaPosicao = confirguracaoNavio[count]
        var coordenadasProcessadas: Pair<Int, Int>? = null
        while (naviosAhInserirNaquelaPosicao > 0) {
            coordenadasProcessadas = solicitarCoordenadas(dimensao, arrayDeNomeDeNavio[count])
            if (coordenadasProcessadas == null) {
                return 300
            }
            if (count >= 1) {
                orientacao = solicitarOrientacao()
                if (insereNavio(tabuleiroHumano, coordenadasProcessadas.first, coordenadasProcessadas.second, orientacao!!, count + 1)) {
                    navioInserido = true
                    naviosAhInserirNaquelaPosicao--
                    naviosAhInserir--
                }
            } else if (insereNavio(tabuleiroHumano, coordenadasProcessadas.first, coordenadasProcessadas.second, "N", count + 1)) {
                navioInserido = true
                naviosAhInserirNaquelaPosicao--
                naviosAhInserir--
            }
            if (navioInserido) {
                mostraMapa(tabuleiroHumano, true)
            } else {
                println("!!! Posicionamento invalido, tente novamente")
            }
            navioInserido = false
        }
        count++
    }
    preencheTabuleiroComputador(tabuleiroComputador, confirguracaoNavio)
    println("Pretende ver o mapa gerado para o Computador? (S/N)")
    if (readln() == "S") {
        mostraMapa(tabuleiroComputador, true)
    }
    return 200
}

fun mensagemAlvoAtingido(jogador: String, alvoAtingido: String,
                         tabuleiroPalpites: Array<Array<Char?>>, coordenadas: Pair<Int, Int>) {
    print(">>> ${jogador} >>>${alvoAtingido}")
    println(if (navioCompleto(tabuleiroPalpites, coordenadas.first, coordenadas.second)) " Navio ao fundo!" else "")
}

fun menuJogar(tabuleiroDefinido: Boolean): Int {
    if (!tabuleiroDefinido) {
        printErroTabuleiroNaoDefinido()
        return 300
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
            println("Indique a posição que pretende atingir")
            println("Coordenadas? (ex: 6,G)")
            coordenadas = readln()
            coordenadaValida = coordenadaValida(coordenadas, dimensaoTabuleiro, dimensaoTabuleiro)
            if (podeRedirecionar(coordenadas.toIntOrNull())) {
                return 300
            }
            if ((coordenadaValida)) {
                coordenadasProcessadas = processaCoordenadas(coordenadas, dimensaoTabuleiro, dimensaoTabuleiro)
            } else {
                println("!!! Coordenadas invalidas, tente novamente")
            }
        }
        alvoAtingido = lancarTiro(tabuleiroComputador, tabuleiroPalpitesDoHumano, coordenadasProcessadas!!)
        mensagemAlvoAtingido("HUMANO", alvoAtingido, tabuleiroPalpitesDoHumano, coordenadasProcessadas)
        if (venceu(tabuleiroPalpitesDoHumano)) {
            println("PARABENS! Venceu o jogo!")
            println("Prima enter para voltar ao menu principal")
            readln()
            return 200
        }
        alvoAtingidoComputador = geraTiroComputador(tabuleiroPalpitesDoComputador)
        println("Computador lancou tiro para a posicao (${alvoAtingidoComputador.first}, ${alvoAtingidoComputador.second})")
        alvoAtingido = lancarTiro(tabuleiroHumano, tabuleiroPalpitesDoComputador, alvoAtingidoComputador)
        mensagemAlvoAtingido("COMPUTADOR", alvoAtingido, tabuleiroPalpitesDoHumano, coordenadasProcessadas)
        if (venceu(tabuleiroPalpitesDoComputador)) {
            println("PARABENS! Venceu o jogo")
            println("Prima enter para voltar ao menu principal")
            readln()
            return 200
        }
        println("Prima enter para continuar")
        readln()
    }
}

fun menuGravarJogo(tabuleiroDefinido: Boolean): Int {
    if (!tabuleiroDefinido) {
        printErroTabuleiroNaoDefinido()
        return 300
    }
    println("Introduza o nome do ficheiro (ex: jogo.txt)")
    val nomeDoFicheiro = readln()
    val dimensao = tabuleiroHumano.size
    gravarJogo(nomeDoFicheiro, tabuleiroHumano, tabuleiroPalpitesDoHumano, tabuleiroComputador, tabuleiroPalpitesDoComputador)
    println("Tabuleiro ${dimensao}x${dimensao} gravado com sucesso")
    return 200
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
    return 200
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

fun gravarJogo(nomeDoFicheiro: String, tabuleiroRealHumano: Array<Array<Char?>>, tabuleiroPalpitesHumano: Array<Array<Char?>>,
               tabuleiroRealComputador: Array<Array<Char?>>, tabuleiroPalpitesComputador: Array<Array<Char?>>) {
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

fun tabuleiroDefinido() = tabuleiroHumano.isNotEmpty() && tabuleiroComputador.isNotEmpty()

fun main() {
    var menuAtual: Int? = menuPrincipal()
    while (true) {
        menuAtual = readln().toIntOrNull()
        menuAtual = when (menuAtual) {
            1 -> menuDefinirTabuleiro()
            2 -> menuJogar(tabuleiroDefinido())
            3 -> menuGravarJogo(tabuleiroDefinido())
            4 -> menuLerJogo()
            0 -> return
            else -> opcaoInvalida()
        }
        if (menuAtual == 200 || menuAtual == 300) {
            menuPrincipal()
        }
    }
}
