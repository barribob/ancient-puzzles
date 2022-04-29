package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.puzzle_manager.*

class ModPuzzles {
    val puzzleManagerFactory = PuzzleManagerFactory()
    val pressAllBlocks = PuzzleType<PressAllBlocksPuzzleManager>("press_all_blocks")
    val pillarCombination = PuzzleType<PillarCombinationPuzzleManager>("pillar_combination")
    val directionalLight = PuzzleType<DirectionalLightPuzzleManager>("directional_light")

    fun init() {
        puzzleManagerFactory.register(pressAllBlocks, ::PressAllBlocksPuzzleManager, ::PressAllBlocksPuzzleManager)
        puzzleManagerFactory.register(pillarCombination, ::PillarCombinationPuzzleManager, ::PillarCombinationPuzzleManager)
        puzzleManagerFactory.register(directionalLight, ::DirectionalLightPuzzleManager, ::DirectionalLightPuzzleManager)
    }
}