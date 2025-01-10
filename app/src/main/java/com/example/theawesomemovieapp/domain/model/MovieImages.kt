package com.example.theawesomemovieapp.domain.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Movie::class,
            parentColumns = ["id"],
            childColumns = ["movieId"],
            onDelete = ForeignKey.CASCADE // Deleta as imagens se o filme for deletado
        )
    ]
)
data class MovieImages(
    @PrimaryKey val id: Int,
    val movieId: Int,
    val images: List<String>
)