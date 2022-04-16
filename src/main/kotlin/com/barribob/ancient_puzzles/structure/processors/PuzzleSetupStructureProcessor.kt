package com.barribob.ancient_puzzles.structure.processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.puzzle_setup_processors.PillarCombinationLockSetupProcesssor
import com.barribob.ancient_puzzles.puzzle_setup_processors.PillarCombinationPositionSetupProcesssor
import com.mojang.serialization.Codec
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class PuzzleSetupStructureProcessor : StructureProcessor() {
    private val setupProcessors = mapOf(
        Pair("pillar_combination_lock", PillarCombinationLockSetupProcesssor()),
        Pair("pillar_combination_position", PillarCombinationPositionSetupProcesssor())
    )

    companion object {
        val CODEC: Codec<PuzzleSetupStructureProcessor> = Codec.unit { INSTANCE }
        private val INSTANCE = PuzzleSetupStructureProcessor()
    }

    override fun process(
        world: WorldView,
        pos: BlockPos,
        pivot: BlockPos,
        structureBlockInfo: Structure.StructureBlockInfo,
        structureBlockInfo2: Structure.StructureBlockInfo,
        data: StructurePlacementData
    ): Structure.StructureBlockInfo {
        if (structureBlockInfo2.state.isOf(Mod.blocks.puzzleSetupBlock)) {
            if (structureBlockInfo2.nbt.contains("processor")) {
                val processorId = structureBlockInfo2.nbt.getString("processor")
                val puzzleSetupProcessor = setupProcessors[processorId]
                if(puzzleSetupProcessor != null) {
                    return puzzleSetupProcessor.setup(world, pivot, structureBlockInfo2, data)
                }
            }
            return structureBlockInfo2
        }
        return structureBlockInfo2
    }

    override fun getType(): StructureProcessorType<*> {
        return Mod.structures.puzzleSetupStructureProcessor
    }
}