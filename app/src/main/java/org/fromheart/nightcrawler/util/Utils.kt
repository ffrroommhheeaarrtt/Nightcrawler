package org.fromheart.nightcrawler.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import org.fromheart.nightcrawler.R
import kotlin.math.roundToInt

fun formatRatingCount(count: Int): String {
    return if (count >= 1000) {
        "${count.toDouble().div(1000.0).roundToInt()}K"
    } else "$count"
}

@Composable
fun formatMovieDuration(duration: Int): String {
    val hour = stringResource(R.string.hour_abb)
    val minute = stringResource(R.string.minute_abb)

    return when {
        duration < 60 -> "$duration$minute"
        duration % 60 == 0 -> "${duration / 60}$hour"
        else -> "${duration / 60}$hour ${duration % 60}$minute"
    }
}

@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isImeVisible)
}