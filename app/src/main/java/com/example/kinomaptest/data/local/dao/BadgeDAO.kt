package com.example.kinomaptest.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kinomaptest.data.local.entity.BadgeDTO
import kotlinx.coroutines.flow.Flow

@Dao
interface BadgeDAO {


    /** INSERT **/
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertOne(badge: BadgeDTO): Long // return new object ID

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(badges: List<BadgeDTO>): List<Long> // return new objects IDs

    /** UPDATE **/
    @Update
    suspend fun updateOne(badge: BadgeDTO): Int // return updated rows count

    @Update
    suspend fun updateALL(badges: List<BadgeDTO>): Int // return updated rows count

    /** DELETE **/
    @Query("DELETE FROM badge")
    suspend fun deleteAll()

    @Query("DELETE FROM badge WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Delete
    suspend fun delete(badge: BadgeDTO)

    @Delete
    suspend fun delete(badges: List<BadgeDTO>)

    /** SELECT REAL TIME / FLOW **/
    @Query("SELECT * FROM badge WHERE id = :id")
    fun getByIdRT(id: Int): Flow<BadgeDTO?>

    @Query("SELECT * FROM badge")
    fun getAllRT(): Flow<List<BadgeDTO>>

    /** SELECT SUSPENDED **/
    @Query("SELECT * FROM badge WHERE id = :id")
    suspend fun getById(id: Int): BadgeDTO?

    @Query("SELECT * FROM badge")
    suspend fun getAll(): List<BadgeDTO>

    /** SEARCH BY MULTIPLE CATEGORIES **/
    @Query("SELECT * FROM badge WHERE category IN (:categories)")
    fun getBadgesByCategoriesRT(categories: List<String>): Flow<List<BadgeDTO>>

    @Query("SELECT DISTINCT category FROM badge ORDER BY category")
    fun observeCategories(): Flow<List<String>>

    /** Optional: suspended version for one-time fetch **/
    @Query("SELECT * FROM badge WHERE category IN (:categories)")
    suspend fun getBadgesByCategories(categories: List<String>): List<BadgeDTO>

    @Query("SELECT DISTINCT category FROM badge ORDER BY category")
    suspend fun getCategories(): List<String>

}