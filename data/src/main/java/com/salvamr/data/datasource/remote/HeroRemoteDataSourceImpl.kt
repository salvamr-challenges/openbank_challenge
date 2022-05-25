package com.salvamr.data.datasource.remote

import model.HeroPaging
import javax.inject.Inject

class HeroRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : HeroRemoteDataSource {

    override suspend fun getAllCharactersWithOffset(offset: Int, limit: Int): Result<HeroPaging> {
        return try {
            val result = apiService.getAllCharactersWithOffset(offset, limit).toDomain()
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}