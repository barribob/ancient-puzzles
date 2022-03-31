package com.barribob.ancient_puzzles

import com.barribob.ancient_puzzles.puzzle_manager.reward_event.DebugRewardEvent
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardEventFactory
import com.barribob.ancient_puzzles.puzzle_manager.reward_event.RewardType

class ModRewards {
    val rewardFactory = RewardEventFactory()
    val debugRewardEvent = RewardType<DebugRewardEvent>("debug_reward_event")
    fun init() {
       rewardFactory.register(debugRewardEvent, ::DebugRewardEvent)
    }
}