package com.barribob.ancient_puzzles.blocks

import com.barribob.ancient_puzzles.Mod
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtString
import net.minecraft.util.math.BlockPos

class PuzzleSetupBlockEntity(pos: BlockPos?, state: BlockState?) : BlockEntity(Mod.blocks.puzzleSetupBlockEntityType, pos, state) {
    var data: NbtCompound = NbtCompound()
    var processor: String = ""

    override fun readNbt(nbt: NbtCompound) {
        if(nbt.contains("processor", NbtString.of("").type.toInt())) {
            processor = nbt.getString("processor")
        }
        if(nbt.contains("data")) {
            data = nbt.getCompound("data")
        }
        super.readNbt(nbt)
    }

    override fun writeNbt(nbt: NbtCompound) {
        nbt.putString("processor", processor)
        nbt.put("data", data)
        super.writeNbt(nbt)
    }
}