package data.repositories

import data.dto.Car
import data.remote.Api
import domain.repositories.RemoteDataRepository

class RemoteDataRepositoryImpl(private val api: Api) : RemoteDataRepository {
    override suspend fun getCars(page: Int): List<Car> = api.getUsers(page)

}

