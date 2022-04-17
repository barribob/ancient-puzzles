package com.barribob.ancient_puzzles.puzzle_manager

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.readBlockPos
import com.barribob.ancient_puzzles.toNbt
import net.minecraft.block.Block
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.state.property.Properties.LIT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PillarCombinationPuzzleManager() : PuzzleManager {
    private var blockPositions: MutableMap<Int, BlockPos> = mutableMapOf()
    private var lockStates: MutableMap<Int, Boolean> = mutableMapOf()

    constructor(nbtCompound: NbtCompound) : this() {
        blockPositions = loadPositions(nbtCompound).toMutableMap()
        lockStates = loadLocks(nbtCompound).toMutableMap()
    }

    override fun isSolved(world: World): Boolean {
        val allBlocksLit = combinationCorrect(world)
        if (allBlocksLit) {
            blockPositions.forEach { replacePuzzleWithBreakableBlock(world, it.value) }
        }
        return allBlocksLit
    }

    private fun replacePuzzleWithBreakableBlock(world: World, pos: BlockPos) {
        if (world.getBlockState(pos).isOf(Mod.blocks.stoneBrickPuzzleLight)) {
            world.setBlockState(pos, Mod.blocks.solvedStoneBrickPuzzleLight.defaultState, Block.NOTIFY_LISTENERS)
        }
    }

    override fun toNbt(): NbtCompound {
        return savePuzzle(blockPositions, lockStates)
    }

    private fun combinationCorrect(world: World) = lockStates.any() && blockPositions.keys.intersect(lockStates.keys).all { lockValid(world, it) }

    private fun lockValid(world: World, lockId: Int): Boolean {
        val optional = world.getBlockState(blockPositions[lockId]).getOrEmpty(LIT)
        if(optional.isEmpty) return true

        return optional.get() == lockStates[lockId]
    }

    fun addPosition(lockId: Int, pos: BlockPos) {
        blockPositions[lockId] = pos
    }

    fun addLockState(lockId: Int, state: Boolean) {
        lockStates[lockId] = state
    }

    companion object {
        fun loadPositions(nbtCompound: NbtCompound): Map<Int, BlockPos> {
            return nbtCompound.getList(
                "positions",
                NbtCompound().type.toInt()
            ).filterIsInstance<NbtCompound>().associate(::loadPosition)
        }

        private fun loadPosition(nbtCompound: NbtCompound): Pair<Int, BlockPos> {
            val id = nbtCompound.getInt("id")
            val pos = readBlockPos(nbtCompound.getCompound("pos"))
            return Pair(id, pos)
        }

        fun loadLocks(nbtCompound: NbtCompound): Map<Int, Boolean> {
            return nbtCompound.getList(
                "locks",
                NbtCompound().type.toInt()
            ).filterIsInstance<NbtCompound>().associate(::loadLock)
        }

        private fun loadLock(nbtCompound: NbtCompound): Pair<Int, Boolean> {
            val id = nbtCompound.getInt("id")
            val lock = nbtCompound.getBoolean("lock")
            return Pair(id, lock)
        }

        fun savePuzzle(blockPositions: Map<Int, BlockPos>, lockStates: Map<Int, Boolean>): NbtCompound {
            val compound = NbtCompound()
            val positionsNbt = NbtList()
            for (puzzleState in blockPositions) {
                val positionCompound = NbtCompound()
                positionCompound.put("pos", puzzleState.value.toNbt())
                positionCompound.putInt("id", puzzleState.key)
                positionsNbt.add(positionCompound)
            }
            compound.put("positions", positionsNbt)

            val locksNbt = NbtList()
            for (lock in lockStates) {
                val lockCompound = NbtCompound()
                lockCompound.putBoolean("lock", lock.value)
                lockCompound.putInt("id", lock.key)
                locksNbt.add(lockCompound)
            }
            compound.put("locks", locksNbt)

            return compound
        }
    }
}
