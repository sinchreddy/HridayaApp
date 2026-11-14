package com.hridaya.app.data.db

import androidx.room.*
import com.hridaya.app.data.model.*
import kotlinx.coroutines.flow.Flow

// Type Converters
class Converters {
    @TypeConverter
    fun fromMood(mood: Mood?): String? = mood?.name

    @TypeConverter
    fun toMood(name: String?): Mood? = name?.let { Mood.valueOf(it) }
}

// DAOs
@Dao
interface MessageDao {
    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY timestamp ASC")
    fun getMessagesForSession(sessionId: String): Flow<List<Message>>

    @Query("SELECT sessionId FROM messages GROUP BY sessionId ORDER BY MAX(timestamp) DESC")
    fun getAllSessionIds(): Flow<List<String>>

    @Query("SELECT * FROM messages WHERE sessionId = :sessionId ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLastMessage(sessionId: String): Message?

    @Insert
    suspend fun insertMessage(message: Message): Long

    @Query("DELETE FROM messages WHERE sessionId = :sessionId")
    suspend fun deleteSession(sessionId: String)

    @Query("SELECT * FROM messages ORDER BY timestamp DESC LIMIT 50")
    fun getRecentMessages(): Flow<List<Message>>
}

@Dao
interface MoodDao {
    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC")
    fun getAllMoods(): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries WHERE timestamp >= :since ORDER BY timestamp ASC")
    fun getMoodsSince(since: Long): Flow<List<MoodEntry>>

    @Query("SELECT * FROM mood_entries ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMood(): MoodEntry?

    @Insert
    suspend fun insertMood(entry: MoodEntry): Long

    @Delete
    suspend fun deleteMood(entry: MoodEntry)
}

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY timestamp DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Query("SELECT * FROM journal_entries WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteEntries(): Flow<List<JournalEntry>>

    @Insert
    suspend fun insertEntry(entry: JournalEntry): Long

    @Update
    suspend fun updateEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    @Query("SELECT * FROM journal_entries WHERE id = :id")
    suspend fun getEntryById(id: Long): JournalEntry?
}

// Database
@Database(
    entities = [Message::class, MoodEntry::class, JournalEntry::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HridayaDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao
    abstract fun moodDao(): MoodDao
    abstract fun journalDao(): JournalDao
}
