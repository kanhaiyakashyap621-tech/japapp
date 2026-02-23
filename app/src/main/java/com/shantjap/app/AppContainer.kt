package com.shantjap.app

import android.content.Context
import com.shantjap.app.data.local.AppDatabase
import com.shantjap.app.data.remote.FirestoreService
import com.shantjap.app.data.repository.CounterRepositoryImpl
import com.shantjap.app.data.repository.LeaderboardRepositoryImpl
import com.shantjap.app.data.repository.QuoteRepositoryImpl
import com.shantjap.app.data.repository.SankalpRepositoryImpl
import com.shantjap.app.data.repository.UserProfileRepositoryImpl

class AppContainer(context: Context) {
    private val database = AppDatabase.getInstance(context)
    private val firestoreService = FirestoreService()

    val counterRepository = CounterRepositoryImpl(
        countDao = database.countDao(),
        journeyDao = database.journeyDao()
    )
    val sankalpRepository = SankalpRepositoryImpl(database.sankalpDao())
    val userProfileRepository = UserProfileRepositoryImpl(database.userProfileDao())
    val quoteRepository = QuoteRepositoryImpl(context.assets)
    val leaderboardRepository = LeaderboardRepositoryImpl(
        firestoreService = firestoreService,
        userProfileRepository = userProfileRepository,
        appContext = context.applicationContext
    )
}
