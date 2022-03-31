package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.puzzle_manager.PressAllBlocksPuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManagerFactory
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleType

class ModPuzzles {
    val puzzleManagerFactory = PuzzleManagerFactory()
    val pressAllBlocks = PuzzleType<PressAllBlocksPuzzleManager>("press_all_blocks")

    fun init() {
        puzzleManagerFactory.register(pressAllBlocks, ::PressAllBlocksPuzzleManager, ::PressAllBlocksPuzzleManager)
    }
}