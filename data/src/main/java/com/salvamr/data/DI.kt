package com.salvamr.data

import com.salvamr.data.datasource.cache.HeroCacheDataSource
import com.salvamr.data.datasource.cache.HeroCacheDataSourceImpl
import com.salvamr.data.datasource.remote.*
import com.salvamr.data.repository.MarvelRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import repository.MarvelRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModuleBinder {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(remoteDataSourceImpl: HeroRemoteDataSourceImpl): HeroRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindMarbelRepository(marvelRepositoryImpl: MarvelRepositoryImpl): MarvelRepository

}

@Module
@InstallIn(SingletonComponent::class)
object DataModuleProvider {

    @Provides
    @Singleton
    fun provideHeroCacheDataSource(): HeroCacheDataSource = HeroCacheDataSourceImpl(mutableMapOf())


    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        val timeStamp = System.currentTimeMillis().toString()
        val toHash = timeStamp + BuildConfig.API_KEY_PRIVATE + BuildConfig.API_KEY
        val hash = md5(toHash)

        val okHttpClient = OkHttpClient()
            .newBuilder()
            .addInterceptor {
                val newUrl = it.request()
                    .url
                    .newBuilder()
                    .addQueryParameter("apikey", BuildConfig.API_KEY)
                    .addQueryParameter("hash", hash)
                    .addQueryParameter("ts", timeStamp)
                    .build()

                val request = it.request().newBuilder().url(newUrl).build()
                it.proceed(request)
            }
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                }
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // src: https://www.kospol.gr/204/create-md5-hashes-in-android/
    private fun md5(s: String): String {
        try {
            // Create MD5 Hash
            val digest = MessageDigest.getInstance("MD5")
            digest.update(s.toByteArray())
            val messageDigest = digest.digest()

            // Create Hex String
            val hexString = StringBuffer()
            for (i in messageDigest.indices) {
                var h = Integer.toHexString(0xFF and messageDigest[i].toInt())
                while (h.length < 2) h = "0$h"
                hexString.append(h)
            }
            return hexString.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }
}
