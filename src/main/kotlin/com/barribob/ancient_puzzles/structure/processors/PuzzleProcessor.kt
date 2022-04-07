package com.barribob.ancient_puzzles.structure.processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getPuzzle
import com.barribob.ancient_puzzles.getRewardForPuzzle
import com.mojang.serialization.Codec
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class PuzzleProcessor : StructureProcessor() {
    companion object {
        val CODEC: Codec<PuzzleProcessor> = Codec.unit { INSTANCE }
        private val INSTANCE = PuzzleProcessor()
    }

    override fun process(
        world: WorldView,
        pos: BlockPos,
        pivot: BlockPos,
        structureBlockInfo: Structure.StructureBlockInfo,
        structureBlockInfo2: Structure.StructureBlockInfo,
        data: StructurePlacementData?
    ): Structure.StructureBlockInfo {
        if (structureBlockInfo.state.isOf(Mod.blocks.stoneBrickPuzzleLight)) {
            val puzzle = world.getChunk(pivot).getPuzzle(Mod.puzzles.pressAllBlocks)
            puzzle.addPosition(structureBlockInfo2.pos)
            world.getChunk(pivot).getRewardForPuzzle(Mod.puzzles.pressAllBlocks, Mod.rewards.debugRewardEvent).setMessage("epic test reward")
            return structureBlockInfo2
        }
        return structureBlockInfo2
    }

    override fun getType(): StructureProcessorType<*> {
        return Mod.structures.puzzleProcessor
    }
}