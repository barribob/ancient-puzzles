package com.barribob.ancient_puzzles
import net.barribob.maelstrom.MaelstromMod
import net.fabricmc.api.ModInitializer

@Suppress("UNUSED")
object AncientPuzzles: ModInitializer {
    override fun onInitialize() {
        Mod.blocks.init()
        Mod.features.init()
        Mod.structures.init()

        if(MaelstromMod.isDevelopmentEnvironment) TestCommands()
    }
}