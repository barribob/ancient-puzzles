package com.barribob.ancient_puzzles.puzzle_manager.reward_event

import net.minecraft.nbt.NbtCompound

class RewardEventFactory {
    private val idRegistry = hashMapOf<String, Factories>()

    fun <T: RewardEvent> register(id: RewardType<T>, nbtFactory: (NbtCompound) -> T) {
        idRegistry[id.type] = Factories(nbtFactory)
    }

    fun create(type: String, nbt: NbtCompound) : RewardEvent {
        val puzzleFactory = idRegistry[type] ?: throw IllegalStateException("Tried to get type $type, which is not registered")
        return puzzleFactory.nbtFactory.invoke(nbt)
    }

    data class Factories(val nbtFactory: (NbtCompound) -> RewardEvent)
}