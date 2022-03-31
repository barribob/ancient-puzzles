package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.features.RuinedGroundFeature
import net.minecraft.util.registry.Registry
import net.minecraft.world.gen.feature.DefaultFeatureConfig

class ModFeatures {

    fun init() {
        Registry.register(Registry.FEATURE, Mod.identifier("ruined_ground"), RuinedGroundFeature(DefaultFeatureConfig.CODEC))
    }
}