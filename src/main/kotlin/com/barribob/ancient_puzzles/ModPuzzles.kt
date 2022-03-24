package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.puzzle_manager.PressAllBlocksPuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManagerFactory

class ModPuzzles {
    val puzzleManagerFactory = PuzzleManagerFactory()
    val pressAllBlocks = "press_all_blocks"

    fun init() {
        puzzleManagerFactory.register(pressAllBlocks, ::PressAllBlocksPuzzleManager, ::PressAllBlocksPuzzleManager)
    }
}