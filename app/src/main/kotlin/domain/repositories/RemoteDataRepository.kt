package domain.repositories

import data.dto.Car

interface RemoteDataRepository {
    suspend fun getCars(page: Int): List<Car>

}

