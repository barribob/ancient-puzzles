package com.barribob.ancient_puzzles.cardinal_components

import com.barribob.ancient_puzzles.Mod
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.chunk.ChunkComponentInitializer
import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3

class ModComponents : ChunkComponentInitializer {
    companion object {
        val puzzleManagerComponentKey: ComponentKey<PuzzleManagerComponent> = ComponentRegistryV3.INSTANCE.getOrCreate(Mod.identifier("puzzle_manager"), PuzzleManagerComponent::class.java)
    }

    override fun registerChunkComponentFactories(registry: ChunkComponentFactoryRegistry) {
        registry.register(puzzleManagerComponentKey, ::PuzzleManagerComponent)
    }
}