package com.barribob.ancient_puzzles.nbt

import com.barribob.ancient_puzzles.mock_objects.TestBlockView
import com.barribob.ancient_puzzles.puzzle_manager.PressAllBlocksPuzzleManager
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.BlockPos
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestPressAllBlocksSerialization {
    @Test
    fun verifyPressAllBlockPuzzleManagerSerialization() {
        val puzzleManager = PressAllBlocksPuzzleManager(TestBlockView(), listOf(BlockPos(123, 68, 456)))
        val nbt = puzzleManager.toNbt()
        val nbtCompound = nbt.getList("block_positions", NbtCompound().type.toInt()).first() as NbtCompound
        Assertions.assertEquals(123, nbtCompound.getInt("x"))
        Assertions.assertEquals(68, nbtCompound.getInt("y"))
        Assertions.assertEquals(456, nbtCompound.getInt("z"))
    }

    @Test
    fun verifyPressAllBlockPuzzleManagerDeserialization() {
        val puzzleManager = PressAllBlocksPuzzleManager(TestBlockView(), listOf(BlockPos(123, 68, 456)))
        val nbt = puzzleManager.toNbt()
        val serialization = PressAllBlocksPuzzleManager(TestBlockView(), nbt)
        val nbtCompound = serialization.toNbt().getList("block_positions", NbtCompound().type.toInt()).first() as NbtCompound
        Assertions.assertEquals(123, nbtCompound.getInt("x"))
        Assertions.assertEquals(68, nbtCompound.getInt("y"))
        Assertions.assertEquals(456, nbtCompound.getInt("z"))
    }
}