package com.darkx.message

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.darkx.message.ui.navigation.DarkXNavHost
import com.darkx.message.ui.navigation.Routes
import com.darkx.message.ui.theme.DarkXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val container = (application as DarkXApp).container
        val startDestination =
            if (container.preferences.isOnboardingCompleted) Routes.HOME else Routes.ONBOARDING

        setContent {
            val themeMode by container.preferences.themeMode.collectAsStateWithLifecycle()
            DarkXTheme(themeMode = themeMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    DarkXNavHost(startDestination = startDestination)
                }
            }
        }
    }
}
