package com.example.newsapp.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.utility.ContentSeparator
import com.example.newsapp.utility.categoriesList
import com.example.newsapp.utility.flowingBorder
import com.example.newsapp.utility.giveColorFromString
import com.example.newsapp.utility.giveCountryString
import com.example.newsapp.viewmodels.NewsViewModel

@Composable
fun HeadlinesScreen(
    newsViewModel: NewsViewModel,
    onClick: (url: String) -> Unit
) {

    val newsResultAtTopHeadlines = newsViewModel.newsDataAtTopHeadlines.observeAsState()

    val auraColor by newsViewModel.auraColorSet.collectAsState()
    val language by newsViewModel.languageSet.collectAsState()
    val sortOrderHeadlines by newsViewModel.sortByForHeadlinesSet.collectAsState()
    val country by newsViewModel.countrySet.collectAsState()

    var currentSettingsHash by rememberSaveable { mutableIntStateOf(0) }

    // Generate a hash based on the current settings values
    val newSettingsHash = listOf(country, language, sortOrderHeadlines).hashCode()

    var selectedCategory by rememberSaveable { mutableStateOf("General") }

    LaunchedEffect(newSettingsHash) {

        if (language != null && sortOrderHeadlines != null && country != null && currentSettingsHash != newSettingsHash) {
            newsViewModel.getNewsTopHeadlines(
                giveCountryString(country) ?: "",
                selectedCategory,
                sortOrderHeadlines!!,
                language!!.replace("English", "en")
            )


        }
        currentSettingsHash = newSettingsHash


    }

    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {

        CategoriesBar(
            newsViewModel = newsViewModel,
            giveColorFromString(auraColor),
            giveCountryString(country) ?: "",
            sortOrderHeadlines ?: "Popularity",
            language ?: "en",
            selectedCategory
        ) {
            selectedCategory = it
        }

        ContentSeparator()

        ShowResult(result = newsResultAtTopHeadlines.value)
        { url ->
            onClick(url)
        }
    }

}

@Composable
fun CategoriesBar(
    newsViewModel: NewsViewModel,
    color: Color,
    countryString: String,
    sortOrder: String,
    language: String,
    selectedCategory: String?,
    onClickCategory: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {

        categoriesList.forEach { category ->

            FlowingBorderButton(
                onClick = {
                    onClickCategory(category)
                    newsViewModel.getNewsTopHeadlines(
                        countryString,
                        category,
                        sortOrder,
                        language.replace("English", "en")
                    )
                },
                categoryText = category,
                isSelected = category == selectedCategory,
                color
            )

        }


    }
}


@Composable
fun FlowingBorderButton(
    onClick: () -> Unit,
    categoryText: String,
    isSelected: Boolean,
    auraColor: Color
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.1f else 1.0f, label = "")

    // Apply padding to prevent overlap when button is scaled
    Box(
        modifier = Modifier
            .padding(6.dp) // Add padding around each button
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale, // Slightly larger scale for selected button
                    scaleY = scale
                )
                .then(
                    if (isSelected) Modifier.flowingBorder(auraColor) else Modifier
                ),
            content = {
                Text(
                    text = categoryText,
                    fontSize = if (isSelected) 18.sp else 14.sp // Larger font for selected button
                )
            }
        )
    }
}






