package com.salvamr.data.datasource.cache

import model.Hero

interface HeroCacheDataSource {

    fun getHeroById(id: Int): Result<Hero>
    fun saveHero(hero: Hero)
    fun clear()
}