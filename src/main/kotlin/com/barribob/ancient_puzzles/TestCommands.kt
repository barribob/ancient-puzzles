package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.cardinal_components.ModComponents
import com.barribob.ancient_puzzles.puzzle_manager.PressAllBlocksPuzzleManager
import net.barribob.maelstrom.MaelstromMod
import net.minecraft.server.command.ServerCommandSource

class TestCommands {
    init {
        MaelstromMod.testCommand.addId(::generatePuzzle.name, ::generatePuzzle)
        MaelstromMod.testCommand.addId(::clearPuzzles.name, ::clearPuzzles)
        MaelstromMod.testCommand.addId(::visualizePuzzles.name, ::visualizePuzzles)
    }

    private fun generatePuzzle(source: ServerCommandSource) {
        val blockPos = source.entity!!.blockPos
        val puzzlePos = blockPos.west(17)
        source.world.setBlockState(blockPos, Mod.blocks.inputBlock.defaultState)
        val chunk = source.world.getChunk(puzzlePos)
        (chunk.getPuzzle(Mod.puzzles.pressAllBlocks) as PressAllBlocksPuzzleManager).addPosition(blockPos)
    }

    private fun clearPuzzles(source: ServerCommandSource) {
        val blockPos = source.entity!!.blockPos
        val chunk = source.world.getChunk(blockPos)
        ModComponents.puzzleManagerComponentKey.get(chunk).removeAllPuzzles()
    }

    private fun visualizePuzzles(source: ServerCommandSource) {
        val blockPos = source.entity!!.blockPos
        val chunk = source.world.getChunk(blockPos)
        (chunk.getPuzzle(Mod.puzzles.pressAllBlocks) as PressAllBlocksPuzzleManager).visualizePuzzle(source.world)
    }
}