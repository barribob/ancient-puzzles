package com.barribob.ancient_puzzles.puzzle_manager

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getBlockPos
import com.barribob.ancient_puzzles.putBlockPos
import net.minecraft.nbt.NbtCompound
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World

class DirectionalLightPuzzleManager() : PuzzleManager {
    private var finalPosition: BlockPos = BlockPos.ORIGIN
    private var batteryPosition: BlockPos = BlockPos.ORIGIN

    constructor(nbtCompound: NbtCompound) : this() {
        finalPosition = nbtCompound.getBlockPos("final_position")
        batteryPosition = nbtCompound.getBlockPos("battery_pos")
    }

    override fun isSolved(world: World): Boolean {
        var currentPos = batteryPosition

        for(i in 0..30) {
            currentPos = getNextPos(currentPos, world)
            if (currentPos == finalPosition) return true
            if (currentPos == BlockPos.ORIGIN) break
            if (!world.getBlockState(currentPos).isOf(Mod.blocks.endDiverter)) break
            world.setBlockState(currentPos, world.getBlockState(currentPos).with(Mod.properties.endPowered, true))
        }

        return false
    }

    private fun getNextPos(currentPos: BlockPos, world: World): BlockPos {
        val state = world.getBlockState(currentPos)
        if (!state.isOf(Mod.blocks.endBattery) && !state.isOf(Mod.blocks.endDiverter)) return BlockPos.ORIGIN
        val direction = state.get(Properties.FACING)
        return currentPos.add(direction.vector.multiply(3))
    }

    override fun toNbt(): NbtCompound {
        return serialize()
    }

    fun setFinalPosition(pos: BlockPos) {
        finalPosition = pos
    }

    fun setBatteryPosition(pos: BlockPos) {
        batteryPosition = pos
    }

    private fun serialize(): NbtCompound {
        val nbtCompound = NbtCompound()
        nbtCompound.putBlockPos("final_position", finalPosition)
        nbtCompound.putBlockPos("battery_pos", batteryPosition)
        return nbtCompound
    }
}