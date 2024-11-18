package com.example.newsapp.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.NetworkResponse
import com.example.newsapp.api.NewsModel
import com.example.newsapp.repository.NewsRepository
import com.example.newsapp.screen.getSettingsDataFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository,
    private val context: Application
) : ViewModel() {


    private val _themeSet = MutableStateFlow("")
    val themeSet: StateFlow<String> = _themeSet

    private val _languageSet = MutableStateFlow<String?>(null)
    val languageSet: StateFlow<String?> = _languageSet

    private val _countrySet = MutableStateFlow<String?>(null)
    val countrySet: StateFlow<String?> = _countrySet

    private val _auraColorSet = MutableStateFlow<String?>(null)
    val auraColorSet: StateFlow<String?> = _auraColorSet

    private val _sortByForSearchedNewsSet = MutableStateFlow<String?>(null)
    val sortByForSearchedNewsSet: StateFlow<String?> = _sortByForSearchedNewsSet

    private val _sortByForHeadlinesSet = MutableStateFlow<String?>(null)
    val sortByForHeadlinesSet: StateFlow<String?> = _sortByForHeadlinesSet

    val newsDataAtEverything: LiveData<NetworkResponse<NewsModel>>
        get() = newsRepository.newsDataAtEverything

    val newsDataAtTopHeadlines: LiveData<NetworkResponse<NewsModel>>
        get() = newsRepository.newsDataAtTopHeadlines

    init {


        viewModelScope.launch {

            // couldn't retrieve all the shared preference data at once in a single coroutine so using
            // parallel coroutine for that problem like below

            launch {
                getSettingsDataFlow(context, "Theme").collect { theme ->
                    _themeSet.value = theme
                }
            }


            launch {
                getSettingsDataFlow(
                    context,
                    "Country"
                ).collect { country -> _countrySet.value = country }
            }

            launch {
                getSettingsDataFlow(
                    context,
                    "Language"
                ).collect { language -> _languageSet.value = language }
            }

            launch {
                getSettingsDataFlow(
                    context,
                    "Aura"
                ).collect { auraColor -> _auraColorSet.value = auraColor }
            }

            launch {
                getSettingsDataFlow(
                    context,
                    "Sort headlines"
                ).collect { sortBy -> _sortByForHeadlinesSet.value = sortBy }
            }

            launch {
                getSettingsDataFlow(
                    context,
                    "Sort searched news"
                ).collect { sortBy -> _sortByForSearchedNewsSet.value = sortBy }
            }


        }


    }


    fun getNewsEverything(query: String, sort: String, language: String) = viewModelScope.launch {

        newsRepository.fetchEverythingNews(query, sort, language)
    }

    fun getNewsTopHeadlines(
        country: String,
        category: String,
        sort: String,
        language: String
    ) = viewModelScope.launch {

        newsRepository.fetchTopHeadlinesNews(country, category, sort, language)
    }

}