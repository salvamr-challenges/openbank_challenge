package com.salvamr.data.datasource.cache

import errors.Error
import model.Hero
import javax.inject.Inject

class HeroCacheDataSourceImpl @Inject constructor(
    private val cache: MutableMap<Int, Hero>
) : HeroCacheDataSource {

    override fun getHeroById(id: Int): Result<Hero> {
        return cache[id]?.let { Result.success(it) } ?: Result.failure(Error.HeroNotFound())
    }

    override fun saveHero(hero: Hero) {
        cache[hero.id] = hero
    }

    override fun clear() {
        cache.clear()
    }
}