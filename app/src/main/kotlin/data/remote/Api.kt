package data.remote

import data.dto.Car
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Api {

    @GET("/")
    suspend fun getUsers(@Query("page") page: Int, @Query("per_page") perPage: Int = 10): List<Car>


    companion object {
        private var retrofitService: Api? = null
        fun getInstance(): Api {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://127.0.0.1:5000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(Api::class.java)
            }
            return retrofitService!!
        }

    }

}
