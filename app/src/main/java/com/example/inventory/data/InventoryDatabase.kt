package com.example.inventory.data

import android.content.Context
import android.security.keystore.KeyProperties
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.inventory.ui.preferences.PreferencesService
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory
import java.io.IOException
import java.security.KeyStore
import javax.crypto.KeyGenerator

const val dbName = "item_database"

@Database(entities = [Item::class], version = 20, exportSchema = true)
abstract class InventoryDatabase: RoomDatabase() {
    abstract fun itemDao(): ItemDao
    companion object {
        @Volatile
        private var Instance: InventoryDatabase? = null

        fun getDatabase(context: Context): InventoryDatabase {
            val passphrase = SQLiteDatabase.getBytes(PreferencesService.advancedKeyAlias.toCharArray())
            val factory = SupportFactory(passphrase)
            val dbFile = context.getDatabasePath(dbName)
            val state = SQLCipherUtils.getDatabaseState(context, dbFile)

            if (state == SQLCipherUtils.State.UNENCRYPTED) {
                val dbTemp = context.getDatabasePath("_temp.db")
                dbTemp.delete()
                SQLCipherUtils.encryptTo(context, dbFile, dbTemp, passphrase)
                val dbBackup = context.getDatabasePath("_backup.db")

                if (dbFile.renameTo(dbBackup)) {
                    if (dbTemp.renameTo(dbFile)) {
                        dbBackup.delete()
                    } else {
                        dbBackup.renameTo(dbFile)
                        throw IOException("Could not rename $dbTemp to $dbFile")
                    }
                } else {
                    dbTemp.delete()
                    throw IOException("Could not rename $dbFile to $dbBackup")
                }
            }

            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, InventoryDatabase::class.java, dbName)
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                    .also{ Instance = it }

            }
        }
    }
/*
    companion object {
        fun newInstance(context: Context): ToDoDatabase {
            val passphrase = PASSPHRASE.toByteArray()
            val state = SQLCipherUtils.getDatabaseState(context, dbFile)
            if (state = SQLCipherUtils.State.UNENCRYPTED) {
                val dbTemp = context.getDatabasePath("_temp.db")
                dbTemp.delete()
                SQLCipherUtils.encryptTo(context, dbFile, dbTemp, passphrase)
                val dbBackup = context.getDatabasePath("_backup.db")

                if (dbFile.renameTo(dbBackup)) {
                    if (dbTemp.renameTo(dbFile)) {
                        dbBackup.delete()
                    } else {
                        dbBackup.renameTo(dbFile)
                        throw IOException("Could not rename $dbTemp to $dbFile")
                    }
                } else {
                    dbTemp.delete()
                    throw IOException("Could not rename $dbFile to $dbBackup")
                }
            }
            return Room.databaseBuilder(context, ToDoDatabase::class.java, DB_NAME)
                .openHelperFactory(SupportFactory(passphrase))
                .build()
        }
    }*/
}