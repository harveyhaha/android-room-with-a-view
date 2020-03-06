package com.example.android.roomwordssample

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException


/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class CompanyDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var companyDao: CompanyDao
    private lateinit var departmentDao: DepartmentDao
    private lateinit var db: WordRoomDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, WordRoomDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        companyDao = db.companyDao()
        departmentDao = db.departmentDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetCompany() = runBlocking {
        val company = Company("阿里", 32, "California", 20000.0)
        val company1 = Company("百度", 32, "beijing", 20000.0)
        val company2 = Company("京东", 32, "beijing", 20000.0)
        companyDao.insert(company)
        companyDao.insert(company1)
        companyDao.insert(company2)
        val allCompany: List<Company> = companyDao.allCompany.waitForValue()
        println("所有公司")
        for (nowCompany in allCompany) {
            println("" + nowCompany.id + " " + nowCompany.name + " " + nowCompany.address)
        }
        //        Log.i("WordDaoTest",allWords.get(0).getWord() + " " + word.getWord());
        assertEquals(allCompany.size, 3)
    }

    @Test
    @Throws(java.lang.Exception::class)
    fun insertAndGetDepartment() {
        insertAndGetCompany()
        val department = Department("研发部", 1)
        val department1 = Department("财务部", 2)
        val department2 = Department("行政部", 3)
        departmentDao.insert(department)
        departmentDao.insert(department1)
        departmentDao.insert(department2)
        val allDepartment: List<Department> = departmentDao.allDepartment.waitForValue()
        println("所有部门")
        for (nowDepartment in allDepartment) {
            println("" + nowDepartment.id + " " + nowDepartment.dept + " " + nowDepartment.empId)
        }
        assertEquals(allDepartment.size, 3)
        assertEquals(allDepartment[0].empId, department.empId)
        assertEquals(allDepartment[1].empId, department1.empId)
        assertEquals(allDepartment[2].empId, department2.empId)
        val innerJoinResults: List<InnerJoinResult> = departmentDao.departmentFromCompany.waitForValue()
        println("多表查询")
        for (innerJoinResult in innerJoinResults) {
            println(innerJoinResult.dept + " " + innerJoinResult.empId + " " + innerJoinResult.name)
        }
    }

    /**
     * 删除公司会自动删除部门
     */
    @Test
    @Throws(java.lang.Exception::class)
    fun deleteCompany() {
        insertAndGetDepartment()
        val allCompany: List<Company> = companyDao.allCompany.waitForValue()
        println("删除公司：" + allCompany[0].name)
        companyDao.delete(allCompany[0])
        val allCompany1: List<Company> = companyDao.allCompany.waitForValue()
        println("所有公司")
        for (nowCompany in allCompany1) {
            println(nowCompany.id.toString() + " " + nowCompany.name + " " + nowCompany.address)
        }
        println("所有部门")
        val allDepartment: List<Department> = departmentDao.allDepartment.waitForValue()
        for (nowDepartment in allDepartment) {
            println("" + nowDepartment.id + " " + nowDepartment.dept + " " + nowDepartment.empId)
        }
        assertEquals(allDepartment.size, 2)
    }
}
