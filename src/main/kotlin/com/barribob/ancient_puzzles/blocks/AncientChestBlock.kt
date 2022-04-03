package com.barribob.ancient_puzzles.blocks

import net.minecraft.block.BlockState
import net.minecraft.block.ChestBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.util.math.BlockPos
import java.util.function.Supplier

class AncientChestBlock(settings: Settings?, supplier: Supplier<BlockEntityType<out ChestBlockEntity>>?) : ChestBlock(settings, supplier) {
    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return AncientChestBlockEntity(pos, state)
    }
}