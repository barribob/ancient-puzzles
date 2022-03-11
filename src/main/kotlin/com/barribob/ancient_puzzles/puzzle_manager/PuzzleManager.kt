package com.barribob.ancient_puzzles.puzzle_manager

import net.minecraft.nbt.NbtCompound
import net.minecraft.world.World

interface PuzzleManager {
    fun tick(world: World)
    fun shouldRemove(world: World): Boolean
    fun toNbt(): NbtCompound
}