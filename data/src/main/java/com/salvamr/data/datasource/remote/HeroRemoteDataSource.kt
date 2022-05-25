package com.salvamr.data.datasource.remote

import model.HeroPaging


interface HeroRemoteDataSource {

    suspend fun getAllCharactersWithOffset(offset: Int, limit: Int): Result<HeroPaging>
}