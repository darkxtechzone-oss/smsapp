package com.darkx.message.core

import android.content.Context
import com.darkx.message.core.permissions.AndroidDefaultSmsHandler
import com.darkx.message.core.permissions.DefaultSmsHandler
import com.darkx.message.data.preferences.AppPreferences

/** Manual dependency container; swap for Hilt/Koin later if the graph grows. */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext

    val preferences: AppPreferences by lazy { AppPreferences(appContext) }
    val defaultSmsHandler: DefaultSmsHandler by lazy { AndroidDefaultSmsHandler(appContext) }
}
