package com.academica.tenant.core.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import com.academica.tenant.models.ServerModels

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "servers"
        private const val TBL_SERVER = "tbl_server"
        private const val ID = "id"
        private const val address = "address"
        private const val name = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        var createTblServer = "CREATE TABLE " + TBL_SERVER + " (" +
                ID + " INTEGER PRIMARY KEY, " +
                address + " TEXT, " +
                name + " TEXT );";
        db.execSQL(createTblServer)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TBL_SERVER")
        onCreate(db)
    }

    fun insertServer(std: ServerModels): Long {
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(address, std.address)
        contentValues.put(name, std.name)

        val success = db.insert(TBL_SERVER, null, contentValues)
        db.close()
        return success
    }

    fun getAllServers(): ArrayList<ServerModels> {
        val serverList: ArrayList<ServerModels> = ArrayList()
        val selectQuery = "SELECT * FROM $TBL_SERVER"
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
        var address: String
        var name: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                address = cursor.getString(cursor.getColumnIndex("address"))
                name = cursor.getString(cursor.getColumnIndex("name"))

                val servers = ServerModels(id = id, address = address, name = name)
                serverList.add(servers)
            } while (cursor.moveToNext())
        }

        return serverList

    }

    fun updateServer(id: Int, address: String, name:String) : Int{
        var db = this.writableDatabase
        var contentValues = ContentValues()
        contentValues.put(ID, id)
        contentValues.put("address", address)
        contentValues.put("name", name)

        val success = db.update(TBL_SERVER, contentValues, "id="+id,  null)
        db.close()
        return success
    }

    fun deleteServer(id: Int): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TBL_SERVER, "id=$id", null)
        db.close()
        return success
    }
}