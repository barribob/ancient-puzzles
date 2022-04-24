package com.barribob.ancient_puzzles.structure

import com.barribob.ancient_puzzles.structure.generator.FallbackAtSizeProvider
import com.barribob.ancient_puzzles.structure.generator.StructurePoolTerminatorGenerator
import net.minecraft.structure.*
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig
import java.util.*

class TerminatorSurfaceStructureFeature : StructureFeature<StructurePoolFeatureConfig>(StructurePoolFeatureConfig.CODEC, ::generatePieces, PostPlacementProcessor.EMPTY) {
    companion object {
        fun generatePieces(context: StructureGeneratorFactory.Context<StructurePoolFeatureConfig>): Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> {
            val blockpos = context.chunkPos.getCenterAtY(0)

            val structurePiecesGenerator = StructurePoolTerminatorGenerator.generate(
                context,
                { structureManager: StructureManager?, poolElement: StructurePoolElement?, pos: BlockPos?, groundLevelDelta: Int, rotation: BlockRotation?, boundingBox: BlockBox? ->
                    PoolStructurePiece(
                        structureManager,
                        poolElement,
                        pos,
                        groundLevelDelta,
                        rotation,
                        boundingBox
                    )
                },
                blockpos,
                bl = false,
                bl2 = true,
                FallbackAtSizeProvider(context)
            )
            return structurePiecesGenerator
        }
    }
}