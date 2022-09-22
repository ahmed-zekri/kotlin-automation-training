package domain.use_cases

import data.wrapper_classes.Result
import domain.repositories.RemoteDataRepository
import kotlinx.coroutines.flow.flow

class GetCarsUseCase(private val remoteDataRepository: RemoteDataRepository) {
    suspend operator fun invoke(page: Int = 1) = flow {

        emit(Result.Loading())
        try {
            val list=remoteDataRepository.getCars(page = page)
            emit(Result.Success(data=list))
        } catch (e: Exception) {

            emit(Result.Error(e.message))

        }


    }
}