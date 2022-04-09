package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.puzzle_manager.reward_event.AncientChestRewardEvent
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardEventFactory
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardType

class ModRewards {
    val rewardFactory = RewardEventFactory()
    val ancientChestRewardEvent = RewardType<AncientChestRewardEvent>("ancient_chest_reward_event")
    fun init() {
        rewardFactory.register(ancientChestRewardEvent, ::AncientChestRewardEvent, ::AncientChestRewardEvent)
    }
}