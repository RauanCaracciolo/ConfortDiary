package edu.ifsp.com.br.confortdiary.model
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLiteHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_DAY (
                id TEXT PRIMARY KEY,
                photo TEXT,
                text TEXT,
                mood TEXT,
                time INTEGER
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DAY")
        onCreate(db)
    }

    fun insertDay(day: Day): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", day.id)
            put("photo", day.photo)
            put("text", day.text)
            put("mood", day.mood.name)
            put("time", day.time)
        }
        val result = db.insert(TABLE_DAY, null, values)
        db.close()
        return result != -1L
    }

    fun getDayById(id: String): Day? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_DAY, null, "id = ?", arrayOf(id),
            null, null, null
        )
        return if (cursor.moveToFirst()) {
            val photo = cursor.getString(cursor.getColumnIndexOrThrow("photo"))
            val text = cursor.getString(cursor.getColumnIndexOrThrow("text"))
            val mood = Mood.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("mood")))
            val time = cursor.getLong(cursor.getColumnIndexOrThrow("time"))
            cursor.close()
            db.close()
            Day(id, photo, text, mood, time)
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    companion object {
        private const val DATABASE_NAME = "confort_diary.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_DAY = "day"
    }
}
