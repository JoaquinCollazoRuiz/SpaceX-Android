package com.joaquincollazoruiz.spacex.data.local

import androidx.room.*
import com.joaquincollazoruiz.spacex.data.local.model.LaunchEntity

@Dao
interface LaunchesDao {

    @Transaction
    fun deleteExistingAndInsertAll(launches: List<LaunchEntity>) {
        deleteAll()
        insertAll(launches)
    }

    @Query(
        """SELECT * FROM launches
                 WHERE
                    CASE
                        WHEN :launchStatusCriteria IS NULL THEN 1
                        ELSE success = :launchStatusCriteria
                    END
                 AND
                    CASE
                        WHEN :launchYearCriteria IS NULL THEN 1
                        ELSE year = :launchYearCriteria
                    END
                 ORDER BY
                   CASE WHEN :sortAscending = 1 THEN dateUnix END ASC,
                   CASE WHEN :sortAscending = 0 THEN dateUnix END DESC
              """
    )
    fun getAllWithMatchingCriteria(
        sortAscending: Boolean,
        launchStatusCriteria: Boolean?,
        launchYearCriteria: Int?
    ): List<LaunchEntity>?

    @Query("DELETE FROM launches")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(launches: List<LaunchEntity>)
}