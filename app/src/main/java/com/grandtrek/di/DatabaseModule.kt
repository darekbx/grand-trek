package com.grandtrek.di

import android.arch.persistence.room.Room
import android.content.Context
import com.grandtrek.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.migration.Migration



@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesAppDatabase(context: Context) =
            Room
                    .databaseBuilder(context.applicationContext, AppDatabase::class.java, AppDatabase.DB_NAME)
                    .addMigrations(MIGRATION_1_2)
                    .allowMainThreadQueries()
                    .build()

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Route ADD COLUMN ridetime INTEGER DEFAULT 0 NOT NULL")
        }
    }
}