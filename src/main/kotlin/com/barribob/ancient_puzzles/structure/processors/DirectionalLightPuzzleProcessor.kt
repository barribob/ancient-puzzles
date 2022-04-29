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

class DirectionalLightPuzzleProcessor : StructureProcessor() {
    companion object {
        val CODEC: Codec<DirectionalLightPuzzleProcessor> = Codec.unit { INSTANCE }
        private val INSTANCE = DirectionalLightPuzzleProcessor()
    }

    override fun process(
        world: WorldView,
        pos: BlockPos,
        pivot: BlockPos,
        structureBlockInfo: Structure.StructureBlockInfo,
        structureBlockInfo2: Structure.StructureBlockInfo,
        data: StructurePlacementData?
    ): Structure.StructureBlockInfo {
        val puzzle = world.getChunk(pivot).getPuzzle(Mod.puzzles.directionalLight)
        if (structureBlockInfo.state.isOf(Mod.blocks.endBattery)) {
            puzzle.setBatteryPosition(structureBlockInfo2.pos)
        }
        else if (structureBlockInfo.state.isOf(Mod.blocks.stoneBrickChest)) {
            puzzle.setFinalPosition(structureBlockInfo2.pos)
        }
        return structureBlockInfo2
    }

    override fun getType(): StructureProcessorType<*> {
        return Mod.structures.directionalLightPuzzleProcessor
    }
}