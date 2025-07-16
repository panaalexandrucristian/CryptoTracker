package com.cryptotracker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.cryptotracker.data.local.entity.CoinEntity

@Dao
interface CoinDao {

    @Query("SELECT * FROM coins")
    suspend fun getAllCoins(): List<CoinEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoins(coins: List<CoinEntity>)

    @Query("DELETE FROM coins")
    suspend fun deleteAllCoins()

    @Transaction
    suspend fun replaceAllCoins(coins: List<CoinEntity>) {
        deleteAllCoins()
        insertCoins(coins)
    }
}
