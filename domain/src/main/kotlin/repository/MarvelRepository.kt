package repository

import model.Hero
import model.HeroPaging

interface MarvelRepository {
    suspend fun getAllCharactersPaged(offset: Int, limit: Int): Result<HeroPaging>
    suspend fun getCharacterById(characterId: Int): Result<Hero>
    suspend fun deleteCache()
}