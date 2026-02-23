package com.shantjap.app.domain.repository

import com.shantjap.app.domain.model.Quote

interface QuoteRepository {
    suspend fun getQuoteForDay(dayOfYear: Int): Quote
}
