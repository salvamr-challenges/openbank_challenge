package usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import model.HeroPaging
import repository.MarvelRepository
import javax.inject.Inject
import javax.inject.Named

class GetHeroesPagedUseCase @Inject constructor(
    @Named("UseCase")
    private val dispatcher: CoroutineDispatcher,
    private val marvelRepository: MarvelRepository
) {

    suspend operator fun invoke(page: Int, limit: Int): Result<HeroPaging> = withContext(dispatcher) {
        marvelRepository.getAllCharactersPaged(offset = page, limit = limit)
    }
}