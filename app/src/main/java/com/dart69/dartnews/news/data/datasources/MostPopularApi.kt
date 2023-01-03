package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.entities.NytResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Api can load only 20 articles for given period.
 * Period must be exactly 1, 7 or 30 days.
 * */
private const val BASE_ROUTE = "/svc/mostpopular/v2"

interface MostPopularApi {

    @GET("$BASE_ROUTE/viewed/{period}.json")
    suspend fun loadMostViewed(
        @Path("period") period: Int,
        @Query("api-key") apiKey: String
    ): Response<NytResponse>

    @GET("$BASE_ROUTE/shared/{period}.json")
    suspend fun loadMostShared(
        @Path("period") period: Int,
        @Query("api-key") apiKey: String
    ): Response<NytResponse>

    @GET("$BASE_ROUTE/emailed/{period}.json")
    suspend fun loadMostEmailed(
        @Path("period") period: Int,
        @Query("api-key") apiKey: String
    ): Response<NytResponse>
}