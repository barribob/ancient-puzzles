package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.features.PressAllBlocksPuzzleFeature
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.util.Identifier
import net.minecraft.util.math.Direction
import net.minecraft.util.math.intprovider.ConstantIntProvider
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.YOffset
import net.minecraft.world.gen.blockpredicate.BlockPredicate
import net.minecraft.world.gen.feature.*
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider
import net.minecraft.world.gen.placementmodifier.*

class ModFeatures {

    fun init() {
        val randomUniformHeight = HeightRangePlacementModifier.of(ConstantHeightProvider.create(YOffset.fixed(90)))
        val findSolidBlock = EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), 32)
        val verticalOffset = RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))
        val modifiers = listOf(RarityFilterPlacementModifier.of(10), SquarePlacementModifier.of(), BiomePlacementModifier.of(), randomUniformHeight, findSolidBlock, verticalOffset)

        val pressAllBlocksFeatureRegistryKey = registerFeature(Mod.identifier("press_all_blocks_puzzle"), PressAllBlocksPuzzleFeature(DefaultFeatureConfig.CODEC), modifiers)
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.SURFACE_STRUCTURES, pressAllBlocksFeatureRegistryKey)
    }

    private fun registerFeature(
        identifier: Identifier,
        feature: Feature<DefaultFeatureConfig>,
        modifiers: List<PlacementModifier>
    ): RegistryKey<PlacedFeature>? {
        val featureRegistryKey = RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, identifier)
        val placedFeatureRegistryKey = RegistryKey.of(Registry.PLACED_FEATURE_KEY, identifier)
        val registeredFeature = Registry.register(Registry.FEATURE, identifier.toString(), feature)
        val featureRegistryEntry = ConfiguredFeatures.register(featureRegistryKey.value.toString(), registeredFeature)

        PlacedFeatures.register(placedFeatureRegistryKey.value.toString(), featureRegistryEntry, modifiers)
        return placedFeatureRegistryKey
    }
}