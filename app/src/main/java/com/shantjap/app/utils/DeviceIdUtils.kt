package com.shantjap.app.utils

import android.content.Context
import android.provider.Settings

object DeviceIdUtils {
    fun getDeviceId(context: Context): String {
        return Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: "unknown"
    }
}
