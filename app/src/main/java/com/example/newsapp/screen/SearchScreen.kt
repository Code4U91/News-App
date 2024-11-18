package com.example.newsapp.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.newsapp.api.NetworkResponse
import com.example.newsapp.api.NewsModel
import com.example.newsapp.utility.ArticleItem
import com.example.newsapp.utility.flowingBorder
import com.example.newsapp.utility.giveColorFromString
import com.example.newsapp.viewmodels.NewsViewModel


@Composable
fun SearchScreen(
    newsViewModel: NewsViewModel,
    onClick: (url: String) -> Unit

) {

    val auraColor by newsViewModel.auraColorSet.collectAsState()

    val language by newsViewModel.languageSet.collectAsState()
    val sortOrderSearch by newsViewModel.sortByForSearchedNewsSet.collectAsState()

    val newsResultAtEverything = newsViewModel.newsDataAtEverything.observeAsState()

    var searchQuery by rememberSaveable { mutableStateOf<String?>(null) }
    var currentSettingsHash by rememberSaveable { mutableIntStateOf(0) }
    val newSettingsHash = listOf(language, sortOrderSearch).hashCode()

    LaunchedEffect(language, sortOrderSearch) {

        if (language != null && sortOrderSearch != null && currentSettingsHash != newSettingsHash) {
            newsViewModel.getNewsEverything(
                searchQuery ?: "India",
                sortOrderSearch ?: "Relevancy",
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

        FlowingBorderSearchBar(newsViewModel, searchQuery ?: "", giveColorFromString(auraColor))
        {
            searchQuery = it
        }

        ShowResult(newsResultAtEverything.value)
        { url ->
            onClick(url)
        }

    }

}

@Composable
fun ShowResult(
    result: NetworkResponse<NewsModel>?,
    onCardClick: (url: String) -> Unit
) {
    when (result) {
        is NetworkResponse.Error -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Network Error",
                    fontSize = 34.sp,
                    fontStyle = FontStyle.Italic,
                )
            }
        }

        NetworkResponse.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        }

        is NetworkResponse.Success -> {

            if (result.data.totalResults != 0) {


                ArticleItem(articles = result.data.articles)
                { url ->
                    onCardClick(url)
                }

            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No result found",
                        fontSize = 34.sp,
                        fontStyle = FontStyle.Italic
                    )
                }

            }

        }

        null -> {}
    }
}


@Composable
fun FlowingBorderSearchBar(
    newsViewModel: NewsViewModel,
    searchQuery: String,
    auraColor: Color,
    onClickQuery: (query: String) -> Unit

) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .flowingBorder(auraColor)
    ) {
        SearchBar(newsViewModel = newsViewModel, searchQuery = searchQuery) {
            onClickQuery(it)
        }
    }
}


@Composable
fun SearchBar(
    newsViewModel: NewsViewModel,
    searchQuery: String,
    onClickSearchQuery: (query: String) -> Unit
) {


    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = searchQuery,
        onValueChange = { onClickSearchQuery(it) },
        placeholder = { Text("Search for news") },
        trailingIcon = {
            IconButton(onClick = {
                if (searchQuery.isNotEmpty()) {
                    newsViewModel.getNewsEverything(searchQuery, "relevancy", "en")
                }
                keyboardController?.hide()
            }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search icon")
            }
        },
        shape = RoundedCornerShape(30.dp),
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {

                if (searchQuery.isNotEmpty()) {
                    newsViewModel.getNewsEverything(searchQuery, "relevancy", "en")
                }

                keyboardController?.hide()
            }
        )
    )
}







