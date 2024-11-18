package com.example.newsapp.utility

import androidx.compose.ui.graphics.Color


fun giveCountryString(country: String?): String? {
    val countryString = when (country) {
        "All Country" -> ""

        "United States" -> "us"

        null -> {
            null
        }

        else -> {
            ""
        }
    }
    return countryString

}

fun giveColorFromString(colorString: String?): Color {
    val auraColor = when (colorString) {
        "Blue" -> {
            Color.Blue
        }

        "Red" -> {
            Color.Red
        }

        "Green" -> {
            Color.Green
        }

        "Yellow" -> {
            Color.Yellow
        }

        null -> {
            Color.Blue
        }

        else -> {
            Color.Blue
        }
    }

    return auraColor

}