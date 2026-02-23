package com.shantjap.app.data.repository

import com.shantjap.app.data.local.dao.UserProfileDao
import com.shantjap.app.data.local.entities.UserProfileEntity
import com.shantjap.app.domain.model.UserProfile
import com.shantjap.app.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {

    override suspend fun getProfile(): UserProfile? {
        val entity = userProfileDao.getProfile() ?: return null
        return UserProfile(
            name = entity.name,
            city = entity.city,
            country = entity.country,
            lastSyncedLifetime = entity.lastSyncedLifetime
        )
    }

    override suspend fun saveProfile(profile: UserProfile) {
        userProfileDao.upsert(
            UserProfileEntity(
                name = profile.name,
                city = profile.city,
                country = profile.country,
                lastSyncedLifetime = profile.lastSyncedLifetime
            )
        )
    }
}
