package com.barribob.ancient_puzzles.puzzle_manager

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardEvent
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardTracker
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardType
import net.barribob.maelstrom.MaelstromMod
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties.LIT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class PressAllBlocksPuzzleManager() : PuzzleManager {
    private var blockPositions: MutableList<BlockPos> = mutableListOf()
    private val rewardTracker = RewardTracker(Mod.rewards.rewardFactory)

    constructor(nbtCompound: NbtCompound) : this() {
        blockPositions = loadBlockPositions(nbtCompound).toMutableList()
        loadReward(nbtCompound, rewardTracker)
    }

    fun visualizePuzzle(world: ServerWorld) {
        if (blockPositions.isNotEmpty()) {
            val points = blockPositions.flatMap { (0..20).map { i -> Vec3d(it.x.toDouble(), (it.y + i * 0.5), it.z.toDouble()) } }
            MaelstromMod.debugPoints.drawDebugPoints(points, 60, points.first(), world)
        }
    }

    override fun tick(world: World) {
        if (allBlocksLit(world)) {
            rewardTracker.doReward(world)
        }
    }

    override fun shouldRemove(world: World): Boolean {
        return allBlocksLit(world)
    }

    override fun toNbt(): NbtCompound {
        val blockPositionsNbt = saveBlockPositions(blockPositions)
        saveReward(blockPositionsNbt, rewardTracker)
        return blockPositionsNbt
    }

    private fun allBlocksLit(world: World) = blockPositions.all { world.getBlockState(it).getOrEmpty(LIT).orElse(false) }
    fun addPosition(pos: BlockPos) {
        blockPositions.add(pos)
    }

    fun <T : RewardEvent> setReward(type: RewardType<T>, rewardEvent: T) {
        rewardTracker.setReward(type, rewardEvent)
    }

    companion object {
        private const val rewardNbtKey = "reward"
        fun saveReward(blockPositionsNbt: NbtCompound, rewardTracker: RewardTracker) {
            val rewardNbt = rewardTracker.toNbt()
            blockPositionsNbt.put(rewardNbtKey, rewardNbt)
        }

        fun loadReward(nbtCompound: NbtCompound, rewardTracker: RewardTracker) {
            rewardTracker.loadNbt(nbtCompound.getCompound(rewardNbtKey))
        }

        fun loadBlockPositions(nbtCompound: NbtCompound) = nbtCompound.getList(
            "block_positions",
            NbtCompound().type.toInt()
        ).map {
            val compound = it as NbtCompound
            BlockPos(compound.getInt("x"), compound.getInt("y"), compound.getInt("z"))
        }

        fun saveBlockPositions(blockPositions: List<BlockPos>): NbtCompound {
            val compound = NbtCompound()
            val blockPositionsNbt = NbtList()
            for (pos in blockPositions) {
                val blockPositionNbt = NbtCompound()
                blockPositionNbt.putInt("x", pos.x)
                blockPositionNbt.putInt("y", pos.y)
                blockPositionNbt.putInt("z", pos.z)
                blockPositionsNbt.add(blockPositionNbt)
            }
            compound.put("block_positions", blockPositionsNbt)
            return compound
        }
    }
}
