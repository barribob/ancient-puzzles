package com.barribob.ancient_puzzles.blocks

import com.barribob.ancient_puzzles.Mod
import net.minecraft.block.BlockState
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.BlockPos

class AncientChestBlockEntity(pos: BlockPos?, state: BlockState?) : ChestBlockEntity(Mod.blocks.ancientChestBlockEntityType, pos, state) {

    fun copyToEntity(entity: ChestBlockEntity) {
        entity.setLootTable(lootTableId, lootTableSeed)
    }

    override fun checkLootInteraction(player: PlayerEntity?) {
    }
}