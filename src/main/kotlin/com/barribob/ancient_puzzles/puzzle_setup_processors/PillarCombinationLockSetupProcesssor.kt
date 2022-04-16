package com.barribob.ancient_puzzles.puzzle_setup_processors

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.getPuzzle
import net.minecraft.block.Blocks
import net.minecraft.nbt.NbtCompound
import net.minecraft.structure.Structure
import net.minecraft.structure.StructurePlacementData
import net.minecraft.util.math.BlockPos
import net.minecraft.world.WorldView

class PillarCombinationLockSetupProcesssor : PuzzleSetupProcessor {
    override fun setup(world: WorldView, pivot: BlockPos, structureBlockInfo: Structure.StructureBlockInfo, data: StructurePlacementData): Structure.StructureBlockInfo {
        val get = structureBlockInfo.nbt.get("data") as NbtCompound
        if (get.contains("lock_id")) {
            val lit = data.getRandom(structureBlockInfo.pos).nextBoolean()
            world.getChunk(pivot).getPuzzle(Mod.puzzles.pillarCombination).addLockState(get.getInt("lock_id"), lit)
            val indicatorBlock = if (lit) Mod.blocks.solvedStoneBrickPuzzleLight else Blocks.STONE_BRICKS

            return Structure.StructureBlockInfo(structureBlockInfo.pos, indicatorBlock.defaultState, NbtCompound())
        }

        return Structure.StructureBlockInfo(structureBlockInfo.pos, Blocks.STONE_BRICKS.defaultState, NbtCompound())
    }
}