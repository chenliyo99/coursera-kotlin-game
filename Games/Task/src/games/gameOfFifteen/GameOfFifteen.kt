package games.gameOfFifteen

import board.Cell
import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {

    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        val allCells = board.getAllCells()
        initializer.initialPermutation.forEachIndexed { index, i ->
            board[allCells.elementAt(index)] = i
        }
    }

    override fun processMove(direction: Direction) {
        val nullCell = board.getAllCells().first { board[it] == null }
        when (direction) {
            Direction.DOWN -> board.getCellOrNull(nullCell.i-1, nullCell.j)?.let { swap(it, nullCell) }
            Direction.UP -> board.getCellOrNull(nullCell.i+1, nullCell.j)?.let { swap(it, nullCell) }
            Direction.LEFT -> board.getCellOrNull(nullCell.i, nullCell.j+1)?.let { swap(it, nullCell) }
            Direction.RIGHT -> board.getCellOrNull(nullCell.i, nullCell.j-1)?.let { swap(it, nullCell) }
        }
    }

    private fun swap(cell1: Cell, cell2: Cell) {
        val tmp = board[cell1]
        board[cell1] = board[cell2]
        board[cell2] = tmp
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean {
        val allCells = board.getAllCells().filter { board[it] != null }.toList()
        for(i in 1..allCells.size-1)
            if(board[allCells[i]]!! < board[allCells[i-1]]!!) return false
        return true
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }
}