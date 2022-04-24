package com.barribob.ancient_puzzles.structure.generator

import net.minecraft.structure.pool.StructurePool
import net.minecraft.util.Identifier
import java.util.*

interface PoolProvider {
    fun getPool(data: PoolProviderData): Pair<Identifier, Optional<StructurePool?>>
}