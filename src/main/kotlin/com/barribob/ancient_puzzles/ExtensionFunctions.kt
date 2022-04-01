package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.cardinal_components.ModComponents
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleType
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardEvent
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardType
import net.minecraft.world.chunk.Chunk

fun <T: PuzzleManager> Chunk.getPuzzle(puzzleType: PuzzleType<T>) : T {
    return ModComponents.puzzleManagerComponentKey.get(this).getPuzzleManager(puzzleType.type) as T
}

fun <T: PuzzleManager, E : RewardEvent> Chunk.getRewardForPuzzle(puzzleType: PuzzleType<T>, rewardType: RewardType<E>) : E {
    return ModComponents.puzzleManagerComponentKey.get(this).getRewardForPuzzle(puzzleType.type, rewardType.type) as E
}