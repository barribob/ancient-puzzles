package com.barribob.ancient_puzzles.blocks

import com.barribob.ancient_puzzles.Mod
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.RodBlock
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class EndDiverterBlock(settings: Settings?) : RodBlock(settings) {
    init {
        defaultState = (stateManager.defaultState as BlockState).with(FACING, Direction.UP).with(Mod.properties.endPowered, false) as BlockState
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        val direction = ctx.side
        val blockState = ctx.world.getBlockState(ctx.blockPos.offset(direction.opposite))
        return if (blockState.isOf(this) && blockState.get(FACING) == direction) {
            defaultState.with(FACING, direction.opposite) as BlockState
        } else defaultState.with(FACING, direction) as BlockState
    }

    override fun appendProperties(builder: StateManager.Builder<Block?, BlockState?>) {
        builder.add(FACING).add(Mod.properties.endPowered)
    }

    override fun onUse(state: BlockState, world: World, pos: BlockPos, player: PlayerEntity, hand: Hand?, hit: BlockHitResult?): ActionResult {
        if (!world.isClient) {
            changeDirection(state, world, pos)
        }
        return ActionResult.SUCCESS
    }

    private fun changeDirection(state: BlockState, world: World, pos: BlockPos) {
        world.setBlockState(pos, state.with(FACING, Direction.byId((state.get(FACING).id + 1) % 6)))
    }
}