package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound

interface PuzzleManager {
    fun tick()
    fun shouldRemove(): Boolean
    fun toNbt(): NbtCompound
}