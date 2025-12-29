package dev.rakamin.newsapp.network

import dev.rakamin.newsapp.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsInterface {

    @GET("top-headlines")
    fun getHeadlines(
        @Query("country") country: String = "id",
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsResponse>

    @GET("everything")
    fun getEverything(
        @Query("q") query: String = "indonesia",
        @Query("language") language: String = "id",
        @Query("page") page: Int = 1,
        @Query("apiKey") apiKey: String = API_KEY
    ): Call<NewsResponse>

    companion object {
        const val BASE_URL = "https://newsapi.org/v2/"
        const val API_KEY = "2472e3bc4c804d93a9cc6515b074a6da"
    }
}