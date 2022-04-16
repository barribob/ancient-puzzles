package com.barribob.ancient_puzzles.puzzle_setup_processors

import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

interface PuzzleSetupProcessor {
    fun setup(world: WorldView, pivot: BlockPos,  structureBlockInfo: Structure.StructureBlockInfo, data: StructurePlacementData) : Structure.StructureBlockInfo
}