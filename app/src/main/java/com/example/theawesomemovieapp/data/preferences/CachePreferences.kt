package com.example.theawesomemovieapp.data.preferences

import android.content.Context
import com.example.theawesomemovieapp.utils.Constants.CACHE_DURATION
import javax.inject.Inject

interface CachePreferences {
    fun isCacheExpired(key: String): Boolean
    fun setLastUpdateTime(key: String, timestamp: Long)
}

class CachePreferencesImpl @Inject constructor(context: Context): CachePreferences {
    private val sharedPreferences =
        context.getSharedPreferences("cache_prefs", Context.MODE_PRIVATE)

    // Salvar o timestamp da última atualização
    override fun setLastUpdateTime(key: String, time: Long) {
        sharedPreferences.edit().putLong(key, time).apply()
    }

    // Obter o timestamp da última atualização
    private fun getLastUpdateTime(key: String): Long {
        return sharedPreferences.getLong(key, 0L)
    }

    // Verificar se o cache está expirado (exemplo: 15 minutos)
    override fun isCacheExpired(key: String): Boolean {
        val lastUpdateTime = getLastUpdateTime(key)
        val currentTime = System.currentTimeMillis()
        return (currentTime - lastUpdateTime) > CACHE_DURATION
    }
}
