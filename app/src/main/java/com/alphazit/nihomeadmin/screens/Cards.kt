package com.alphazit.nihomeadmin.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SwappableCardStack() {
    // List of card content
    val cardContents = listOf("Card 1", "Card 2", "Card 3")
    val cardColors = listOf(Color.Gray, Color.DarkGray, Color.Black)
    var currentTopCard by remember { mutableIntStateOf(0) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    var shouldSwap by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    // Animate the offset for smoother transitions
    val animatedOffsetY by animateDpAsState(targetValue = if (shouldSwap) 180.dp else offsetY.dp)

    fun swapCards() {
        shouldSwap = true
        coroutineScope.launch {
            // Delay to let the animation play out
            delay(300)
            currentTopCard = (currentTopCard + 1) % cardContents.size
            offsetY = 0f
            shouldSwap = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Loop from bottom to top to draw cards in the correct order
        for (i in 2 downTo 0) {
            val cardIndex = (currentTopCard + i) % cardContents.size
            val isTopCard = i == 0

            Card(
                modifier = Modifier
                    .align(Alignment.Center)
                    .width(if(isTopCard) 340.dp else (340-(20 * i)).dp)
                    .height(200.dp)
                    .offset(y = if (isTopCard) animatedOffsetY else (15 * i).dp)
                    .pointerInput(Unit) {
                        if (isTopCard) {
                            detectVerticalDragGestures(
                                onVerticalDrag = { change, dragAmount ->
                                    if (!shouldSwap) {
                                        offsetY += dragAmount
                                        change.consume()
                                        if (offsetY > 150) {
                                            swapCards()
                                        } else if (offsetY < -150) {
                                            offsetY = 0f
                                        }
                                    }
                                },
                                onDragEnd = {
                                    if (offsetY <= 180f) {
                                        offsetY = 0f
                                    }
                                }
                            )
                        }
                    }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(cardColors[cardIndex]),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cardContents[cardIndex],
                        fontSize = 24.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}