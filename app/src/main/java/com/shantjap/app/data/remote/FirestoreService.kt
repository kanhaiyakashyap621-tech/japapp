package com.shantjap.app.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shantjap.app.domain.model.LeaderboardEntry
import kotlinx.coroutines.tasks.await

class FirestoreService {
    private val db: FirebaseFirestore = Firebase.firestore

    suspend fun upsertDailyEntry(dateKey: String, entry: LeaderboardEntry) {
        val ref = db.collection("daily_rankings")
            .document(dateKey)
            .collection("entries")
            .document(entry.deviceId)
        val payload = entryPayload(entry)
        ref.set(payload, SetOptions.merge()).await()
    }

    suspend fun upsertWeeklyEntry(weekKey: String, entry: LeaderboardEntry) {
        val ref = db.collection("weekly_rankings")
            .document(weekKey)
            .collection("entries")
            .document(entry.deviceId)
        val payload = entryPayload(entry)
        ref.set(payload, SetOptions.merge()).await()
    }

    suspend fun upsertLifetimeEntry(entry: LeaderboardEntry) {
        val ref = db.collection("lifetime_rankings")
            .document(entry.deviceId)
        val payload = entryPayload(entry)
        ref.set(payload, SetOptions.merge()).await()
    }

    suspend fun incrementDailyCount(dateKey: String, deviceId: String, delta: Long) {
        val ref = db.collection("daily_rankings")
            .document(dateKey)
            .collection("entries")
            .document(deviceId)
        ref.update("count", FieldValue.increment(delta)).await()
    }

    suspend fun incrementWeeklyCount(weekKey: String, deviceId: String, delta: Long) {
        val ref = db.collection("weekly_rankings")
            .document(weekKey)
            .collection("entries")
            .document(deviceId)
        ref.update("count", FieldValue.increment(delta)).await()
    }

    suspend fun incrementLifetimeCount(deviceId: String, delta: Long) {
        val ref = db.collection("lifetime_rankings")
            .document(deviceId)
        ref.update("count", FieldValue.increment(delta)).await()
    }

    suspend fun incrementGlobalTotal(delta: Long) {
        val ref = db.collection("global_stats").document("total")
        ref.set(mapOf("total" to FieldValue.increment(delta)), SetOptions.merge()).await()
    }

    suspend fun getDailyRanking(dateKey: String, limit: Long): List<LeaderboardEntry> {
        val snapshot = db.collection("daily_rankings")
            .document(dateKey)
            .collection("entries")
            .orderBy("count", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
        return snapshot.documents.mapNotNull { toEntry(it.data) }
    }

    suspend fun getWeeklyRanking(weekKey: String, limit: Long): List<LeaderboardEntry> {
        val snapshot = db.collection("weekly_rankings")
            .document(weekKey)
            .collection("entries")
            .orderBy("count", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
        return snapshot.documents.mapNotNull { toEntry(it.data) }
    }

    suspend fun getLifetimeRanking(limit: Long): List<LeaderboardEntry> {
        val snapshot = db.collection("lifetime_rankings")
            .orderBy("count", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()
        return snapshot.documents.mapNotNull { toEntry(it.data) }
    }

    suspend fun getGlobalTotal(): Long {
        val snapshot = db.collection("global_stats")
            .document("total")
            .get()
            .await()
        return snapshot.getLong("total") ?: 0L
    }

    private fun entryPayload(entry: LeaderboardEntry): Map<String, Any> {
        return mapOf(
            "deviceId" to entry.deviceId,
            "name" to entry.name,
            "city" to entry.city,
            "country" to entry.country,
            "mode" to entry.mode.name,
            "count" to entry.count,
            "updatedAt" to entry.updatedAt
        )
    }

    private fun toEntry(data: Map<String, Any>?): LeaderboardEntry? {
        if (data == null) return null
        val deviceId = data["deviceId"] as? String ?: return null
        val name = data["name"] as? String ?: return null
        val city = data["city"] as? String ?: ""
        val country = data["country"] as? String ?: ""
        val mode = (data["mode"] as? String)?.let { runCatching { enumValueOf(it) }.getOrNull() } ?: return null
        val count = (data["count"] as? Number)?.toLong() ?: 0L
        val updatedAt = (data["updatedAt"] as? Number)?.toLong() ?: 0L
        return LeaderboardEntry(
            deviceId = deviceId,
            name = name,
            city = city,
            country = country,
            mode = mode,
            count = count,
            updatedAt = updatedAt
        )
    }
}
