package com.barribob.ancient_puzzles.cardinal_components

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleManager
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardEvent
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardTracker
import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.world.World
import net.minecraft.world.chunk.Chunk
import net.minecraft.world.chunk.WorldChunk

class PuzzleManagerComponent(private val chunk: Chunk) : ComponentV3, ServerTickingComponent {
    private val puzzleManagers = mutableMapOf<String, Puzzle>()
    private val puzzleManagerNbtRegistry = Mod.puzzles.puzzleManagerFactory
    private val puzzleManagerFactory = Mod.puzzles.puzzleManagerFactory
    private val puzzleManagersKey = "puzzle_managers"
    private val puzzleManagerTypeKey = "type"
    private val puzzleManagerKey = "puzzle_manager"

    private data class Puzzle(val puzzleManager: PuzzleManager, val rewardTracker: RewardTracker = RewardTracker(Mod.rewards.rewardFactory))

    override fun serverTick() {
        val worldChunk = chunk

        if (worldChunk is WorldChunk) {
            val world = worldChunk.world
            checkRemove(world)
        }
    }

    private fun checkRemove(world: World) {
        val toRemove = puzzleManagers.filter { it.value.puzzleManager.isSolved(world) }
        if (toRemove.isNotEmpty()) {
            toRemove.map { it.key }.forEach { puzzleManagers.remove(it) }
            toRemove.forEach { it.value.rewardTracker.doReward(world) }
            chunk.setNeedsSaving(true)
        }
    }

    private fun getPuzzle(type: String): Puzzle {
        val puzzle = puzzleManagers[type]
        if (puzzle != null) return puzzle
        val newPuzzleManager = puzzleManagerFactory.createPuzzleManager(type)
        val newPuzzle = Puzzle(newPuzzleManager)
        puzzleManagers[type] = newPuzzle
        return newPuzzle
    }

    fun getPuzzleManager(type: String): PuzzleManager = getPuzzle(type).puzzleManager

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
                val puzzle = Puzzle(puzzleManager)
                puzzleManagers[type] = puzzle
                puzzle.rewardTracker.loadNbt(puzzleManagerNbt)
            }
        }
    }

    override fun writeToNbt(tag: NbtCompound) {
        val puzzleManagersNbt = NbtList()
        puzzleManagers.forEach {
            val puzzleManagerNbt = NbtCompound()
            puzzleManagerNbt.put(puzzleManagerTypeKey, NbtString.of(it.key))
            puzzleManagerNbt.put(puzzleManagerKey, it.value.puzzleManager.toNbt())
            it.value.rewardTracker.toNbt(puzzleManagerNbt)
            puzzleManagersNbt.add(puzzleManagerNbt)
        }
        tag.put(puzzleManagersKey, puzzleManagersNbt)
    }

    fun getRewardForPuzzle(type: String, type1: String): RewardEvent {
        return getPuzzle(type).rewardTracker.getRewardEvent(type1)
    }
}
