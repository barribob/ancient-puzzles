package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.state.property.Properties.LIT
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class PressAllBlocksPuzzleManager(private val blockView: World, private val blockPositions: List<BlockPos>) : PuzzleManager {

    constructor(chunk: World, nbtCompound: NbtCompound) : this(chunk,
        loadBlockPositions(nbtCompound)
    )

    override fun tick() {
        if (allBlocksLit()) {
            println("You get a prize!")
        }
    }

    override fun shouldRemove(): Boolean {
        return allBlocksLit()
    }

    override fun toNbt(): NbtCompound {
        return saveBlockPositions(blockPositions)
    }

    private fun allBlocksLit() = blockPositions.all { blockView.getBlockState(it).getOrEmpty(LIT).orElse(false) }

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
