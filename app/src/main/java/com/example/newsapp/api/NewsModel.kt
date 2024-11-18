package com.example.newsapp.api

data class NewsModel(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)