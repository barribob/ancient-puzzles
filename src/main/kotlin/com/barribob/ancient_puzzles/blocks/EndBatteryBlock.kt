package com.barribob.ancient_puzzles.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.FacingBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.math.Direction

class EndBatteryBlock(settings: Settings?) : FacingBlock(settings) {
    init {
        this.defaultState = this.stateManager.defaultState.with(FACING, Direction.UP)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val direction = ctx.side
        val blockState = ctx.world.getBlockState(ctx.blockPos.offset(direction.opposite))
        return if (blockState.isOf(this) && blockState.get(FACING) == direction) {
            defaultState.with(FACING, direction.opposite) as BlockState
        } else defaultState.with(FACING, direction) as BlockState
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING)
    }
}