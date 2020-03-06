package com.example.android.roomwordssample

import androidx.room.ColumnInfo

/**
 * @Description:
 * @Author: wutengfei
 * @CreateDate: 20-3-6 上午9:21
 */
class InnerJoinResult {
    //这里同样省略了getter/setter方法
    @ColumnInfo(name = "emp_id")
    var empId = 0
    var name: String? = null
    var dept: String? = null

}