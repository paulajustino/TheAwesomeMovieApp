package com.example.theawesomemovieapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromCarouselItemList(value: List<CarouselItem>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCarouselItemList(value: String?): List<CarouselItem>? {
        val listType = object : TypeToken<List<CarouselItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}