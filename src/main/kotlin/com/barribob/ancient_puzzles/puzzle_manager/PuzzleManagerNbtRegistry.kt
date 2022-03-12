package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound
import java.lang.reflect.Type

class PuzzleManagerNbtRegistry {
    private val idRegistry = hashMapOf<String, (NbtCompound) -> PuzzleManager>()
    private val typeRegistry = hashMapOf<Type, String>()

    init {
        register("press_all_blocks", ::PressAllBlocksPuzzleManager, PressAllBlocksPuzzleManager::class.java)
    }

    private fun register(id: String, factory: (NbtCompound) -> PuzzleManager, type: Type) {
        idRegistry[id] = factory
        typeRegistry[type] = id
    }

    fun createPuzzleManagerFromNbt(type: String, nbt: NbtCompound) : PuzzleManager {
        val puzzleFactory = idRegistry[type] ?: throw IllegalStateException("Tried to get puzzle manager type $type, which is not registered")
        return puzzleFactory.invoke(nbt)
    }

    fun getPuzzleManagerType(puzzleManager: PuzzleManager): String {
        return typeRegistry[puzzleManager::class.java] ?: throw IllegalStateException("Tried to get puzzle manager type ${puzzleManager::class.java}, which is not registered")
    }
}