package com.example.supfitness

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SQLiteHelper (context:Context) : SQLiteOpenHelper(context, DATABASE_NAME,null , DATABASE_VERSION){

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME= "student.db"
        const val TBL_WEIGHT = "tbl_weight"
        private const val ID = "id"
        const val KG = "kg"
        private const val KG2 = "kg2"
        const val DATE = "date"

    }

    override fun onCreate(db: SQLiteDatabase?){

        val createTblStudent = ("CREATE TABLE " + TBL_WEIGHT + "("
                + ID + " INTEGER PRIMARY KEY," + KG + " TEXT," + KG2 + " TEXT,"
                + DATE + " TEXT" + ")")
        db?.execSQL(createTblStudent)
        val createTblRun = ("CREATE TABLE " + "Run" + "("
                + "id" + " INTEGER PRIMARY KEY," + "startdate" + " TEXT," + "endate" + " TEXT,"
                + "totaltime" + " TEXT," + "totalposition" + " TEXT" + ")")
        db?.execSQL(createTblRun)
        val createTblPos = ("CREATE TABLE " + "Position" + "("
                + "id" + " INTEGER," + "latitude" + " TEXT," + "longitude" + " TEXT" + ")")
        db?.execSQL(createTblPos)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int){
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_WEIGHT")
        onCreate(db)
    }

    fun insertStudent(std: WeightModel): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        val kgf1 = std.kg.toString().toInt()
        val kgf2 = std.kg2.toString().toInt()
        contentValues.put (ID, std.id)
        contentValues.put (KG, kgf1)
        contentValues.put (KG2, kgf2)
        contentValues.put (DATE, std.date)

        val success = db.insert(TBL_WEIGHT, null, contentValues)
        db.close()
        return success


    }

    fun getToday(): Boolean {
        val tempdate = SimpleDateFormat("dd/M/yyyy")
        val datenow: String = tempdate.format(Date())
        val countQuery = "SELECT * FROM $TBL_WEIGHT WHERE date == '$datenow'"
        val db = this.readableDatabase
        var result = false

        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        if (count > 0)
            result = true
        return result
    }

    fun count(): Boolean {
        val countQuery = "SELECT * FROM $TBL_WEIGHT"
        val db = this.readableDatabase
        var result = false

        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        if (count >= 3)
            result = true
        return result
    }


    @SuppressLint("Range")
    fun getAllStudent(): ArrayList<WeightModel>{
        val stdList: ArrayList<WeightModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_WEIGHT ORDER BY date DESC, id DESC"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var kg: Int
        var kg2: Int
        var date: String


        if(cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                kg =  cursor.getInt(cursor.getColumnIndex("kg"))
                kg2 =  cursor.getInt(cursor.getColumnIndex("kg2"))
                date =  cursor.getString(cursor.getColumnIndex("date"))

                val std = WeightModel(id = id, kg = kg, kg2 = kg2, date = date)
                stdList.add(std)
            } while (cursor.moveToNext())
        }

        return stdList
    }

    fun deleteStudentById(id: Int): Int {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TBL_WEIGHT, "id=$id", null)
        db.close()
        return success
    }
}