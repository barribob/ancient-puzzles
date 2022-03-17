package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.structure.SingleTemplateStructurePiece
import com.barribob.ancient_puzzles.structure.StructureRegister
import com.barribob.ancient_puzzles.structure.TestStructureFeature
import net.minecraft.structure.StructurePieceType
import net.minecraft.tag.TagKey
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement
import net.minecraft.world.gen.chunk.placement.SpreadType
import net.minecraft.world.gen.feature.DefaultFeatureConfig

class ModStructures {
    fun init() {
        val identifier = Mod.identifier("press_all_blocks")
        val pressAllBlocksPuzzleStructureRegistry = StructureRegister(identifier)
        val pressAllBlocksStructurePieceType = StructurePieceType { context, nbt -> SingleTemplateStructurePiece(context.structureManager, nbt, Registry.STRUCTURE_PIECE.get(identifier)!!) }
        Registry.register(Registry.STRUCTURE_PIECE, identifier, pressAllBlocksStructurePieceType)
        val biomeTag = TagKey.of(Registry.BIOME_KEY, Mod.identifier("has_structure/press_all_blocks"))
        val structureFeature = TestStructureFeature(DefaultFeatureConfig.CODEC, pressAllBlocksStructurePieceType)
        val structurePlacement = RandomSpreadStructurePlacement(24, 8, SpreadType.LINEAR, 1385496)
        val configuredStructureFeature = structureFeature.configure(DefaultFeatureConfig.INSTANCE, biomeTag)

        pressAllBlocksPuzzleStructureRegistry.register(structureFeature, configuredStructureFeature, structurePlacement)
    }
}