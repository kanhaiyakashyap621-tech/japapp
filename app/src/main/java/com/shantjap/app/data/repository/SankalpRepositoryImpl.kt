package com.shantjap.app.data.repository

import com.shantjap.app.data.local.dao.SankalpDao
import com.shantjap.app.data.local.entities.SankalpEntity
import com.shantjap.app.domain.model.Sankalp
import com.shantjap.app.domain.repository.SankalpRepository
import com.shantjap.app.utils.DateUtils

class SankalpRepositoryImpl(
    private val sankalpDao: SankalpDao
) : SankalpRepository {

    override suspend fun getSankalp(): Sankalp? {
        val entity = sankalpDao.getSankalp() ?: return null
        return Sankalp(text = entity.text, lastReminderDate = entity.lastReminderDate)
    }

    override suspend fun saveSankalp(text: String) {
        val today = DateUtils.todayKey()
        sankalpDao.upsert(SankalpEntity(text = text, lastReminderDate = today))
    }

    override suspend fun markReminderShown(date: String) {
        val current = sankalpDao.getSankalp() ?: return
        sankalpDao.upsert(current.copy(lastReminderDate = date))
    }
}
