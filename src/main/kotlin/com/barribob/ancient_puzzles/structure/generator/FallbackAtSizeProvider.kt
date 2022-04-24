package com.barribob.ancient_puzzles.structure.generator

import net.minecraft.structure.StructureGeneratorFactory
import net.minecraft.structure.pool.StructurePool
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig
import java.util.*

class FallbackAtSizeProvider(private val context: StructureGeneratorFactory.Context<StructurePoolFeatureConfig>) : PoolProvider {
    override fun getPool(data: PoolProviderData): Pair<Identifier, Optional<StructurePool?>> {
        val poolIdentifier = Identifier(data.structureBlockInfo.nbt.getString("pool"))
        val registry = context.registryManager.get(Registry.STRUCTURE_POOL_KEY)
        val optional = registry.getOrEmpty(poolIdentifier)
        if (optional.isPresent) {
            if (data.currentSize == context.config.size - 1) {
                val terminatorsId = optional.get().terminatorsId
                return Pair(terminatorsId, registry.getOrEmpty(terminatorsId))
            }
        }
        return Pair(poolIdentifier, optional)
    }
}