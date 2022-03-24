package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound

class PuzzleManagerFactory {
    private val idRegistry = hashMapOf<String, Factories>()

    fun register(id: String, nbtFactory: (NbtCompound) -> PuzzleManager, factory: () -> PuzzleManager) {
        idRegistry[id] = Factories(nbtFactory, factory)
    }

    fun createPuzzleManager(type: String, nbt: NbtCompound) : PuzzleManager {
        val puzzleFactory = idRegistry[type] ?: throw IllegalStateException("Tried to get puzzle manager type $type, which is not registered")
        return puzzleFactory.nbtFactory.invoke(nbt)
    }

    fun createPuzzleManager(type: String) : PuzzleManager {
        val puzzleFactory = idRegistry[type] ?: throw IllegalStateException("Tried to get puzzle manager type $type, which is not registered")
        return puzzleFactory.factory.invoke()
    }

    data class Factories(val nbtFactory: (NbtCompound) -> PuzzleManager, val factory: () -> PuzzleManager)
}