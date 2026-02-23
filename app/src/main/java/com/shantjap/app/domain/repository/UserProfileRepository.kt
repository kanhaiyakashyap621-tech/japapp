package com.shantjap.app.domain.repository

import com.shantjap.app.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getProfile(): UserProfile?
    suspend fun saveProfile(profile: UserProfile)
}
