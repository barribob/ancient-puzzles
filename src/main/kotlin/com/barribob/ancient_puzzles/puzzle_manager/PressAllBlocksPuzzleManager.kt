package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.state.property.Properties.LIT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PressAllBlocksPuzzleManager(private var blockPositions : MutableList<BlockPos> = mutableListOf()) : PuzzleManager {

    constructor(nbtCompound: NbtCompound) : this() {
       blockPositions = loadBlockPositions(nbtCompound).toMutableList()
    }



    override fun tick(world: World) {
        if (allBlocksLit(world)) {
            println("You get a prize!")
        }
    }

    override fun shouldRemove(world: World): Boolean {
        return allBlocksLit(world)
    }

    override fun toNbt(): NbtCompound {
        return saveBlockPositions(blockPositions)
    }

    private fun allBlocksLit(world: World) = blockPositions.all { world.getBlockState(it).getOrEmpty(LIT).orElse(false) }
    fun addPosition(pos: BlockPos) {
        blockPositions.add(pos)
    }

    companion object {
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
