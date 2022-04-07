package com.barribob.ancient_puzzles.puzzle_manager

import com.barribob.ancient_puzzles.Mod
import com.barribob.ancient_puzzles.readBlockPos
import com.barribob.ancient_puzzles.toNbt
import net.barribob.maelstrom.MaelstromMod
import net.minecraft.block.Block
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.server.world.ServerWorld
import net.minecraft.state.property.Properties.LIT
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

class PressAllBlocksPuzzleManager() : PuzzleManager {
    private var blockPositions: MutableList<BlockPos> = mutableListOf()

    constructor(nbtCompound: NbtCompound) : this() {
        blockPositions = loadBlockPositions(nbtCompound).toMutableList()
    }

    fun visualizePuzzle(world: ServerWorld) {
        if (blockPositions.isNotEmpty()) {
            val points = blockPositions.flatMap { (0..20).map { i -> Vec3d(it.x.toDouble(), (it.y + i * 0.5), it.z.toDouble()) } }
            MaelstromMod.debugPoints.drawDebugPoints(points, 60, points.first(), world)
        }
    }

    override fun isSolved(world: World): Boolean {

        val allBlocksLit = allBlocksLit(world)
        if(allBlocksLit) {
            blockPositions.forEach{ replacePuzzleWithBreakableBlock(world, it) }
        }
        return allBlocksLit
    }

    fun replacePuzzleWithBreakableBlock(world: World, pos: BlockPos) {
        if(world.getBlockState(pos).isOf(Mod.blocks.inputBlock)) {
            world.setBlockState(pos, Mod.blocks.finishedPressAllBlocksInput.defaultState, Block.NOTIFY_LISTENERS)
        }
    }

    override fun toNbt(): NbtCompound {
        return saveBlockPositions(blockPositions)
    }

    private fun allBlocksLit(world: World) = blockPositions.all { world.getBlockState(it).getOrEmpty(LIT).orElse(true) }

    fun addPosition(pos: BlockPos) {
        blockPositions.add(pos)
    }

    companion object {
        fun loadBlockPositions(nbtCompound: NbtCompound) = nbtCompound.getList(
            "block_positions",
            NbtCompound().type.toInt()
        ).filterIsInstance<NbtCompound>().map(::readBlockPos)

        fun saveBlockPositions(blockPositions: List<BlockPos>): NbtCompound {
            val compound = NbtCompound()
            val blockPositionsNbt = NbtList()
            for (pos in blockPositions) {
                blockPositionsNbt.add(pos.toNbt())
            }
            compound.put("block_positions", blockPositionsNbt)
            return compound
        }
    }
}
