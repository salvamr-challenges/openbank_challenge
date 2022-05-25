package com.salvamr.presentation.ui.list

import com.salvamr.presentation.ui.list.model.HeroVo

sealed class HeroListFragmentState {
    object Loading : HeroListFragmentState()
    object HeroesRetrieved : HeroListFragmentState() {
        val values: MutableList<HeroVo> = mutableListOf()
    }
    class Error(val throwable: Throwable) : HeroListFragmentState()
}