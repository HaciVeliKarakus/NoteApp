package io.hvk.noteappdel

import android.app.Application
import androidx.room.Room
import io.hvk.noteappdel.data.NoteDatabase
import io.hvk.noteappdel.data.NoteRepository

class NoteApp : Application() {
    private lateinit var database: NoteDatabase
    lateinit var repository: NoteRepository

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            this,
            NoteDatabase::class.java,
            "notes.db"
        ).build()
        repository = NoteRepository(database.noteDao)
    }
} 