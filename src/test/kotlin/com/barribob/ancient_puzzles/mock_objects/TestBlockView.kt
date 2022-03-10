package com.barribob.ancient_puzzles.mock_objects

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.fluid.FluidState
import net.minecraft.util.math.BlockPos
import net.minecraft.world.BlockView

class TestBlockView : BlockView {
    override fun getHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun getBottomY(): Int {
        TODO("Not yet implemented")
    }

    override fun getBlockEntity(pos: BlockPos?): BlockEntity? {
        TODO("Not yet implemented")
    }

    override fun getBlockState(pos: BlockPos?): BlockState {
        TODO("Not yet implemented")
    }

    override fun getFluidState(pos: BlockPos?): FluidState {
        TODO("Not yet implemented")
    }
}