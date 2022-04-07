package com.barribob.ancient_puzzles

import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

class ModSounds {
    private val soundIdMap = mutableMapOf<SoundEvent, Identifier>()
    val chestAppear = newSound("chest_appear")

    fun init() {
        registerSound(chestAppear)
    }

    private fun registerSound(event: SoundEvent) {
        Registry.register(Registry.SOUND_EVENT, soundIdMap[event], event)
    }

    private fun newSound(id: String): SoundEvent {
        val identifier = Mod.identifier(id)
        val soundEvent = SoundEvent(identifier)
        soundIdMap[soundEvent] = identifier
        return soundEvent
    }
}