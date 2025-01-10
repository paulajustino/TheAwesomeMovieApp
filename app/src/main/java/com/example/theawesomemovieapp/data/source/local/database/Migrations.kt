package com.example.theawesomemovieapp.data.source.local.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Criar a nova tabela Movie sem a coluna carouselImages
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `Movie_temp` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `title` TEXT NOT NULL,
                `posterImage` TEXT,
                `content` TEXT NOT NULL,
                `rating` REAL NOT NULL
            )
        """)

        // Copiar os dados da tabela Movie para a Movie_temp, ignorando a coluna carouselImages
        db.execSQL("""
            INSERT INTO `Movie_temp` (`id`, `title`, `posterImage`, `content`, `rating`)
            SELECT `id`, `title`, `posterImage`, `content`, `rating` FROM `Movie`
        """)

        // Deletar a tabela Movie antiga
        db.execSQL("DROP TABLE IF EXISTS `Movie`")

        // Renomear a tabela Movie_temp para Movie
        db.execSQL("ALTER TABLE `Movie_temp` RENAME TO `Movie`")

        // Criando a nova tabela MovieImages
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS `MovieImages` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `movieId` INTEGER NOT NULL,
                `images` TEXT NOT NULL,
                FOREIGN KEY(`movieId`) REFERENCES `Movie`(`id`) ON DELETE CASCADE
            )
        """)
    }
}