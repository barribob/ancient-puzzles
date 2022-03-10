package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound
import net.minecraft.world.chunk.Chunk
import java.lang.reflect.Type

class PuzzleManagerNbtRegistry {
    private val idRegistry = hashMapOf<String, (Chunk, NbtCompound) -> PuzzleManager>()
    private val typeRegistry = hashMapOf<Type, String>()

    init {
        register("press_all_block", ::PressAllBlocksPuzzleManager, PressAllBlocksPuzzleManager::class.java)
    }

    private fun register(id: String, factory: (Chunk, NbtCompound) -> PuzzleManager, type: Type) {
        idRegistry[id] = factory
        typeRegistry[type] = id
    }

    fun createPuzzleManagerFromNbt(chunk: Chunk, type: String, nbt: NbtCompound) : PuzzleManager {
        val puzzleFactory = idRegistry[type] ?: throw IllegalStateException("Tried to get puzzle manager type $type, which is not registered")
        return puzzleFactory.invoke(chunk, nbt)
    }

    fun getPuzzleManagerType(puzzleManager: PuzzleManager): String {
        return typeRegistry[puzzleManager::class.java] ?: throw IllegalStateException("Tried to get puzzle manager type ${puzzleManager::class.java}, which is not registered")
    }
}