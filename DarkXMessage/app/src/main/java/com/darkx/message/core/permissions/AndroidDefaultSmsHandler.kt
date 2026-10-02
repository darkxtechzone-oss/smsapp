package com.darkx.message.core.permissions

import android.app.role.RoleManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Telephony

class AndroidDefaultSmsHandler(private val context: Context) : DefaultSmsHandler {

    private val hasTelephony: Boolean
        get() = context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)

    private val roleManager: RoleManager?
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            context.getSystemService(RoleManager::class.java)
        } else {
            null
        }

    override fun isRoleAvailable(): Boolean {
        if (!hasTelephony) return false
        // Before Android 10 there is no role; the legacy ACTION_CHANGE_DEFAULT flow is used.
        return roleManager?.isRoleAvailable(RoleManager.ROLE_SMS) ?: true
    }

    override fun isDefaultSmsApp(): Boolean {
        val manager = roleManager
        return if (manager != null) {
            manager.isRoleHeld(RoleManager.ROLE_SMS)
        } else {
            Telephony.Sms.getDefaultSmsPackage(context) == context.packageName
        }
    }

    override fun createRequestIntent(): Intent? {
        if (!isRoleAvailable()) return null
        val manager = roleManager
        return if (manager != null) {
            manager.createRequestRoleIntent(RoleManager.ROLE_SMS)
        } else {
            Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT)
                .putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, context.packageName)
        }
    }
}
