package com.barribob.ancient_puzzles.puzzle_manager.reward_event

import net.minecraft.nbt.NbtCompound

class RewardEventFactory {
    private val idRegistry = hashMapOf<String, Factories>()

    fun <T: RewardEvent> register(id: RewardType<T>, nbtFactory: (NbtCompound) -> T, factory: () -> T) {
        idRegistry[id.type] = Factories(nbtFactory, factory)
    }

    fun create(type: String, nbt: NbtCompound) : RewardEvent {
        val factory = idRegistry[type] ?: throw IllegalStateException("Tried to get type $type, which is not registered")
        return factory.nbtFactory.invoke(nbt)
    }

    fun create(type: String) : RewardEvent{
        val factory = idRegistry[type] ?: throw IllegalStateException("Tried to get type $type, which is not registered")
        return factory.factory.invoke()
    }

    data class Factories(val nbtFactory: (NbtCompound) -> RewardEvent, val factory: () -> RewardEvent)
}