package it.uniroma2.RunTeam.Sudoku


import android.content.Context
import android.content.res.Configuration
import android.os.LocaleList
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.util.*

val LocalAppLocale = compositionLocalOf { mutableStateOf(Locale("en")) }

fun updateLocale(context: Context, locale: Locale): Context {
    Locale.setDefault(locale)
    val config = Configuration(context.resources.configuration)
    config.setLocales(LocaleList(locale))
    return context.createConfigurationContext(config)
}
