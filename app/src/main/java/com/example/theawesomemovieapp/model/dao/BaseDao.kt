package com.example.theawesomemovieapp.model.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update

interface BaseDao<T> {

    @Insert
    suspend fun insert(entity: T)

    @Insert
    suspend fun insertList(entities: List<T>)

    @Update
    suspend fun update(entity: T)

    @Update
    suspend fun updateList(entities: List<T>)

    @Delete
    suspend fun delete(entity: T)

    @Delete
    suspend fun deleteList(entities: List<T>)
}