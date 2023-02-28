package org.fromheart.nightcrawler.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round,
                strokeWidth = 10.dp,
                modifier = Modifier.size(100.dp)
            )
        }
    }
}