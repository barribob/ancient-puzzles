package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.mixins.StructureFeatureRegisterInvoker
import com.barribob.ancient_puzzles.structure.TestStructureFeature
import com.barribob.ancient_puzzles.structure.processors.PuzzleProcessor
import net.minecraft.structure.processor.StructureProcessorType
import net.minecraft.world.gen.GenerationStep

class ModStructures {
    val puzzleProcessor: StructureProcessorType<PuzzleProcessor> = StructureProcessorType.register(Mod.identifier("puzzle_processor").toString(), PuzzleProcessor.CODEC)

    fun init() {
        val identifier = Mod.identifier(Mod.puzzles.pressAllBlocks)
        val structureFeature = TestStructureFeature()
        StructureFeatureRegisterInvoker.invokeRegister(identifier.toString(), structureFeature, GenerationStep.Feature.SURFACE_STRUCTURES)
    }
}