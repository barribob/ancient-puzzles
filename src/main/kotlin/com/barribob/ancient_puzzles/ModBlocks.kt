package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.blocks.AncientChestBlock
import com.barribob.ancient_puzzles.blocks.AncientChestBlockEntity
import com.barribob.ancient_puzzles.blocks.InputBlock
import com.barribob.ancient_puzzles.blocks.TimedPressurePlate
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.PressurePlateBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ModBlocks {
    val inputBlock = InputBlock(FabricBlockSettings.of(Material.STONE, MapColor.BLACK).requiresTool().strength(-1.0f, 3600000f).dropsNothing())
    private val ancientPressurePlate = TimedPressurePlate(PressurePlateBlock.ActivationRule.MOBS, FabricBlockSettings.of(Material.STONE).requiresTool().noCollision().strength(3.0f))
    val ancientChest = AncientChestBlock(FabricBlockSettings.of(Material.STONE).strength(-1f, 3600000f).dropsNothing()) { BlockEntityType.CHEST }
    val ancientChestBlockEntityType: BlockEntityType<AncientChestBlockEntity> = FabricBlockEntityTypeBuilder.create(::AncientChestBlockEntity, ancientChest).build()

    fun init() {
        registerBlockAndItem(Mod.identifier("input_block"), inputBlock, FabricItemSettings().group(Mod.itemGroup))
        registerBlockAndItem(Mod.identifier("ancient_pressure_plate"), ancientPressurePlate, FabricItemSettings().group(Mod.itemGroup))
        registerBlockAndItem(Mod.identifier("ancient_chest"), ancientChest, FabricItemSettings().group(Mod.itemGroup))

        Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("ancient_chest"), ancientChestBlockEntityType)
    }

    private fun registerBlockAndItem(identifier: Identifier, block: Block, fabricItemSettings: FabricItemSettings = FabricItemSettings()) {
        Registry.register(Registry.BLOCK, identifier, block)
        Registry.register(Registry.ITEM, identifier, BlockItem(block, fabricItemSettings))
    }
}