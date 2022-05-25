package model

data class HeroPaging(
    val offset: Int,
    val limit: Int,
    val total: Int,
    val results: List<Hero>
)