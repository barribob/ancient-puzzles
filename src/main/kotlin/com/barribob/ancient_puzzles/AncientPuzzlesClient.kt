package com.barribob.ancient_puzzles

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry
import net.minecraft.client.render.block.entity.ChestBlockEntityRenderer

@Suppress("UNUSED")
class AncientPuzzlesClient : ClientModInitializer{
    override fun onInitializeClient() {
        BlockEntityRendererRegistry.register(Mod.blocks.stoneBrickChestBlockEntityType, ::ChestBlockEntityRenderer)
    }
}