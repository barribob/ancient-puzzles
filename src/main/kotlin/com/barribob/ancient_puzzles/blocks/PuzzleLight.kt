package com.barribob.ancient_puzzles.blocks

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.RedstoneOreBlock
import net.minecraft.item.ItemPlacementContext
import net.minecraft.particle.DustParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.StateManager
import net.minecraft.state.property.BooleanProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import java.util.*

class PuzzleLight(settings: Settings?) : Block(settings) {
    companion object {
        val lit: BooleanProperty = Properties.LIT
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

    private fun spawnParticles(world: World, pos: BlockPos) {
        val d = 0.5625
        val random = world.random
        for (direction in Direction.values()) {
            val blockPos = pos.offset(direction)
            if (world.getBlockState(blockPos).isOpaqueFullCube(world, blockPos)) continue
            val axis = direction.axis
            val e = if (axis === Direction.Axis.X) 0.5 + d * direction.offsetX.toDouble() else random.nextFloat().toDouble()
            val f = if (axis === Direction.Axis.Y) 0.5 + d * direction.offsetY.toDouble() else random.nextFloat().toDouble()
            val g = if (axis === Direction.Axis.Z) 0.5 + d * direction.offsetZ.toDouble() else random.nextFloat().toDouble()
            world.addParticle(DustParticleEffect.DEFAULT, pos.x.toDouble() + e, pos.y.toDouble() + f, pos.z.toDouble() + g, 0.0, 0.0, 0.0)
        }
    }


    override fun randomDisplayTick(state: BlockState, world: World, pos: BlockPos, random: Random?) {
        if (state.get(RedstoneOreBlock.LIT)) {
            spawnParticles(world, pos)
        }
    }
}