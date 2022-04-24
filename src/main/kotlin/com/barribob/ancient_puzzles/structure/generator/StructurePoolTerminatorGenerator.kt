package com.barribob.ancient_puzzles.structure.generator

import com.google.common.collect.Lists
import com.google.common.collect.Queues
import com.mojang.logging.LogUtils
import net.minecraft.block.JigsawBlock
import net.minecraft.structure.*
import net.minecraft.structure.Structure.StructureBlockInfo
import net.minecraft.structure.pool.*
import net.minecraft.util.BlockRotation
import net.minecraft.util.Identifier
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.math.BlockBox
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Box
import net.minecraft.util.registry.Registry
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.HeightLimitView
import net.minecraft.world.Heightmap
import net.minecraft.world.biome.source.BiomeCoords
import net.minecraft.world.gen.chunk.ChunkGenerator
import net.minecraft.world.gen.feature.StructureFeature
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig
import net.minecraft.world.gen.random.AtomicSimpleRandom
import net.minecraft.world.gen.random.ChunkRandom
import org.apache.commons.lang3.mutable.MutableObject
import org.slf4j.Logger
import java.util.*
import java.util.function.Consumer
import kotlin.math.max

object StructurePoolTerminatorGenerator {
    val LOGGER: Logger = LogUtils.getLogger()
    fun generate(context2: StructureGeneratorFactory.Context<StructurePoolFeatureConfig>, pieceFactory: StructurePoolBasedGenerator.PieceFactory, pos: BlockPos, bl: Boolean, bl2: Boolean, poolProvider: PoolProvider): Optional<StructurePiecesGenerator<StructurePoolFeatureConfig>> {
        val chunkRandom = ChunkRandom(AtomicSimpleRandom(0L))
        chunkRandom.setCarverSeed(context2.seed(), context2.chunkPos().x, context2.chunkPos().z)
        val dynamicRegistryManager = context2.registryManager()
        val structurePoolFeatureConfig = context2.config()
        val chunkGenerator = context2.chunkGenerator()
        val structureManager = context2.structureManager()
        val heightLimitView = context2.world()
        val predicate = context2.validBiome()
        StructureFeature.init()
        val registry = dynamicRegistryManager.get(Registry.STRUCTURE_POOL_KEY)
        val blockRotation = BlockRotation.random(chunkRandom)
        val structurePool = structurePoolFeatureConfig.startPool.value()
        val structurePoolElement = structurePool.getRandomElement(chunkRandom)
        if (structurePoolElement === EmptyPoolElement.INSTANCE) {
            return Optional.empty()
        }
        val poolStructurePiece = pieceFactory.create(structureManager, structurePoolElement, pos, structurePoolElement.groundLevelDelta, blockRotation, structurePoolElement.getBoundingBox(structureManager, pos, blockRotation))
        val blockBox = poolStructurePiece.boundingBox
        val i = (blockBox.maxX + blockBox.minX) / 2
        val j = (blockBox.maxZ + blockBox.minZ) / 2
        val k = if (bl2) pos.y + chunkGenerator.getHeightOnGround(i, j, Heightmap.Type.WORLD_SURFACE_WG, heightLimitView) else pos.y
        if (!predicate.test(chunkGenerator.getBiomeForNoiseGen(BiomeCoords.fromBlock(i), BiomeCoords.fromBlock(k), BiomeCoords.fromBlock(j)))) {
            return Optional.empty()
        }
        val l = blockBox.minY + poolStructurePiece.groundLevelDelta
        poolStructurePiece.translate(0, k - l, 0)
        return Optional.of(StructurePiecesGenerator { structurePiecesCollector: StructurePiecesCollector, _: StructurePiecesGenerator.Context<StructurePoolFeatureConfig>? ->
            val list = Lists.newArrayList<PoolStructurePiece>()
            list.add(poolStructurePiece)
            if (structurePoolFeatureConfig.size <= 0) {
                return@StructurePiecesGenerator
            }
            val box = Box((i - 80).toDouble(), (k - 80).toDouble(), (j - 80).toDouble(), (i + 80 + 1).toDouble(), (k + 80 + 1).toDouble(), (j + 80 + 1).toDouble())
            val generator = StructurePoolTerminatorGenerator(registry, structurePoolFeatureConfig.size, pieceFactory, chunkGenerator, structureManager, list, chunkRandom, poolProvider)

            generator.structurePieces.addLast(
                ShapedPoolStructurePiece(
                    poolStructurePiece,
                    MutableObject(VoxelShapes.combineAndSimplify(VoxelShapes.cuboid(box), VoxelShapes.cuboid(Box.from(blockBox)), BooleanBiFunction.ONLY_FIRST)),
                    0
                )
            )
            while (!generator.structurePieces.isEmpty()) {
                val shapedPoolStructurePiece = generator.structurePieces.removeFirst()
                generator.generatePiece(shapedPoolStructurePiece.piece, shapedPoolStructurePiece.pieceShape, shapedPoolStructurePiece.currentSize, bl, heightLimitView)
            }
            list.forEach(Consumer { piece: PoolStructurePiece? -> structurePiecesCollector.addPiece(piece) })
        })
    }

