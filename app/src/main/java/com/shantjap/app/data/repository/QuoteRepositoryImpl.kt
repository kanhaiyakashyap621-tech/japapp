package com.shantjap.app.data.repository

import android.content.res.AssetManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shantjap.app.domain.model.Quote
import com.shantjap.app.domain.repository.QuoteRepository
import java.io.InputStreamReader

class QuoteRepositoryImpl(
    private val assets: AssetManager
) : QuoteRepository {

    private val cached: List<Quote> by lazy { loadQuotes() }

    override suspend fun getQuoteForDay(dayOfYear: Int): Quote {
        if (cached.isEmpty()) {
            return Quote(day = dayOfYear, hindi = "", english = "")
        }
        val index = (dayOfYear.coerceIn(1, cached.size)) - 1
        return cached[index]
    }

    private fun loadQuotes(): List<Quote> {
        assets.open("quotes.json").use { input ->
            val reader = InputStreamReader(input)
            val type = object : TypeToken<List<QuoteDto>>() {}.type
            val items: List<QuoteDto> = Gson().fromJson(reader, type)
            return items.map { Quote(day = it.day, hindi = it.hindi, english = it.english) }
        }
    }

    private data class QuoteDto(
        val day: Int,
        val hindi: String,
        val english: String
    )
}
