package com.shantjap.app.utils

import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator
import com.shantjap.app.domain.model.Mode

object Haptics {
    fun vibrate(context: Context, mode: Mode) {
        val pattern = when (mode) {
            Mode.RADHA -> longArrayOf(0, 18, 50, 18)
            Mode.RAM -> longArrayOf(0, 24, 36, 24)
        }
        val vibrator = context.getSystemService(Vibrator::class.java) ?: return
        val effect = VibrationEffect.createWaveform(pattern, -1)
        vibrator.vibrate(effect)
    }
}
