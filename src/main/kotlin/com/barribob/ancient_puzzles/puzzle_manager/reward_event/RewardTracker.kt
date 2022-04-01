package com.barribob.ancient_puzzles.puzzle_manager.reward_event

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.world.World

class RewardTracker(private val rewardEventFactory: RewardEventFactory) {
    private val rewardEvents = mutableMapOf<String, RewardEvent>()
    private val rewardsKey = "rewards"
    private val rewardNbtKey = "reward"
    private val typeNbtKey = "type"

    fun getRewardEvent(type: String): RewardEvent {
        val rewardEvent = rewardEvents[type]
        if (rewardEvent != null) return rewardEvent
        val newReward = rewardEventFactory.create(type)
        rewardEvents[type] = newReward
        return newReward
    }

    fun loadNbt(nbtCompound: NbtCompound) {
        if (nbtCompound.contains(rewardsKey)) {
            val rewardsNbt = nbtCompound.getList(rewardsKey, NbtCompound().type.toInt())
            rewardsNbt.toList().filterIsInstance<NbtCompound>().forEach(::loadReward)
        }
    }

    private fun loadReward(nbtCompound: NbtCompound) {
        if (nbtCompound.contains(typeNbtKey) && nbtCompound.contains(rewardNbtKey)) {
            val type = nbtCompound.getString(typeNbtKey)
            rewardEvents[type] = rewardEventFactory.create(type, nbtCompound.getCompound(rewardNbtKey))
        }
    }

    fun doReward(world: World) {
        rewardEvents.forEach { it.value.doEvent(world) }
    }

    fun toNbt(nbtCompound: NbtCompound) {
        val rewardNbt = NbtList()
        rewardEvents.map(::saveReward).forEach { rewardNbt.add(it) }
        nbtCompound.put(rewardsKey, rewardNbt)
    }

    private fun saveReward(entry: Map.Entry<String, RewardEvent>): NbtCompound {
        val compound = NbtCompound()
        compound.putString(typeNbtKey, entry.key)
        compound.put(rewardNbtKey, entry.value.toNbt())
        return compound
    }
}