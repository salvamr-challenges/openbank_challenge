package com.salvamr.data.repository

import com.salvamr.data.datasource.cache.HeroCacheDataSource
import com.salvamr.data.datasource.remote.HeroRemoteDataSource
import model.Hero
import model.HeroPaging
import repository.MarvelRepository
import javax.inject.Inject
import javax.inject.Singleton

class MarvelRepositoryImpl @Inject constructor(
    private val remoteDataSource: HeroRemoteDataSource,
    private val cacheDataSource: HeroCacheDataSource,
) : MarvelRepository {

    override suspend fun getAllCharactersPaged(offset: Int, limit: Int): Result<HeroPaging> {
        val result = remoteDataSource.getAllCharactersWithOffset(offset, limit).getOrElse {
            return Result.failure(it)
        }.also { heroPaging ->
            heroPaging.results.forEach {
                cacheDataSource.saveHero(it)
            }
        }

        return Result.success(result)
    }

    override suspend fun getCharacterById(characterId: Int): Result<Hero> {
        return cacheDataSource.getHeroById(characterId)
    }

    override suspend fun deleteCache() {
        cacheDataSource.clear()
    }
}