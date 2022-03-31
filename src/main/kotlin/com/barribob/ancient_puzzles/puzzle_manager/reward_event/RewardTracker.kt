package com.barribob.ancient_puzzles.puzzle_manager.reward_event

import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

class RewardTracker(private val rewardEventFactory: RewardEventFactory) {
    private var reward: Reward? = null
    private val rewardNbtKey = "reward"
    private val typeNbtKey = "type"

    data class Reward(val rewardType: String, val rewardEvent: RewardEvent)

    fun <T : RewardEvent> setReward(type: RewardType<T>, rewardEvent: T) {
        reward = Reward(type.type, rewardEvent)
    }

    fun loadNbt(nbtCompound: NbtCompound) {
        if (nbtCompound.contains(typeNbtKey) && nbtCompound.contains(rewardNbtKey)) {
            val type = nbtCompound.getString(typeNbtKey)
            reward = Reward(type, rewardEventFactory.create(type, nbtCompound.getCompound(rewardNbtKey)))
        }
    }

    fun doReward(world: World) {
        reward?.rewardEvent?.doEvent(world)
    }

    fun toNbt(): NbtCompound {
        val compound = NbtCompound()
        if (reward != null) {
            compound.putString(typeNbtKey, reward?.rewardType)
            compound.put(rewardNbtKey, reward?.rewardEvent?.toNbt())
        }
        return compound
    }
}