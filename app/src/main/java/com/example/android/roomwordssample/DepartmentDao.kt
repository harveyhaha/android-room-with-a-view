package com.example.android.roomwordssample

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * @Description:
 * @Author: wutengfei
 * @CreateDate: 20-3-6 上午9:20
 */
@Dao
interface DepartmentDao {
    @get:Query("SELECT * FROM department")
    val allDepartment: LiveData<List<Department>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(department: Department)

    //使用内连接查询
    @get:Query("SELECT emp_id,name,dept  from company INNER JOIN department ON Company.id=Department.emp_id")
    val departmentFromCompany: LiveData<List<InnerJoinResult>>
}