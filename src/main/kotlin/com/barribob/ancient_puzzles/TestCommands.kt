package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.puzzle_manager.PressAllBlocksPuzzleManager
import net.barribob.maelstrom.MaelstromMod
import net.minecraft.server.command.ServerCommandSource

class TestCommands {
    init {
        MaelstromMod.testCommand.addId(::generatePuzzle.name, ::generatePuzzle)
    }

    private fun generatePuzzle(source: ServerCommandSource) {
        val blockPos = source.entity!!.blockPos
        val puzzlePos = blockPos.west(17)
        source.world.setBlockState(blockPos, Mod.blocks.inputBlock.defaultState)
        val chunk = source.world.getChunk(puzzlePos)
        chunk.addPuzzle(PressAllBlocksPuzzleManager(listOf(blockPos)))
    }
}