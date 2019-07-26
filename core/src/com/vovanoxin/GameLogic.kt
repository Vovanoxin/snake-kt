package com.vovanoxin

import java.util.*
import kotlin.random.Random
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input


class Snake(var initCell: Cell) {
    var direction: Direction = Direction.RIGHT

    var bodyParts: LinkedList<Cell> = LinkedList<Cell>()
    var head: Cell = initCell

    init {
        bodyParts.add(initCell)
    }

    fun grow(food: Cell) {
        bodyParts.add(food)
        head = food


    }

    fun move(nextCell: Cell) {

        bodyParts.removeLast()

        head = nextCell

        bodyParts.addFirst(head)

    }

}


data class Cell(val row: Int, val col: Int, var type: CellType = CellType.Empty)

enum class CellType {
    Empty, Body, Food, Head
}

enum class Direction {
    UP, RIGHT, DOWN, LEFT
}

class GameField(val ROWS: Int = 100, val COL: Int = 100) {
    val grid = createGrid(ROWS, COL)
    private val initCell = grid[ROWS / 2][COL / 2]
    private var food: Cell = grid[Random.nextInt(0, ROWS)][Random.nextInt(0, COL)]
    private val snake = Snake(initCell)
    private var gameOver = false


    private fun generateFood() {
        while (true) {
            food = grid[Random.nextInt(0, ROWS)][Random.nextInt(0, COL)]
            if (food in snake.bodyParts) {
                continue
            } else break
        }

    }

    private fun calculateNextCell(): Cell {

        var row: Int = snake.head.row
        var col: Int = snake.head.col

        if (col+1 >= 100 || row+1 >= 100||col-1 < 0||row-1 < 0) {
            return Cell(-1, -1)
        }

        return when (snake.direction) {
            Direction.UP -> grid[row][++col]
            Direction.DOWN -> grid[row][--col]
            Direction.LEFT -> grid[--row][col]
            Direction.RIGHT -> grid[++row][col]

        }
    }

    fun update() {
        if (!gameOver) {
            val nextCell = calculateNextCell()
            if (snake.bodyParts.contains(nextCell) || nextCell.col == -1) {
                gameOver = true
            } else {
                snake.move(nextCell)


                if (snake.head == food) {
                    snake.grow(food)
                    generateFood()

                }

                updateGrid()
            }


        }
    }

    private fun updateGrid() {
        grid.forEach { it.forEach { it.type = CellType.Empty } }
        snake.bodyParts.forEach { grid[it.row][it.col].type = CellType.Body }
        snake.head.type = CellType.Head
        food.type = CellType.Food
    }

    fun queryInput() {
        val aPressed = Gdx.input.isKeyPressed(Input.Keys.A)
        val dPressed = Gdx.input.isKeyPressed(Input.Keys.D)
        val wPressed = Gdx.input.isKeyPressed(Input.Keys.W)
        val sPressed = Gdx.input.isKeyPressed(Input.Keys.S)

        //I'm not sure if delay is needed here

        if (aPressed && (snake.direction != Direction.RIGHT)) snake.direction = Direction.LEFT
        Thread.sleep(1)
        if (dPressed && (snake.direction != Direction.LEFT)) snake.direction = Direction.RIGHT
        Thread.sleep(1)
        if (wPressed && (snake.direction != Direction.DOWN)) snake.direction = Direction.UP
        Thread.sleep(1)
        if (sPressed && (snake.direction != Direction.UP)) snake.direction = Direction.DOWN
    }


}

fun createGrid(ROWS: Int, COL: Int): Array<Array<Cell>> {
    return Array(ROWS) {
        val row = it
        Array(COL) {
            Cell(row, it)
        }
    }
}