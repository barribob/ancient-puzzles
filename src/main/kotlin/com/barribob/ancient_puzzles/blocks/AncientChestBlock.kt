package com.barribob.ancient_puzzles.blocks

import com.barribob.ancient_puzzles.Mod
import net.minecraft.block.BlockState
import net.minecraft.block.ChestBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.block.entity.ChestBlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.sound.SoundCategory
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import java.util.function.Supplier

class AncientChestBlock(settings: Settings?, supplier: Supplier<BlockEntityType<out ChestBlockEntity>>?) : ChestBlock(settings, supplier) {
    override fun createBlockEntity(pos: BlockPos?, state: BlockState?): BlockEntity {
        return AncientChestBlockEntity(pos, state)
    }

    override fun onUse(state: BlockState?, world: World, pos: BlockPos?, player: PlayerEntity, hand: Hand?, hit: BlockHitResult?): ActionResult {
        return if (world.isClient) {
            ActionResult.SUCCESS
        } else {
            player.playSound(Mod.sounds.stoneBrickChestLocked, SoundCategory.BLOCKS, 1.0f, 1.0f)
            ActionResult.CONSUME
        }
    }
}