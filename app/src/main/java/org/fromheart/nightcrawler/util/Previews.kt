package org.fromheart.nightcrawler.util

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "dark theme", group = "themes", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "light theme", group = "themes")
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.FUNCTION)
annotation class ThemePreview