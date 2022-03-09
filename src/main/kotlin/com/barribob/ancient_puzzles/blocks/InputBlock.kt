package com.barribob.ancient_puzzles.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.item.ItemPlacementContext
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.*

class InputBlock(settings: Settings?) : Block(settings) {
    companion object {
        private val lit = Properties.LIT
    }

    init {
        defaultState = defaultState.with(lit, false)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState = defaultState.with(lit, ctx.world.isReceivingRedstonePower(ctx.blockPos))

    override fun neighborUpdate(state: BlockState, world: World, pos: BlockPos?, block: Block?, fromPos: BlockPos?, notify: Boolean) {
        if (world.isClient) {
            return
        }
        val bl = state.get(lit)
        if (bl != world.isReceivingRedstonePower(pos)) {
            if (bl) {
                world.createAndScheduleBlockTick(pos, this, 4)
            } else {
                world.setBlockState(pos, state.cycle(lit) as BlockState, NOTIFY_LISTENERS)
            }
        }
    }

    override fun scheduledTick(state: BlockState, world: ServerWorld, pos: BlockPos?, random: Random?) {
        if (state.get(lit) && !world.isReceivingRedstonePower(pos)) {
            world.setBlockState(pos, state.cycle(lit) as BlockState, NOTIFY_LISTENERS)
        }
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(lit)
    }
}