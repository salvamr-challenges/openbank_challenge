package com.salvamr.data.datasource.cache

import com.salvamr.data.datasource.HeroMock
import io.mockk.junit4.MockKRule
import model.Hero
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class HeroCacheDataSourceImplTest {
    @get:Rule
    val mockkRule = MockKRule(this)

    private val cache = mutableMapOf<Int, Hero>()

    private val subject = HeroCacheDataSourceImpl(cache)

    @Before
    fun setUp() {
        cache.clear()
    }

    @Test
    fun getHeroById_Success() {
        // Arrange
        val hero = HeroMock.provideHero()
        subject.saveHero(hero)

        // Act
        val result = subject.getHeroById(hero.id)

        // Assert
        assert(result.isSuccess)
        assert(result.getOrNull()!!.id == hero.id)
    }

    @Test
    fun getHeroById_Failure() {
        // Arrange
        val hero = HeroMock.provideHero()

        // Act
        val result = subject.getHeroById(hero.id)

        // Assert
        assert(result.isFailure)
        assert(result.getOrNull() == null)
    }

    @Test
    fun clear() {
        // Arrange
        val hero = HeroMock.provideHero()

        // Act
        subject.saveHero(hero)

        // Assert
        val cachedHero = cache[hero.id]
        assert(cachedHero != null)
        assert(cachedHero!!.id == hero.id)
    }
}