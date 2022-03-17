package com.barribob.ancient_puzzles.structure

import com.barribob.ancient_puzzles.Mod
import com.mojang.serialization.Codec
import net.minecraft.structure.*
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.StructureFeature

class TestStructureFeature(
    codec: Codec<DefaultFeatureConfig>, structurePieceType: StructurePieceType
) :
    StructureFeature<DefaultFeatureConfig>(codec,
        StructureGeneratorFactory.simple(
            StructureGeneratorFactory.checkForBiomeOnTop(Heightmap.Type.WORLD_SURFACE_WG)
        ) { collector, context -> addPieces(collector, context, structurePieceType) }) {

    companion object {
        private val template: Identifier = Mod.identifier("test")
        fun addPieces(collector : StructurePiecesCollector, context : StructurePiecesGenerator.Context<DefaultFeatureConfig>, structurePieceType: StructurePieceType) {
            val x = context.chunkPos().startX
            val z = context.chunkPos().startZ
            val y = 90
            val blockPos = BlockPos(x, y, z)
            val rotation = BlockRotation.random(context.random)
            collector.addPiece(SingleTemplateStructurePiece(context.structureManager, blockPos, template, rotation, structurePieceType))
        }

    }
}