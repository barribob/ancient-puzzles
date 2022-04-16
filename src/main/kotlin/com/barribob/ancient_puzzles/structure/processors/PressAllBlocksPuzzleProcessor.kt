package com.barribob.ancient_puzzles.structure.processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getPuzzle
import com.mojang.serialization.Codec
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class PressAllBlocksPuzzleProcessor : StructureProcessor() {
    companion object {
        val CODEC: Codec<PressAllBlocksPuzzleProcessor> = Codec.unit { INSTANCE }
        private val INSTANCE = PressAllBlocksPuzzleProcessor()
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
            return structureBlockInfo2
        }
        return structureBlockInfo2
    }

    override fun getType(): StructureProcessorType<*> {
        return Mod.structures.pressAllBlocksPuzzleProcessor
    }
}