package com.shantjap.app.utils

import java.util.Locale

object CountryUtils {
    fun detectCountry(): String {
        val locale = Locale.getDefault()
        val display = locale.displayCountry
        return if (display.isNullOrBlank()) locale.country else display
    }
}
