package com.shantjap.app.domain.repository

import com.shantjap.app.domain.model.Sankalp

interface SankalpRepository {
    suspend fun getSankalp(): Sankalp?
    suspend fun saveSankalp(text: String)
    suspend fun markReminderShown(date: String)
}
