package com.barribob.ancient_puzzles.structure

import com.barribob.ancient_puzzles.mixins.DataFixMixin
import com.barribob.ancient_puzzles.mixins.StructureFeatureRegisterInvoker
import net.minecraft.datafixer.fix.StructuresToConfiguredStructuresFix
import net.minecraft.structure.StructureSets
import net.minecraft.util.Identifier
import net.minecraft.util.registry.BuiltinRegistries
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryEntry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.gen.GenerationStep
import net.minecraft.world.gen.chunk.placement.StructurePlacement
import net.minecraft.world.gen.feature.ConfiguredStructureFeature
import net.minecraft.world.gen.feature.StructureFeature

class StructureRegister(private val structureIdentifier: Identifier) {
    private val configuredStructureKey: RegistryKey<ConfiguredStructureFeature<*, *>> = createConfigureStructureKey(structureIdentifier)

    fun register(structureFeature: StructureFeature<*>, configuredStructure: ConfiguredStructureFeature<*, *>, structurePlacement: StructurePlacement) {
        StructureFeatureRegisterInvoker.invokeRegister(structureIdentifier.toString(), structureFeature, GenerationStep.Feature.SURFACE_STRUCTURES)
        val configuredStructureEntry = registerConfiguredStructure(configuredStructureKey, configuredStructure)
        val structureSetKey = RegistryKey.of(Registry.STRUCTURE_SET_KEY, structureIdentifier)
        StructureSets.register(structureSetKey, configuredStructureEntry, structurePlacement)

        DataFixMixin.ancientPuzzles_setStructures(DataFixMixin.ancientPuzzles_getStructures() +
                mapOf(Pair(structureIdentifier.toString(), StructuresToConfiguredStructuresFix.Mapping.create(structureIdentifier.toString()))))
    }

    private fun createConfigureStructureKey(identifier: Identifier) = RegistryKey.of(Registry.CONFIGURED_STRUCTURE_FEATURE_KEY,  identifier)
    private fun registerConfiguredStructure(key: RegistryKey<ConfiguredStructureFeature<*, *>>, configuredStructure: ConfiguredStructureFeature<*, *>): RegistryEntry<ConfiguredStructureFeature<*, *>> {
        return BuiltinRegistries.add(BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, key.value, configuredStructure)
    }
}