package com.salvamr.data.repository

import com.salvamr.data.datasource.HeroMock
import com.salvamr.data.datasource.cache.HeroCacheDataSource
import com.salvamr.data.datasource.remote.HeroRemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MarvelRepositoryImplTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val cacheDataSource: HeroCacheDataSource = mockk()
    private val remoteDataSource: HeroRemoteDataSource = mockk()

    private val subject = MarvelRepositoryImpl(
        cacheDataSource = cacheDataSource,
        remoteDataSource = remoteDataSource
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `given remote returning a throwable repository should return a Failure`() = runTest {
        launch(Dispatchers.Main) {
            // Arrange
            val offset = 0
            val limit = 20
            val throwable = Throwable()
            coEvery {
                remoteDataSource.getAllCharactersWithOffset(
                    offset,
                    limit
                )
            } returns Result.failure(throwable)

            // Act
            val result = subject.getAllCharactersPaged(offset, limit)

            // Assert
            assert(result.isFailure)
            assert(result.exceptionOrNull() != null)
            assert(result.exceptionOrNull() == throwable)
        }
    }

    @Test
    fun `given remote returning a result repository should return a Success`() = runTest {
        launch(Dispatchers.Main) {
            // Arrange
            val offset = 0
            val limit = 20
            val heroRemote = HeroMock.provideHeroRemoteDo(offset, limit)
            coEvery {
                remoteDataSource.getAllCharactersWithOffset(
                    offset,
                    limit
                )
            } returns Result.success(heroRemote)
            coEvery {
                cacheDataSource.saveHero(any())
            } returns Unit

            // Act
            val result = subject.getAllCharactersPaged(offset, limit)

            // Assert
            assert(result.isSuccess)
            assert(result.getOrNull() != null)
            coVerify(atLeast = heroRemote.data.results.size) { cacheDataSource.saveHero(any()) }
        }


    }
}