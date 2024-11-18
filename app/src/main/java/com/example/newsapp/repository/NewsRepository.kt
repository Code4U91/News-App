package com.example.newsapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.newsapp.api.Constant
import com.example.newsapp.api.NetworkResponse
import com.example.newsapp.api.NewsAPI
import com.example.newsapp.api.NewsModel
import javax.inject.Inject

class NewsRepository @Inject constructor(private val newsAPI: NewsAPI) {

    private val _newsDataAtEverything = MutableLiveData<NetworkResponse<NewsModel>>()
    val newsDataAtEverything: LiveData<NetworkResponse<NewsModel>>
        get() = _newsDataAtEverything

    private val _newsDataAtTopHeadlines = MutableLiveData<NetworkResponse<NewsModel>>()
    val newsDataAtTopHeadlines: LiveData<NetworkResponse<NewsModel>>
        get() = _newsDataAtTopHeadlines


    // For fetching Everything or searching for a topic on vast collection of news

    suspend fun fetchEverythingNews(query: String, sortBy: String, language: String) {
        _newsDataAtEverything.value = NetworkResponse.Loading

        try {

            val response = newsAPI.getNewsEverything(query, Constant.apiKey, sortBy, language)



            if (response.isSuccessful && response.body() != null) {
                _newsDataAtEverything.value = NetworkResponse.Success(response.body()!!)
            } else {
                _newsDataAtEverything.value = NetworkResponse.Error("Failed to fetch the News")
            }

        } catch (e: Exception) {
            _newsDataAtEverything.value =
                NetworkResponse.Error("Failed to fetch data due to exception error")
        }
    }


    // For fetching top headlines on selected categories

    suspend fun fetchTopHeadlinesNews(
        country: String,
        category: String,
        sortBy: String,
        language: String
    ) {
        _newsDataAtTopHeadlines.value = NetworkResponse.Loading

        try {

            val response =
                newsAPI.getNewsTopHeadlines(country, category, Constant.apiKey, sortBy, language)



            if (response.isSuccessful && response.body() != null) {
                _newsDataAtTopHeadlines.value = NetworkResponse.Success(response.body()!!)
            } else {
                _newsDataAtTopHeadlines.value = NetworkResponse.Error("Failed to fetch the News")
            }

        } catch (e: Exception) {
            _newsDataAtTopHeadlines.value =
                NetworkResponse.Error("Failed to fetch data due to exception error")
        }
    }
}