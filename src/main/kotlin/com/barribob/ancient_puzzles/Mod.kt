package com.barribob.ancient_puzzles

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.util.Identifier

object Mod {
    private const val modId = "ancient_puzzles"
    val blocks = ModBlocks()
    val itemGroup: ItemGroup = FabricItemGroupBuilder.build(identifier("items")) { ItemStack(Items.GLOW_ITEM_FRAME) }
    fun identifier(path: String) = Identifier(modId, path)
}