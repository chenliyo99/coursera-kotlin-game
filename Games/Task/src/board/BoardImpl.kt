package board

import board.Direction.*
import java.lang.IllegalArgumentException

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)

open class SquareBoardImpl(final override val width:Int):SquareBoard{

    private val cells:MutableList<Cell> = mutableListOf<Cell>()
    init {
        if(width<=0) throw IllegalArgumentException("width cannot be less than 1,is ${width}")
        var count = 0
        for(i in 1..width)
            for(j in 1..width)
                cells.add(count++,Cell(i,j))
    }


    override fun getCellOrNull(i: Int, j: Int): Cell? =cells.filter { it.i == i }.filter { it.j == j }.firstOrNull()

    override fun getAllCells(): Collection<Cell> =cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val result:MutableList<Cell> = mutableListOf<Cell>()
        var count = 0
        for(j in jRange) {
            if(!bValid(i,j)) continue;
            result.add(count++, getCell(i, j))
        }
        return result
    }

    private fun bValid(i:Int,j:Int):Boolean = (i>=1&&i<=width&&j>=1&&j<=width)

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val result:MutableList<Cell> = mutableListOf<Cell>()
        var count = 0
        for(i in iRange) {
            if(!bValid(i,j)) continue;
            result.add(count++, getCell(i, j))
        }
        return result
    }

    override fun getCell(i: Int, j: Int): Cell = getCellOrNull(i,j)?:
    throw IllegalArgumentException("cannot find cell(i:${i},j:${j})")

    override fun Cell.getNeighbour(direction: Direction): Cell? = when(direction){
        UP->getCellOrNull(i-1,j)
        DOWN->getCellOrNull(i+1,j)
        LEFT->getCellOrNull(i,j-1)
        RIGHT->getCellOrNull(i,j+1)
    }
}


fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

class GameBoardImpl<T>(width: Int):SquareBoardImpl(width),GameBoard<T>{

    val store: MutableMap<Cell, T?> = hashMapOf()

    init {
        getAllCells().forEach{set(it,null)}
    }

    override fun get(cell: Cell): T? {
        return store.get(cell)
    }

    override fun set(cell: Cell, value: T?) {
        store[cell] = value
    }

    override fun all(predicate: (T?) -> Boolean): Boolean =filter(predicate).size == store.size

    override fun any(predicate: (T?) -> Boolean): Boolean = filter(predicate).size != 0

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = store.filter { predicate(it.value) }.keys

    override fun find(predicate: (T?) -> Boolean): Cell? = filter(predicate).firstOrNull()


}

