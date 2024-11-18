package com.example.newsapp.utility

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.newsapp.api.Article


@Composable
fun Modifier.flowingBorder(auraColor: Color): Modifier {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val animatedOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = ""
    )

    val gradientColors = listOf(Color.Transparent, auraColor, Color.Transparent)
    val gradientWidthPx = with(LocalDensity.current) { 16.dp.toPx() }

    return this.drawBehind {
        val animatedStart = size.width * animatedOffset
        val animatedEnd = animatedStart + gradientWidthPx
        val brush = Brush.linearGradient(
            colors = gradientColors,
            start = Offset(animatedStart, 0f),
            end = Offset(animatedEnd, size.height)
        )
        drawRoundRect(
            brush = brush,
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = CornerRadius(30f, 30f),
            style = Stroke(width = 4.dp.toPx())
        )
    }
}

@Composable
fun ArticleItem(
    articles: List<Article>,
    onCardArticleClicked: (url: String) -> Unit
) {

    val listState = rememberLazyListState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState

        ) {

            items(articles)
            { article ->
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onCardArticleClicked(article.url)
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        AsyncImage(
                            model = article.urlToImage
                                ?: "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSUrgu4a7W_OM8LmAuN7Prk8dzWXm7PVB_FmA&s",
                            contentDescription = "Article image",
                            modifier = Modifier
                                .size(80.dp)
                                .aspectRatio(1f),
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) {

                            Text(
                                text = article.title,
                                fontWeight = FontWeight.Bold,
                                maxLines = 3
                            )

                            Text(
                                text = article.source.name,
                                maxLines = 1,
                                fontSize = 14.sp
                            )
                        }

                    }
                }
            }
        }
    }

}

@Composable
fun ContentSeparator() {
    HorizontalDivider(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        thickness = 1.dp,
        color = Color.Gray
    )

}

