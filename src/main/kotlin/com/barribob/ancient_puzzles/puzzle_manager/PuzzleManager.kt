package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

interface PuzzleManager {
    fun isSolved(world: World): Boolean
    fun toNbt(): NbtCompound
}