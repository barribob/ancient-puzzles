package com.barribob.ancient_puzzles.features

import com.mojang.serialization.Codec
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.structure.RuinedPortalStructurePiece
import net.minecraft.structure.RuinedPortalStructurePiece.VerticalPlacement
import net.minecraft.tag.BlockTags
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldAccess
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.Feature
import net.minecraft.world.gen.feature.util.FeatureContext
import java.util.*
import kotlin.math.abs

class RuinedGroundFeature(configCodec: Codec<DefaultFeatureConfig>?) : Feature<DefaultFeatureConfig>(configCodec) {
    override fun generate(context: FeatureContext<DefaultFeatureConfig>): Boolean {
        placeRuinsBase(BlockBox(context.origin).expand(10).offset(0, 10, 0), context.random, context.world)
        return true
    }

    // From RuinedPortalStructurePiece
    private fun placeRuinsBase(boundingBox: BlockBox, random: Random, world: WorldAccess) {
        val blockPos: BlockPos = boundingBox.center
        val x = blockPos.x
        val z = blockPos.z
        val fs = floatArrayOf(0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.4f, 0.4f, 0.3f, 0.2f, 0.1f, 0.1f, 0.1f)
        val k = fs.size
        val l: Int = (boundingBox.blockCountX + boundingBox.blockCountZ) / 2
        val randomFactor = random.nextInt(1.coerceAtLeast(8 - l / 2))
        val mutable = BlockPos.ORIGIN.mutableCopy()
        for (X in x - k..x + k) {
            for (Z in z - k..z + k) {
                val distance = abs(X - x) + abs(Z - z)
                val randomDistanceFactor = 0.coerceAtLeast(distance + randomFactor)
                if (randomDistanceFactor >= k) continue
                val f = fs[randomDistanceFactor]
                if (random.nextDouble() >= f.toDouble()) continue
                val s = getBaseHeight(world, X, Z)
                mutable.set(X, s, Z)
                if (abs(s - boundingBox.minY) > 3 || !this.canReplace(world, mutable)) continue
                this.setBlock(world, mutable)
            }
        }
    }

    private fun getBaseHeight(world: WorldAccess, x: Int, y: Int): Int {
        return world.getTopY(RuinedPortalStructurePiece.getHeightmapType(VerticalPlacement.ON_LAND_SURFACE), x, y) - 1
    }

    private fun canReplace(world: WorldAccess, pos: BlockPos): Boolean {
        val blockState = world.getBlockState(pos)
        return !blockState.isOf(Blocks.AIR) && !blockState.isOf(Blocks.OBSIDIAN) && !blockState.isIn(BlockTags.FEATURES_CANNOT_REPLACE) && (!blockState.isOf(Blocks.LAVA) && blockState.isSolidBlock(world, pos))
    }

    private fun setBlock(world: WorldAccess, pos: BlockPos) {
        val blocks = listOf(Blocks.COBBLESTONE, Blocks.GRAVEL, Blocks.CRACKED_STONE_BRICKS)
        world.setBlockState(pos, blocks[world.random.nextInt(blocks.size)].defaultState, Block.NOTIFY_ALL)
    }
}