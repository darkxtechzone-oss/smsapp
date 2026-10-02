package com.darkx.message.core.permissions

import android.content.Intent

/** Abstraction over Android's default-SMS role so it can be faked in tests. */
interface DefaultSmsHandler {
    /** False on devices without telephony or where the SMS role does not exist. */
    fun isRoleAvailable(): Boolean

    fun isDefaultSmsApp(): Boolean

    /** Intent to launch for result, or null when the role cannot be requested. */
    fun createRequestIntent(): Intent?
}
