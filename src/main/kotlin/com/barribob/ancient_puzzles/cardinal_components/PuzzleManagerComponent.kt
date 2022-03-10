package com.barribob.ancient_puzzles.cardinal_components

import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManagerNbtRegistry
import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.world.chunk.Chunk

class PuzzleManagerComponent(private val chunk: Chunk) : ComponentV3, ServerTickingComponent {
    private val puzzleManagers = mutableSetOf<PuzzleManager>()
    private val puzzleManagerNbtRegistry = PuzzleManagerNbtRegistry()
    private val puzzleManagersKey = "puzzle_managers"
    private val puzzleManagerTypeKey = "type"
    private val puzzleManagerKey = "puzzle_manager"

    override fun serverTick() {
        puzzleManagers.forEach { it.tick() }

        checkRemove()
    }

    private fun checkRemove() {
        val toRemove = puzzleManagers.filter { it.shouldRemove() }.toSet()
        if (toRemove.isNotEmpty()) {
            puzzleManagers.removeAll(toRemove)
            chunk.setNeedsSaving(true)
        }
    }

    fun addPuzzleManager(puzzleManager: PuzzleManager) {
        puzzleManagers.add(puzzleManager)
        chunk.setNeedsSaving(true)
    }

    override fun readFromNbt(tag: NbtCompound) {
        if (tag.contains(puzzleManagersKey)) {
            val puzzleManagersNbt = tag.getList(puzzleManagersKey, NbtCompound().type.toInt())
            puzzleManagersNbt.forEach {
                val puzzleManagerNbt = (it as NbtCompound)
                val puzzleManager = puzzleManagerNbtRegistry.createPuzzleManagerFromNbt(chunk, puzzleManagerNbt.getString(puzzleManagerTypeKey), puzzleManagerNbt.getCompound(puzzleManagerKey))
                puzzleManagers.add(puzzleManager)
            }
        }
    }

    override fun writeToNbt(tag: NbtCompound) {
        val puzzleManagersNbt = NbtList()
        puzzleManagers.forEach {
            val puzzleManagerNbt = NbtCompound()
            puzzleManagerNbt.put(puzzleManagerTypeKey, NbtString.of(puzzleManagerNbtRegistry.getPuzzleManagerType(it)))
            puzzleManagerNbt.put(puzzleManagerKey, it.toNbt())
            puzzleManagersNbt.add(puzzleManagerNbt)
        }
        tag.put(puzzleManagersKey, puzzleManagersNbt)
    }
}
