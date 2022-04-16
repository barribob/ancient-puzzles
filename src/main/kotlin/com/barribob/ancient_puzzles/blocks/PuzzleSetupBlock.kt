package com.barribob.ancient_puzzles.blocks

import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.entity.BlockEntity
import net.minecraft.util.math.BlockPos

class PuzzleSetupBlock(settings: Settings?) : BlockWithEntity(settings) {
    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = PuzzleSetupBlockEntity(pos, state)
}