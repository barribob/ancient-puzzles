package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.cardinal_components.ModComponents
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import net.minecraft.world.chunk.Chunk

fun Chunk.addPuzzle(puzzleManager: PuzzleManager) {
    ModComponents.puzzleManagerComponentKey.get(this).addPuzzleManager(puzzleManager)
}