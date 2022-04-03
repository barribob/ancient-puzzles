package com.barribob.ancient_puzzles.structure.processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getRewardForPuzzle
import com.mojang.serialization.Codec
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.structure.processor.StructureProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class AncientChestProcessor : StructureProcessor() {
    companion object {
        val CODEC: Codec<AncientChestProcessor> = Codec.unit { INSTANCE }
        private val INSTANCE = AncientChestProcessor()
    }

    override fun process(
        world: WorldView,
        pos: BlockPos,
        pivot: BlockPos,
        structureBlockInfo: Structure.StructureBlockInfo,
        structureBlockInfo2: Structure.StructureBlockInfo,
        data: StructurePlacementData?
    ): Structure.StructureBlockInfo {
        if (structureBlockInfo.state.isOf(Mod.blocks.ancientChest)) {
            world.getChunk(pivot).getRewardForPuzzle(Mod.puzzles.pressAllBlocks, Mod.rewards.ancientChestRewardEvent).addChestPosition(structureBlockInfo2.pos)
            return structureBlockInfo2
        }
        return structureBlockInfo2
    }

    override fun getType(): StructureProcessorType<*> {
        return Mod.structures.ancientChestProcessor
    }
}
