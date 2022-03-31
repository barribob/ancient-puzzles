package com.barribob.ancient_puzzles.puzzle_manager.reward_event

import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

interface RewardEvent {
    fun doEvent(world: World)
    fun toNbt(): NbtCompound
}