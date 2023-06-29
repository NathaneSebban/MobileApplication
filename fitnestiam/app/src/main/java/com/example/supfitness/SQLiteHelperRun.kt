package com.example.supfitness

import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class SQLiteHelperRun (context:Context) : SQLiteOpenHelper(context, DATABASE_NAME,null , DATABASE_VERSION) {

    companion object {

        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "student.db"
        const val TBL_RUN = "Run"
        private const val ID = "id"
        const val STARTDATE = "startdate"
        const val ENDDATE = "endate"
        const val TOTALTIME = "totaltime"
        var finalid = 0

    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.e("id run :", "passed")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_RUN")
        onCreate(db)

    }

    fun startRun() {
        val db = this.writableDatabase

        val tempdate = SimpleDateFormat("dd/M/yyyy H:m:s")
        val startDate: String = tempdate.format(Date()).toString()
        finalid = getAutoId()

        db?.execSQL("INSERT INTO $TBL_RUN ($ID, $STARTDATE) VALUES ($finalid, '$startDate')")
        Log.e("id run :", "$finalid")
        db.close()
    }

    fun PositionSave(latitude: String, longitude: String) {
        val db = this.writableDatabase

        db?.execSQL("INSERT INTO Position ($ID, latitude, longitude) VALUES ($finalid, '$latitude', '$longitude')")
        db.close()
    }

    fun count() {
        val countQuery = "SELECT * FROM Position WHERE id = $finalid"
        val db = this.readableDatabase

        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        db?.execSQL("UPDATE $TBL_RUN SET totalposition = '$count' WHERE $ID = $finalid")
        cursor.close()
    }

    fun getAutoId(): Int {
        val random = Random()
        return random.nextInt(100)
    }

    fun finishRun(timecome: Double) {
        count()
        val db = this.writableDatabase

        val tempdate = SimpleDateFormat("dd/M/yyyy H:m:s")
        val finalDate: String = tempdate.format(Date()).toString()

        db?.execSQL("UPDATE $TBL_RUN SET $ENDDATE = '$finalDate', $TOTALTIME = $timecome WHERE $ID = $finalid")
        Log.e("id run :", "$finalid $finalDate $timecome")
        db.close()
    }

    @SuppressLint("Range")
    fun getAllRuns(): ArrayList<RunModel> {
        val stdList: ArrayList<RunModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_RUN ORDER BY $STARTDATE DESC"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var startdate: String
        var endate: String
        var totaltime: String
        var totalposition: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                startdate = cursor.getString(cursor.getColumnIndex("startdate"))
                endate = cursor.getString(cursor.getColumnIndex("endate"))
                totaltime = cursor.getString(cursor.getColumnIndex("totaltime"))
                totalposition = cursor.getString(cursor.getColumnIndex("totalposition"))

                val std =
                    RunModel(id = id, startdate = startdate, endate = endate, totaltime = totaltime, totalposition = totalposition)
                stdList.add(std)
            } while (cursor.moveToNext())
        }

        return stdList
    }
}

   /* fun getToday(): Boolean {
        val tempdate = SimpleDateFormat("dd/M/yyyy")
        val datenow: String = tempdate.format(Date())
        val countQuery = "SELECT * FROM $TBL_STUDENT WHERE date == '$datenow'"
        val db = this.readableDatabase
        var result = false

        val cursor = db.rawQuery(countQuery, null)
        val count = cursor.count
        cursor.close()
        if (count > 0)
            result = true
        return result
    }


    /*
    fun getAllStudent(): ArrayList<WeightModel>{
        val stdList: ArrayList<WeightModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_STUDENT ORDER BY date DESC, id DESC"
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
    }*/