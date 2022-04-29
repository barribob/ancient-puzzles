package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.cardinal_components.ModComponents
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleType
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardEvent
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardType
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import net.minecraft.world.chunk.Chunk
import java.util.*

fun <T : PuzzleManager> Chunk.getPuzzle(puzzleType: PuzzleType<T>): T {
    return ModComponents.puzzleManagerComponentKey.get(this).getPuzzleManager(puzzleType.type) as T
}

fun <T : PuzzleManager, E : RewardEvent> Chunk.getRewardForPuzzle(puzzleType: PuzzleType<T>, rewardType: RewardType<E>): E {
    return ModComponents.puzzleManagerComponentKey.get(this).getRewardForPuzzle(puzzleType.type, rewardType.type) as E
}

fun BlockPos.toNbt(): NbtCompound {
    val blockPositionNbt = NbtCompound()
    blockPositionNbt.putInt("x", x)
    blockPositionNbt.putInt("y", y)
    blockPositionNbt.putInt("z", z)
    return blockPositionNbt
}

fun readBlockPos(compound: NbtCompound): BlockPos = BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"))
fun NbtCompound.getBlockPos(key: String): BlockPos = readBlockPos(this.getCompound(key))
fun NbtCompound.putBlockPos(key: String, blockPos: BlockPos) {
    this.put(key, blockPos.toNbt())
}

fun Random.randomPitch() = (this.nextFloat() - this.nextFloat()) * 0.2f + 1.0f
