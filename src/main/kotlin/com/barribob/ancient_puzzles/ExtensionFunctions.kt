package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.cardinal_components.ModComponents
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleType
import net.minecraft.world.chunk.Chunk

fun <T: PuzzleManager> Chunk.getPuzzle(puzzleType: PuzzleType<T>) : T {
    return ModComponents.puzzleManagerComponentKey.get(this).getPuzzleManager(puzzleType.type) as T
}