package com.barribob.ancient_puzzles.blocks

import net.minecraft.block.PressurePlateBlock

class TimedPressurePlate(type: ActivationRule?, settings: Settings?) : PressurePlateBlock(type, settings) {
    override fun getTickRate(): Int = 200
}