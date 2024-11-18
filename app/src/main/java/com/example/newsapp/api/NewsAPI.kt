package com.example.newsapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("/v2/everything")
    suspend fun  getNewsEverything(
        @Query("q") query : String,
        @Query("apikey") key : String,
        @Query("sortBy") sort : String,
        @Query("language") language: String


    ) : Response<NewsModel>

    @GET("/v2/top-headlines")
    suspend fun  getNewsTopHeadlines(
        @Query("country") c : String,
        @Query("category") category : String,
        @Query("apikey") key : String,
        @Query("sortBy") sort : String,
        @Query("language") language: String

    ) : Response<NewsModel>

}