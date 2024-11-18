package com.example.newsapp.screen

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.example.newsapp.utility.ContentSeparator
import com.example.newsapp.utility.countries
import com.example.newsapp.utility.sortBy
import com.example.newsapp.utility.theme
import com.example.newsapp.viewmodels.NewsViewModel
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(
    newsViewModel: NewsViewModel
) {

    val selectedTheme by newsViewModel.themeSet.collectAsState()

    val selectedAuraColor by newsViewModel.auraColorSet.collectAsState()

    val selectedLanguage by newsViewModel.languageSet.collectAsState()
    val selectedCountry by newsViewModel.countrySet.collectAsState()
    val selectedSortByForHeadlines by newsViewModel.sortByForHeadlinesSet.collectAsState()
    val selectedSortByForSearchedNews by newsViewModel.sortByForSearchedNewsSet.collectAsState()


    val context = LocalContext.current

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    )
    {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "News App",
                fontSize = 50.sp,
                color = Color.Red,
                fontFamily = FontFamily.Serif,
                textDecoration = TextDecoration.Underline
            )

            ContentSeparator()

            SettingsCategory(category = "General")

            SettingItem(
                label = "Country",
                selectedOption = selectedCountry!!,
                options = countries,
                context = context
            )

            SettingItem(
                label = "Language",
                selectedOption = selectedLanguage!!,
                options = com.example.newsapp.utility.language,
                context = context
            )

            SettingItem(
                label = "Sort searched news",
                selectedOption = selectedSortByForSearchedNews!!,
                options = sortBy,
                context = context
            )

            SettingItem(
                label = "Sort headlines",
                selectedOption = selectedSortByForHeadlines!!,
                options = sortBy,
                context = context
            )

            ContentSeparator()

            SettingsCategory(category = "Appearance")

            SettingItem(
                label = "Theme",
                selectedOption = selectedTheme,
                options = theme,
                context = context

            )

            SettingItem(
                label = "Aura",
                selectedOption = selectedAuraColor!!,
                options = com.example.newsapp.utility.auraColor,
                context = context
            )

        }
    }
}

@Composable
fun SettingsCategory(category: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            text = category,
            fontSize = 15.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SettingItem(
    label: String,
    selectedOption: String,
    options: List<String>,
    context: Context
) {
    val coroutineScope = rememberCoroutineScope()

    var expanded by remember { mutableStateOf(false) }

    if (expanded) {
        BackHandler {
            expanded = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
        )

        Text(
            text = selectedOption,
            fontSize = 15.sp,
            color = Color.Gray
        )
    }

    if (expanded) {
        Popup(
            alignment = Alignment.Center,
            onDismissRequest = {
                expanded = false
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
                    .clickable { expanded = false }
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        options.forEach { option ->
                            Text(
                                text = option,
                                fontSize = 16.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable {

                                        if (selectedOption != option) {
                                            coroutineScope.launch {
                                                saveSelectedSettingOption(context, option, label)

                                            }
                                        }
                                        expanded = false
                                    }
                            )
                        }
                    }

                }
            }

        }
    }

}

