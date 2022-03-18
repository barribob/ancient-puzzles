package com.barribob.ancient_puzzles.structure

import net.barribob.maelstrom.MaelstromMod
import net.minecraft.structure.*
import net.minecraft.structure.pool.StructurePoolBasedGenerator
import net.minecraft.structure.pool.StructurePoolElement
import net.minecraft.util.BlockRotation
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.world.Heightmap
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig
import org.apache.logging.log4j.Level
import java.util.*

class TestStructureFeature : StructureFeature<StructurePoolFeatureConfig>(StructurePoolFeatureConfig.CODEC, ::generatePieces, PostPlacementProcessor.EMPTY) {
    companion object {
        fun generatePieces(context: StructureGeneratorFactory.Context<StructurePoolFeatureConfig>): Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> {

            // Turns the chunk coordinates into actual coordinates we can use. (Gets center of that chunk)
            var blockpos = context.chunkPos().getCenterAtY(0)

            // Find the top Y value of the land and then offset our structure to 60 blocks above that.
            // WORLD_SURFACE_WG will stop at top water so we don't accidentally put our structure into the ocean if it is a super deep ocean.

            // Find the top Y value of the land and then offset our structure to 60 blocks above that.
            // WORLD_SURFACE_WG will stop at top water so we don't accidentally put our structure into the ocean if it is a super deep ocean.
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
                false,  // special boundary adjustments for villages. It's... hard to explain. Keep this false and make your pieces not be partially intersecting.
                // Either not intersecting or fully contained will make children pieces spawn just fine. It's easier that way.
                false // Place at heightmap (top land). Set this to false for structure to be place at the passed in blockpos's Y value instead.
                // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
            )

            /*
         * Note, you are always free to make your own JigsawPlacement class and implementation of how the structure
         * should generate. It is tricky but extremely powerful if you are doing something that vanilla's jigsaw system cannot do.
         * Such as for example, forcing 3 pieces to always spawn every time, limiting how often a piece spawns, or remove the intersection limitation of pieces.
         *
         * An example of a custom JigsawPlacement.addPieces in action can be found here (warning, it is using Mojmap mappings):
         * https://github.com/TelepathicGrunt/RepurposedStructures/blob/1.18.2/src/main/java/com/telepathicgrunt/repurposedstructures/world/structures/pieces/PieceLimitedJigsawManager.java
         */


            /*
         * Note, you are always free to make your own JigsawPlacement class and implementation of how the structure
         * should generate. It is tricky but extremely powerful if you are doing something that vanilla's jigsaw system cannot do.
         * Such as for example, forcing 3 pieces to always spawn every time, limiting how often a piece spawns, or remove the intersection limitation of pieces.
         *
         * An example of a custom JigsawPlacement.addPieces in action can be found here (warning, it is using Mojmap mappings):
         * https://github.com/TelepathicGrunt/RepurposedStructures/blob/1.18.2/src/main/java/com/telepathicgrunt/repurposedstructures/world/structures/pieces/PieceLimitedJigsawManager.java
         */if (structurePiecesGenerator.isPresent) {
                // I use to debug and quickly find out if the structure is spawning or not and where it is.
                // This is returning the coordinates of the center starting piece.
                MaelstromMod.LOGGER.info("Rundown House at $blockpos")
            }

            // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.

            // Return the pieces generator that is now set up so that the game runs it when it needs to create the layout of structure pieces.
            return structurePiecesGenerator
        }
    }
}