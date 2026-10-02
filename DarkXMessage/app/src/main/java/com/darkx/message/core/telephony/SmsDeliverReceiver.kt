package com.darkx.message.core.telephony

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Declared so Android lets the app qualify for the default SMS role.
 *
 * NOT IMPLEMENTED YET: parsing multipart PDUs and writing to the SMS provider
 * arrives in Part 2. Until then incoming texts are not stored if this app is default.
 */
class SmsDeliverReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = Unit
}
