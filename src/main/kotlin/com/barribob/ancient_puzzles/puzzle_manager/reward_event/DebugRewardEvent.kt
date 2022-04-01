package com.barribob.ancient_puzzles.puzzle_manager.reward_event

import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

private const val messageNbtKey = "message"

class DebugRewardEvent(private var message: String = "") : RewardEvent {
    constructor(nbtCompound: NbtCompound) : this(nbtCompound.getString(messageNbtKey))

    override fun doEvent(world: World) {
        println(message)
    }

    fun setMessage(message: String) {
        this.message = message
    }

    override fun toNbt(): NbtCompound {
        val nbtCompound = NbtCompound()
        nbtCompound.putString(messageNbtKey, message)
        return nbtCompound
    }
}