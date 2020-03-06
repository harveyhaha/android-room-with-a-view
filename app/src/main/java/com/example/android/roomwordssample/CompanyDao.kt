package com.example.android.roomwordssample

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * @Description:
 * @Author: wutengfei
 * @CreateDate: 20-3-6 上午9:20
 */
@Dao
interface CompanyDao {
    @get:Query("SELECT * FROM company")
    val allCompany: LiveData<List<Company>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(company: Company)

    @Delete
    fun delete(company: Company)
}