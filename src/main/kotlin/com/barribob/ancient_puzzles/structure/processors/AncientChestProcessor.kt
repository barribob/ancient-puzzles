package com.barribob.ancient_puzzles.structure.processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getRewardForPuzzle
import com.barribob.ancient_puzzles.puzzle_manager.PuzzleType
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class AncientChestProcessor(val puzzleType: String) : StructureProcessor() {
    companion object {
        val CODEC: Codec<AncientChestProcessor> = RecordCodecBuilder.create { instance ->
            instance.group(
            Codec.STRING.fieldOf("puzzle_type").forGetter { it.puzzleType }
        ).apply(instance, ::AncientChestProcessor) }
    }

    override fun process(
        world: WorldView,
        pos: BlockPos,
        pivot: BlockPos,
        structureBlockInfo: Structure.StructureBlockInfo,
        structureBlockInfo2: Structure.StructureBlockInfo,
        data: StructurePlacementData?
    ): Structure.StructureBlockInfo {
        if (structureBlockInfo.state.isOf(Mod.blocks.stoneBrickChest)) {
            world.getChunk(pivot).getRewardForPuzzle(PuzzleType(puzzleType), Mod.rewards.ancientChestRewardEvent).addChestPosition(structureBlockInfo2.pos)
            return structureBlockInfo2
        }
        return structureBlockInfo2
    }

    override fun getType(): StructureProcessorType<*> {
        return Mod.structures.ancientChestProcessor
    }
}
