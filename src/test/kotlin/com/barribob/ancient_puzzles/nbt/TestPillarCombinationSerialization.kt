package com.barribob.ancient_puzzles.nbt

import com.barribob.ancient_puzzles.puzzle_manager.PillarCombinationPuzzleManager
import net.minecraft.util.math.BlockPos
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TestPillarCombinationSerialization {
    @Test
    fun verifyPressAllBlockPuzzleManagerDeserialization() {
        val nbt = PillarCombinationPuzzleManager.savePuzzle(mapOf(Pair(1, BlockPos(123, 68, 456))), mapOf(Pair(1, true)))
        val positions = PillarCombinationPuzzleManager.loadPositions(nbt)
        val locks = PillarCombinationPuzzleManager.loadLocks(nbt)
        Assertions.assertEquals(123, positions[1]?.x)
        Assertions.assertEquals(68, positions[1]?.y)
        Assertions.assertEquals(456, positions[1]?.z)
        Assertions.assertTrue(locks[1] ?: false)
    }
}