package usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import model.Hero
import repository.MarvelRepository
import javax.inject.Inject
import javax.inject.Named

class GetHeroByIdUseCase @Inject constructor(
    @Named("UseCase")
    private val dispatcher: CoroutineDispatcher,
    private val marvelRepository: MarvelRepository
) {

    suspend operator fun invoke(id: Int): Result<Hero> = withContext(dispatcher) {
        marvelRepository.getCharacterById(id)
    }
}