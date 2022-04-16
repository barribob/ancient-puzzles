package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.blocks.*
import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.block.Block
import net.minecraft.block.MapColor
import net.minecraft.block.Material
import net.minecraft.block.PressurePlateBlock
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.item.BlockItem
import net.minecraft.state.property.Properties
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ModBlocks {
    val stoneBrickPuzzleLight = PuzzleLight(FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(-1.0f, 3600000f).luminance { if(it.get(Properties.LIT) != false) 8 else 0 }.dropsNothing())
    val solvedStoneBrickPuzzleLight = Block(FabricBlockSettings.of(Material.STONE, MapColor.GRAY).requiresTool().strength(2.0f, 6.0f).luminance(8).dropsNothing())
    private val deepslatePressurePlate = TimedPressurePlate(PressurePlateBlock.ActivationRule.MOBS, FabricBlockSettings.of(Material.STONE).requiresTool().noCollision().strength(3.0f))
    val stoneBrickChest = AncientChestBlock(FabricBlockSettings.of(Material.STONE).strength(-1f, 3600000f).dropsNothing()) { BlockEntityType.CHEST }
    val stoneBrickChestBlockEntityType: BlockEntityType<AncientChestBlockEntity> = FabricBlockEntityTypeBuilder.create(::AncientChestBlockEntity, stoneBrickChest).build()
    val puzzleSetupBlock = PuzzleSetupBlock(FabricBlockSettings.of(Material.STONE).strength(-1f, 3600000f).dropsNothing())
    val puzzleSetupBlockEntityType: BlockEntityType<PuzzleSetupBlockEntity> = FabricBlockEntityTypeBuilder.create(::PuzzleSetupBlockEntity, puzzleSetupBlock).build()

    fun init() {
        registerBlockAndItem(Mod.identifier("stone_brick_puzzle_light"), stoneBrickPuzzleLight, FabricItemSettings().group(Mod.itemGroup))
        registerBlockAndItem(Mod.identifier("deepslate_pressure_plate"), deepslatePressurePlate, FabricItemSettings().group(Mod.itemGroup))
        registerBlockAndItem(Mod.identifier("stone_brick_chest"), stoneBrickChest, FabricItemSettings().group(Mod.itemGroup))
        registerBlockAndItem(Mod.identifier("solved_stone_brick_puzzle_light"), solvedStoneBrickPuzzleLight, FabricItemSettings().group(Mod.itemGroup))
        registerBlockAndItem(Mod.identifier("puzzle_setup"), puzzleSetupBlock, FabricItemSettings().group(Mod.itemGroup))


        Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("stone_brick_chest"), stoneBrickChestBlockEntityType)
        Registry.register(Registry.BLOCK_ENTITY_TYPE, Mod.identifier("puzzle_setup"), puzzleSetupBlockEntityType)
    }

    private fun registerBlockAndItem(identifier: Identifier, block: Block, fabricItemSettings: FabricItemSettings = FabricItemSettings()) {
        Registry.register(Registry.BLOCK, identifier, block)
        Registry.register(Registry.ITEM, identifier, BlockItem(block, fabricItemSettings))
    }
}