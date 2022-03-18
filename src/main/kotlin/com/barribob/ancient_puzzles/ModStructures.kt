package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.mixins.StructureFeatureRegisterInvoker
import com.barribob.ancient_puzzles.structure.TestStructureFeature
import net.minecraft.world.gen.GenerationStep

class ModStructures {
    fun init() {
        val identifier = Mod.identifier("press_all_blocks")
        val structureFeature = TestStructureFeature()
        StructureFeatureRegisterInvoker.invokeRegister(identifier.toString(), structureFeature, GenerationStep.Feature.SURFACE_STRUCTURES)
    }
}