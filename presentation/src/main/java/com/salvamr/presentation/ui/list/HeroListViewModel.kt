package com.salvamr.presentation.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salvamr.presentation.ui.list.model.HeroVo
import com.salvamr.presentation.ui.list.model.toVo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import usecase.GetHeroByIdUseCase
import usecase.GetHeroesPagedUseCase
import javax.inject.Inject
import javax.inject.Named

private const val PAGE_LIMIT = 20

@HiltViewModel
class HeroListViewModel @Inject constructor(
    @Named("ViewModel")
    private val dispatcher: CoroutineDispatcher,
    private val getHeroesPagedUseCase: GetHeroesPagedUseCase,
    private val getHeroByIdUseCase: GetHeroByIdUseCase
) : ViewModel() {

    private val _heroList: MutableStateFlow<HeroListFragmentState> =
        MutableStateFlow(HeroListFragmentState.Loading)
    val heroList = _heroList.asStateFlow()

    private var page = 0

    init {
        getHeroes()
    }

    suspend fun getHeroById(id: Int): HeroVo? {
        val results = getHeroByIdUseCase(id).getOrElse {
            _heroList.emit(HeroListFragmentState.Error(it))
            null
        }

        return results?.toVo()
    }

    fun getHeroes() = viewModelScope.launch(dispatcher) {
        _heroList.emit(HeroListFragmentState.Loading)

        val results = getHeroesPagedUseCase(page, PAGE_LIMIT).getOrElse {
            _heroList.emit(HeroListFragmentState.Error(it))
            return@launch
        }

        val resultsToVo = results.results.map { it.toVo() }

        _heroList.emit(HeroListFragmentState.HeroesRetrieved.apply { values.addAll(resultsToVo) })
    }

    fun incrementPage() {
        page += PAGE_LIMIT
    }
}