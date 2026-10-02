package com.darkx.message

import android.app.Application
import com.darkx.message.core.AppContainer

class DarkXApp : Application() {
    val container: AppContainer by lazy { AppContainer(this) }
}
