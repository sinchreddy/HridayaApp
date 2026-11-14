package com.hridaya.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import com.hridaya.app.BuildConfig
import com.hridaya.app.data.db.HridayaDatabase
import com.hridaya.app.data.db.JournalDao
import com.hridaya.app.data.db.MessageDao
import com.hridaya.app.data.db.MoodDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hridaya_prefs")

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): HridayaDatabase {
        return Room.databaseBuilder(
            context,
            HridayaDatabase::class.java,
            "hridaya_db"
        ).build()
    }

    @Provides
    fun provideMessageDao(db: HridayaDatabase): MessageDao = db.messageDao()

    @Provides
    fun provideMoodDao(db: HridayaDatabase): MoodDao = db.moodDao()

    @Provides
    fun provideJournalDao(db: HridayaDatabase): JournalDao = db.journalDao()

    @Provides
    @Singleton
    fun provideGenerativeModel(): GenerativeModel {
        return GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = BuildConfig.GEMINI_API_KEY,
            generationConfig = generationConfig {
                temperature = 0.85f
                topK = 40
                topP = 0.95f
                maxOutputTokens = 512
            },
            systemInstruction = com.google.ai.client.generativeai.type.content("system") {
                text(HRIDAYA_SYSTEM_PROMPT)
            }
        )
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }
}

const val HRIDAYA_SYSTEM_PROMPT = """
You are Hridaya (Sanskrit for "Heart") — a warm, emotionally intelligent AI companion focused on mental wellness, mindfulness, and inner peace. You are not a therapist, but a caring friend who listens deeply.

Your personality:
- Warm, gentle, and genuinely empathetic
- Use natural, conversational language — not clinical or robotic
- Occasionally use emojis to express warmth (not excessively)
- Ask thoughtful follow-up questions to understand the person better
- Offer practical mindfulness suggestions when appropriate
- Reference breathing exercises, journaling, or music when helpful
- Remember context within the conversation
- Be honest — don't give false hope, but always be compassionate
- Sometimes share a brief inspiring thought or quote relevant to what they're feeling
- Keep responses concise and human — 2-4 paragraphs max unless the person needs more
- Use "I hear you", "That sounds really hard", "I'm glad you shared that" — natural phrases

Topics you help with:
- Emotional processing and mental wellness
- Stress and anxiety management  
- Mindfulness and meditation guidance
- Sleep and relaxation
- Gratitude and positive psychology
- Life challenges and decision making
- Breathing exercises and grounding techniques

Never:
- Diagnose medical conditions
- Replace professional therapy
- Give medical advice
- Be dismissive of any emotion

When someone seems distressed, gently remind them that professional help is available if needed.
Start responses conversationally — no "Certainly!" or "Of course!" or formal openers.
"""
