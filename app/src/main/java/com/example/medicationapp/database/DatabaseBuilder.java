package com.example.medicationapp.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.medicationapp.model.Medication;

@Database(entities = {Medication.class},version = 1)
@TypeConverters(Converter.class)
public abstract class DatabaseBuilder extends RoomDatabase {

    private static DatabaseBuilder databaseBuilder = null;

    public synchronized static DatabaseBuilder getInstance(Context context){
        if(databaseBuilder ==null){
            databaseBuilder = Room.databaseBuilder(context.getApplicationContext(),
                    DatabaseBuilder.class, "MedicationDB").build();
        }
        return databaseBuilder;
    }

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }

    public abstract DAO getDao();
}
