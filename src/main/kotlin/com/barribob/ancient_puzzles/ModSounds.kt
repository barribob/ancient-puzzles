package com.barribob.ancient_puzzles

import net.minecraft.sound.SoundEvent
import net.minecraft.util.registry.Registry

class ModSounds {
    val chestAppear = newSound("chest_appear")
    val stoneBrickChestLocked = newSound("stone_brick_chest_locked")

    private fun newSound(id: String): SoundEvent {
        val identifier = Mod.identifier(id)
        val soundEvent = SoundEvent(identifier)
        return Registry.register(Registry.SOUND_EVENT, identifier, soundEvent)
    }
}