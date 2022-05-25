package com.salvamr.data.datasource

import com.salvamr.data.datasource.remote.model.*
import model.Hero
import model.HeroPaging

object HeroMock {

    fun provideHero(id: Int = 123) = Hero(
        id = id,
        name = "Spiderman",
        description = "The Amazing Spiderman",
        thumbnail = "spidey.jpg"
    )

    fun provideHeroRemoteDo(offset: Int, limit: Int) = HeroRemoteDo(
        code = 200,
        status = "Ok",
        copyright = "Copyright",
        attributionText = "attributionText",
        attributionHTML = "attributionHtml",
        data = Data(
            offset = offset,
            limit = limit,
            total = 100,
            results = (0 until limit).map {
                Results(
                    id = it,
                    name = "sample",
                    description = "description",
                    modified = "20/10/2022",
                    resourceURI = "http://marvel.com",
                    urls = emptyList(),
                    thumbnail = Thumbnail("marvel.com", "png"),
                    comics = Comics(
                        available = 2 + it,
                        returned = 0,
                        collectionURI = "collectionUri",
                        items = emptyList()
                    ),
                    stories = Stories(
                        available = 3 + it,
                        returned = 0,
                        collectionURI = "collectionUri",
                        items = emptyList()
                    ),
                    events = Events(
                        available = 4 + it,
                        returned = 0,
                        collectionURI = "collectionUri",
                        items = emptyList()
                    ),
                    series = Series(
                        available = 5 + it,
                        returned = 0,
                        collectionURI = "collectionUri",
                        items = emptyList()
                    )
                )
            },
            count = 100
        ),
        etag = "etag"
    )

    fun provideHeroPaging(offset: Int, limit: Int) = HeroPaging(
        offset = offset,
        limit = limit,
        total = 100,
        results = (0 until limit).map { provideHero(it) }
    )
}