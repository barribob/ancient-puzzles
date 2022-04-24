package com.barribob.ancient_puzzles.structure.generator

import net.minecraft.structure.StructureGeneratorFactory
import net.minecraft.structure.pool.StructurePool
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig
import java.util.*

class SimplePoolProvider(private val context: StructureGeneratorFactory.Context<StructurePoolFeatureConfig>) : PoolProvider {
    override fun getPool(data: PoolProviderData): Pair<Identifier, Optional<StructurePool?>> {
        val poolIdentifier = Identifier(data.structureBlockInfo.nbt.getString("pool"))
        val optional = context.registryManager.get(Registry.STRUCTURE_POOL_KEY).getOrEmpty(poolIdentifier)
        return Pair(poolIdentifier, optional)
    }
}