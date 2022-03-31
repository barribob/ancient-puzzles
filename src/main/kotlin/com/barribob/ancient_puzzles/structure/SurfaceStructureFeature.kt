package com.barribob.ancient_puzzles.structure

import net.minecraft.structure.*
import net.minecraft.structure.pool.StructurePoolBasedGenerator
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig
import java.util.*

class SurfaceStructureFeature : StructureFeature<StructurePoolFeatureConfig>(StructurePoolFeatureConfig.CODEC, ::generatePieces, PostPlacementProcessor.EMPTY) {
    companion object {
        fun generatePieces(context: StructureGeneratorFactory.Context<StructurePoolFeatureConfig>): Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> {
            var blockpos = context.chunkPos().getCenterAtY(0)

            val topLandY = context.chunkGenerator().getHeightOnGround(blockpos.x, blockpos.z, Heightmap.Type.WORLD_SURFACE_WG, context.world())
            blockpos = blockpos.up(topLandY)

            val structurePiecesGenerator = StructurePoolBasedGenerator.generate(
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
                },  // Needed in order to create a list of jigsaw pieces when making the structure's layout.
                blockpos,  // Position of the structure. Y value is ignored if last parameter is set to true.
                false,
                false // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
            )
            return structurePiecesGenerator
        }
    }
}