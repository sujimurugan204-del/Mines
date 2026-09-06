package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = PrimaryFixed,
  onPrimary = OnPrimaryFixed,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = SecondaryFixed,
  onSecondary = OnSecondaryFixed,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiary = TertiaryFixed,
  onTertiary = Tertiary,
  background = OnSurface,
  surface = OnSurface,
  onBackground = Surface,
  onSurface = Surface,
  error = ErrorColor,
  errorContainer = ErrorContainer,
  onError = OnError,
  onErrorContainer = OnErrorContainer,
  outline = Outline,
  outlineVariant = OutlineVariant
)

private val LightColorScheme = lightColorScheme(
  primary = Primary,
  onPrimary = OnPrimary,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  secondary = Secondary,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiary = Tertiary,
  onTertiary = OnTertiary,
  tertiaryContainer = TertiaryContainer,
  onTertiaryContainer = OnTertiaryContainer,
  background = Surface,
  onBackground = OnSurface,
  surface = Surface,
  onSurface = OnSurface,
  surfaceVariant = SurfaceContainerHighest,
  onSurfaceVariant = OnSurfaceVariant,
  error = ErrorColor,
  errorContainer = ErrorContainer,
  onError = OnError,
  onErrorContainer = OnErrorContainer,
  outline = Outline,
  outlineVariant = OutlineVariant
)

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Preserve brand identity
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

