package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.features.TestFeature
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.util.math.Direction
import net.minecraft.util.math.intprovider.ConstantIntProvider
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.feature.ConfiguredFeatures
import net.minecraft.world.gen.feature.DefaultFeatureConfig
import net.minecraft.world.gen.feature.PlacedFeatures
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider
import net.minecraft.world.gen.placementmodifier.*

class ModFeatures {

    fun init() {
        val testFeatureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, Mod.identifier("test"))
        val testPlacedFeatureRegistryKey = RegistryKey.of(Registry.PLACED_FEATURE_KEY, Mod.identifier("test"))
        val testFeature = Registry.register(Registry.FEATURE, "ancient_puzzles:test", TestFeature(DefaultFeatureConfig.CODEC))
        val testFeatureRegistryEntry = ConfiguredFeatures.register(testFeatureRegistryKey.value.toString(), testFeature)

        val randomUniformHeight = HeightRangePlacementModifier.of(ConstantHeightProvider.create(YOffset.fixed(90)))
        val findSolidBlock = EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), 32)
        val verticalOffset = RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))
        PlacedFeatures.register(testPlacedFeatureRegistryKey.value.toString(), testFeatureRegistryEntry,
            RarityFilterPlacementModifier.of(10), SquarePlacementModifier.of(), BiomePlacementModifier.of(),
            randomUniformHeight,
            findSolidBlock,
            verticalOffset
        )

        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.SURFACE_STRUCTURES, testPlacedFeatureRegistryKey)
    }
}