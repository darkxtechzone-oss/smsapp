package com.darkx.message.core.telephony

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Declared for default SMS role eligibility.
 *
 * NOT IMPLEMENTED YET: MMS download/storage arrives with the MMS phase.
 */
class MmsDeliverReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) = Unit
}
