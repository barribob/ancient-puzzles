package com.barribob.ancient_puzzles.cardinal_components

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.WorldChunk

class PuzzleManagerComponent(private val chunk: Chunk) : ComponentV3, ServerTickingComponent {
    private val puzzleManagers = mutableMapOf<String, PuzzleManager>()
    private val puzzleManagerNbtRegistry = Mod.puzzles.puzzleManagerFactory
    private val puzzleManagerFactory = Mod.puzzles.puzzleManagerFactory
    private val puzzleManagersKey = "puzzle_managers"
    private val puzzleManagerTypeKey = "type"
    private val puzzleManagerKey = "puzzle_manager"

    override fun serverTick() {
        val worldChunk = chunk

        if(worldChunk is WorldChunk) {
            val world = worldChunk.world
            tickPuzzles(world)
            checkRemove(world)
        }
    }

    private fun tickPuzzles(world: World) {
        puzzleManagers.forEach { it.value.tick(world) }
    }

    private fun checkRemove(world: World) {
        val toRemove = puzzleManagers.filter { it.value.shouldRemove(world) }.map { it.key }
        if (toRemove.isNotEmpty()) {
            toRemove.forEach { puzzleManagers.remove(it) }
            chunk.setNeedsSaving(true)
        }
    }

    fun getPuzzleManager(type: String) : PuzzleManager {
        val puzzleManager = puzzleManagers[type]
        if(puzzleManager != null) return puzzleManager
        val newPuzzleManager = puzzleManagerFactory.createPuzzleManager(type)
        puzzleManagers[type] = newPuzzleManager
        return newPuzzleManager
    }

    fun removeAllPuzzles() {
        puzzleManagers.clear()
        chunk.setNeedsSaving(true)
    }

    override fun readFromNbt(tag: NbtCompound) {
        if (tag.contains(puzzleManagersKey)) {
            val puzzleManagersNbt = tag.getList(puzzleManagersKey, NbtCompound().type.toInt())
            puzzleManagersNbt.forEach {
                val puzzleManagerNbt = (it as NbtCompound)
                val type = puzzleManagerNbt.getString(puzzleManagerTypeKey)
                val puzzleManager = puzzleManagerNbtRegistry.createPuzzleManager(type, puzzleManagerNbt.getCompound(puzzleManagerKey))
                puzzleManagers[type] = puzzleManager
            }
        }
    }

    override fun writeToNbt(tag: NbtCompound) {
        val puzzleManagersNbt = NbtList()
        puzzleManagers.forEach {
            val puzzleManagerNbt = NbtCompound()
            puzzleManagerNbt.put(puzzleManagerTypeKey, NbtString.of(it.key))
            puzzleManagerNbt.put(puzzleManagerKey, it.value.toNbt())
            puzzleManagersNbt.add(puzzleManagerNbt)
        }
        tag.put(puzzleManagersKey, puzzleManagersNbt)
    }
}
