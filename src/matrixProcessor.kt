package processor

import java.util.*
import kotlin.math.pow

fun main() {
    val scanner = Scanner(System.`in`)
    var exit = false
    do {
        println("1. Add matrices\n" +
                "2. Multiply matrix to a constant\n" +
                "3. Multiply matrices\n" +
                "4. Transpose matrix\n" +
                "5. Calculate a determinant\n" +
                "6. Inverse matrix\n" +
                "0. Exit\n" +
                "Your choice:")
        when (scanner.nextLine()) {
            "1" -> addMatrices()
            "2" -> {
                val matrix = addMatrix("")
                println("Enter constant")
                val const = scanner.nextLine().toDouble()
                printMatrix(multiplyConst(matrix, const))
            }
            "3" -> multiplyMatrices()
            "4" -> {
                println("1. Main diagonal\n2. Side diagonal\n3. Vertical line\n4. Horizontal line\nYour choice: ")
                val type = scanner.nextLine()
                if (type !in "1".."4") println("Unknown command") else {
                    printMatrix(transMatrix(addMatrix(""), type))
                }
            }
            "5" -> {
                val matrix = addMatrix("")
                println (if (matrix.size != matrix[0].size) "Incorrect. The matrix should be square"
                else {
                    "The result is: \n${determinant(matrix)}"
                })
            }
            "6" -> {
                val matrix = addMatrix("")
                if (matrix.size != matrix[0].size) println("Incorrect. The matrix should be square")
                else {
                    val res = inverse(matrix)
                    println("The result is:")
                    for (i in res) println(i.map { (it * 100.0).toInt() / 100.0 }.joinToString(" "))
                }
            }
            "0" -> exit = true
            else -> println("Unknown command")
        }
    } while (!exit)
}

fun inverse(matrix: Array<Array<Double>>): Array<Array<Double>> {
    val det = determinant(matrix)
    var coMatrix = Array(matrix.size) {Array(matrix[0].size) { 0.0 }}
    for (i in matrix.indices) {
        for (j in matrix[0].indices) {
            coMatrix[i][j] = (-1.0).pow(2.0 + j + i) * determinant(matrix.filterIndexed { index, _ ->  index != i }.map { it.filterIndexed { index, _ ->  index != j }.toTypedArray() }.toTypedArray())
        }
    }
    coMatrix = transMatrix(coMatrix, "1")
    return multiplyConst(coMatrix, 1 / det)
}

fun determinant(matrix: Array<Array<Double>>):Double {
    var x = 0.0
    if (matrix.size == 2) return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0])
    if (matrix.size != 2) {
        for (j in matrix[0].indices) {
            x += (-1.0).pow(2.0 + j).toInt() * matrix[0][j] * determinant(matrix.filterIndexed { index, _ ->  index != 0 }.map { it.filterIndexed { index, _ ->  index != j }.toTypedArray() }.toTypedArray())
        }
    }
    return x
}

fun addMatrix(word: String): Array<Array<Double>> {
    val scanner = Scanner(System.`in`)
    println("Enter size of $word matrix: ")
    val row = scanner.next().toInt()
    val colum = scanner.next().toInt()
    println("Enter $word matrix:")
    return Array(row) {Array(colum) { scanner.next().toDouble() }}
}

fun printMatrix(matrix: Array<Array<Double>>) {
    for (i in matrix) println(i.joinToString(" "))
}

fun transMatrix(matrix: Array<Array<Double>>, type: String): Array<Array<Double>> {
    val res = Array(matrix[0].size) {Array(matrix.size) { 0.0 }}
    val res1 = Array(matrix.size) {Array(matrix[0].size) { 0.0 }}
    for (i in res.indices) {
        for (j in res[0].indices){
            when (type) {
                "1" -> res[i][j] = matrix[j][i]
                "2" -> res[i][j] = matrix[matrix.size - 1 - j][matrix[0].size - 1 - i]
                "3" -> res1[j][i] = matrix[j][res.size - 1 - i]
                "4" -> res1[j][i] = matrix[res[0].size - 1 - j][i]
            }
        }
    }
    return (if (type in "1".."2") res else res1)
}

fun multiplyConst(input: Array<Array<Double>>, const: Double): Array<Array<Double>> {
    val res = Array(input.size) { Array(input[0].size) { 0.0 } }
    for (i in input.indices) {
        for (j in input[i].indices) {
            res[i][j] = input[i][j] * const
        }
    }
    return res
}

fun addMatrices() {
    val matrixOne = addMatrix("first")
    val matrixTwo = addMatrix("second")
    val res = Array(matrixOne.size) {Array(matrixOne[0].size) { 0.0 }}
    if (matrixOne.size != matrixTwo.size || matrixOne[0].size != matrixTwo[0].size) {
        println("ERROR")
    } else {
        for (i in matrixOne.indices) {
            for (j in matrixOne[i].indices) {
                res[i][j] = matrixOne[i][j] + matrixTwo[i][j]
            }
        }
        printMatrix(res)
    }
}

fun multiplyMatrices() {
    val matrixOne = addMatrix("first")
    val matrixTwo = addMatrix("second")
    val res = Array(matrixOne.size) {Array(matrixTwo[0].size) { 0.0 }}
    if (matrixOne[0].size != matrixTwo.size) {
        println("ERROR")
    } else {
        for (row1 in matrixOne.indices) {
            for (col2 in matrixTwo[0].indices) {
                for (col1 in matrixOne[0].indices) {
                    res[row1][col2] += matrixOne[row1][col1] * matrixTwo[col1][col2]
                }
            }
        }
        printMatrix(res)
    }
}