    internal class StructurePoolTerminatorGenerator(
        private val registry: Registry<StructurePool?>,
        private val maxSize: Int,
        private val pieceFactory: StructurePoolBasedGenerator.PieceFactory,
        private val chunkGenerator: ChunkGenerator,
        private val structureManager: StructureManager?,
        private val children: MutableList<in PoolStructurePiece>,
        private val random: Random?,
        private val poolProvider: PoolProvider
    ) {
        val structurePieces: Deque<ShapedPoolStructurePiece> = Queues.newArrayDeque()
        fun generatePiece(piece: PoolStructurePiece, pieceShape: MutableObject<VoxelShape?>, currentSize: Int, modifyBoundingBox: Boolean, world: HeightLimitView?) {
            val structurePoolElement = piece.poolElement
            val blockPos = piece.pos
            val blockRotation = piece.rotation
            val projection = structurePoolElement.projection
            val bl = projection == StructurePool.Projection.RIGID
            val mutableObject = MutableObject<VoxelShape?>()
            val blockBox = piece.boundingBox
            val i = blockBox.minY
            block0@ for (structureBlockInfo2 in structurePoolElement.getStructureBlockInfos(structureManager, blockPos, blockRotation, random)) {
                generateStructureBlock(structureBlockInfo2, i, blockBox, mutableObject, pieceShape, currentSize, modifyBoundingBox, bl, world, piece, projection)
            }
        }

        private fun generateStructureBlock(
            structureBlockInfo2: StructureBlockInfo,
            i: Int,
            blockBox: BlockBox,
            mutableObject: MutableObject<VoxelShape?>,
            pieceShape: MutableObject<VoxelShape?>,
            currentSize: Int,
            modifyBoundingBox: Boolean,
            bl: Boolean,
            world: HeightLimitView?,
            piece: PoolStructurePiece,
            projection: StructurePool.Projection?
        ) {
            val mutableObject2: MutableObject<VoxelShape?>
            val direction = JigsawBlock.getFacing(structureBlockInfo2.state)
            val blockPos2 = structureBlockInfo2.pos
            val blockPos3 = blockPos2.offset(direction)
            val j = blockPos2.y - i
            var k = -1
            val (poolIdentifier, optional) = poolProvider.getPool(PoolProviderData(structureBlockInfo2, currentSize))
            if (!optional.isPresent || optional.get().elementCount == 0 && poolIdentifier != StructurePools.EMPTY.value) {
                LOGGER.warn("Empty or non-existent pool: {}", poolIdentifier as Any)
                return
            }
            val fallbackIdentifier = optional.get().terminatorsId
            val optional2 = registry.getOrEmpty(fallbackIdentifier)
            if (!optional2.isPresent || optional2.get().elementCount == 0 && fallbackIdentifier != StructurePools.EMPTY.value) {
                LOGGER.warn("Empty or non-existent fallback pool: {}", fallbackIdentifier as Any)
                return
            }
            val bl2 = blockBox.contains(blockPos3)
            if (bl2) {
                mutableObject2 = mutableObject
                if (mutableObject.value == null) {
                    mutableObject.value = VoxelShapes.cuboid(Box.from(blockBox))
                }
            } else {
                mutableObject2 = pieceShape
            }
            val list = Lists.newArrayList<StructurePoolElement>()
            if (currentSize != maxSize) {
                list.addAll(optional.get().getElementIndicesInRandomOrder(random))
            }
            list.addAll(optional2.get().getElementIndicesInRandomOrder(random))
            val iterator: Iterator<StructurePoolElement> = list.iterator()
            for (structurePoolElement2 in iterator) {
                for (blockRotation2 in BlockRotation.randomRotationOrder(random)) {
                    val list2 = structurePoolElement2.getStructureBlockInfos(structureManager, BlockPos.ORIGIN, blockRotation2, random)
                    val blockBox2 = structurePoolElement2.getBoundingBox(structureManager, BlockPos.ORIGIN, blockRotation2)
                    val l = if (!modifyBoundingBox || blockBox2.blockCountY > 16) 0 else list2.stream().mapToInt { structureBlockInfo: StructureBlockInfo ->
                        if (!blockBox2.contains(structureBlockInfo.pos.offset(JigsawBlock.getFacing(structureBlockInfo.state)))) {
                            return@mapToInt 0
                        }
                        val poolId = Identifier(structureBlockInfo.nbt.getString("pool"))
                        println("Inner getting pool $poolId")
                        val structurePool = registry.getOrEmpty(poolId)
                        val fallback: Optional<StructurePool> = structurePool.flatMap { pool: StructurePool? -> registry.getOrEmpty(pool!!.terminatorsId) }
                        val poolY = structurePool.map { pool: StructurePool? -> pool!!.getHighestY(structureManager) }.orElse(0)
                        val fallbackY: Int = fallback.map { pool: StructurePool? -> pool!!.getHighestY(structureManager) }.orElse(0)
                        max(poolY, fallbackY)
                    }.max().orElse(0)
                    for (structureBlockInfo22 in list2) {
                        val t: Int
                        var r: Int
                        var p: Int
                        if (!JigsawBlock.attachmentMatches(structureBlockInfo2, structureBlockInfo22)) continue
                        val blockPos4 = structureBlockInfo22.pos
                        val blockPos5 = blockPos3.subtract(blockPos4)
                        val blockBox3 = structurePoolElement2.getBoundingBox(structureManager, blockPos5, blockRotation2)
                        val m = blockBox3.minY
                        val projection2 = structurePoolElement2.projection
                        val bl3 = projection2 == StructurePool.Projection.RIGID
                        val n = blockPos4.y
                        val o = j - n + JigsawBlock.getFacing(structureBlockInfo2.state).offsetY
                        if (bl && bl3) {
                            p = i + o
                        } else {
                            if (k == -1) {
                                k = chunkGenerator.getHeightOnGround(blockPos2.x, blockPos2.z, Heightmap.Type.WORLD_SURFACE_WG, world)
                            }
                            p = k - n
                        }
                        val q = p - m
                        val blockBox4 = blockBox3.offset(0, q, 0)
                        val blockPos6 = blockPos5.add(0, q, 0)
                        if (l > 0) {
                            r = (l + 1).coerceAtLeast(blockBox4.maxY - blockBox4.minY)
                            blockBox4.encompass(BlockPos(blockBox4.minX, blockBox4.minY + r, blockBox4.minZ))
                        }
                        if (VoxelShapes.matchesAnywhere(mutableObject2.value, VoxelShapes.cuboid(Box.from(blockBox4).contract(0.25)), BooleanBiFunction.ONLY_SECOND)) continue
                        mutableObject2.value = VoxelShapes.combine(mutableObject2.value, VoxelShapes.cuboid(Box.from(blockBox4)), BooleanBiFunction.ONLY_FIRST)
                        r = piece.groundLevelDelta
                        val s = if (bl3) r - o else structurePoolElement2.groundLevelDelta
                        val poolStructurePiece = pieceFactory.create(structureManager, structurePoolElement2, blockPos6, s, blockRotation2, blockBox4)
                        if (bl) {
                            t = i + j
                        } else if (bl3) {
                            t = p + n
                        } else {
                            if (k == -1) {
                                k = chunkGenerator.getHeightOnGround(blockPos2.x, blockPos2.z, Heightmap.Type.WORLD_SURFACE_WG, world)
                            }
                            t = k + o / 2
                        }
                        piece.addJunction(JigsawJunction(blockPos3.x, t - j + r, blockPos3.z, o, projection2))
                        poolStructurePiece.addJunction(JigsawJunction(blockPos2.x, t - n + s, blockPos2.z, -o, projection))
                        children.add(poolStructurePiece)
                        if (currentSize + 1 > maxSize) return
                        structurePieces.addLast(ShapedPoolStructurePiece(poolStructurePiece, mutableObject2, currentSize + 1))
                        return
                    }
                }
            }
        }
    }

    internal class ShapedPoolStructurePiece(val piece: PoolStructurePiece, val pieceShape: MutableObject<VoxelShape?>, val currentSize: Int)
}