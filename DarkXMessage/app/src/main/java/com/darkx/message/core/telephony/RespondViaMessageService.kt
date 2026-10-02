package com.darkx.message.core.telephony

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Declared for default SMS role eligibility ("reply by message" from the dialer).
 *
 * NOT IMPLEMENTED YET: sending is wired up with the send/receive phase.
 */
class RespondViaMessageService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
}
