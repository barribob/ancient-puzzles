package com.barribob.ancient_puzzles.puzzle_setup_processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getPuzzle
import com.barribob.ancient_puzzles.readBlockPos
import net.minecraft.nbt.NbtCompound
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class PillarCombinationPositionSetupProcesssor : PuzzleSetupProcessor {
    override fun setup(world: WorldView, pivot: BlockPos, structureBlockInfo: Structure.StructureBlockInfo, data: StructurePlacementData): Structure.StructureBlockInfo {
        val get = structureBlockInfo.nbt.get("data") as NbtCompound
        if (get.contains("lock_id")) {
            world.getChunk(pivot).getPuzzle(Mod.puzzles.pillarCombination).addPosition(get.getInt("lock_id"), structureBlockInfo.pos)
        }

        return Structure.StructureBlockInfo(structureBlockInfo.pos, Mod.blocks.stoneBrickPuzzleLight.defaultState, NbtCompound())
    }
}
