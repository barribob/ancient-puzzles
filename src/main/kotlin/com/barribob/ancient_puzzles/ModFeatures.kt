package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.features.PressAllBlocksPuzzleFeature
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.DefaultFeatureConfig

class ModFeatures {

    fun init() {
        Registry.register(Registry.FEATURE, Mod.identifier("press_all_blocks_puzzle"), PressAllBlocksPuzzleFeature(DefaultFeatureConfig.CODEC))
    }
}