package com.example.zd5_up_versia3

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.zd5_up_versia3.dataclass.GroupMovie
import com.example.zd5_up_versia3.dataclass.Movies
import com.example.zd5_up_versia3.dataclass.Users
import com.example.zd5_up_versia3.dataclass.Zakaz
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class DatabaseHelper (private val appContext: Context) : SQLiteOpenHelper(appContext, DATABASE_NAME, null, DATABASE_VERSION){
    companion object {
        const val DATABASE_NAME = "BDMovies.db"
        const val DATABASE_VERSION = 1

        const val TABLE_USER = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_Login = "login"
        const val COLUMN_Email = "email"
        const val COLUMN_Password = "password"

        const val TABLE_MOVIE = "movie"
        const val COLUMN_ID_MOVIE = "id"
        const val COLUMN_Img = "img"
        const val COLUMN_Title = "title"
        const val COLUMN_Gener = "gener"
        const val COLUMN_Limitation = "limitation"

        const val TABLE_GroupGener = "groupGener"
        const val COLUMN_ID_GroupGener = "id"
        const val COLUMN_GroupGener = "gener"

        const val TABLE_Zakaz = "zakaz"
        const val COLUMN_ID_Zakaz = "id"
        const val COLUMN_Login_User_Zakaz = "loginUser"
        const val COLUMN_Titel_Zakaz = "titlMovie"
        const val COLUMN_Time_Zakaz = "time"
        const val COLUMN_Data_Zakaz = "data"

    }
    init {
        // Переместите вызов копирования базы данных сюда
        copyDatabaseFromAssets(appContext)
    }
    override fun onCreate(db: SQLiteDatabase) {
        try {
            Log.d("MyLog", "Creating Horoscope table...")

            // Проверяем, существует ли таблица
            val checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_USER'"
            val cursor = db.rawQuery(checkTableQuery, null)
            val tableExists = cursor.count > 0
            cursor.close()

            if (!tableExists) {
                // Если таблица не существует, вызываем метод для копирования базы данных из assets
                copyDatabaseFromAssets(appContext)
                Log.d("MyLog", "Users table does not exist. Copied from assets.")
            } else {
                Log.d("MyLog", "Users table already exists.")
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error checking Users table: ${e.message}")
            e.printStackTrace()
        }
    }
    private fun checkDatabaseExists(context: Context): Boolean {
        val dbPath = context.getDatabasePath(DATABASE_NAME)
        return dbPath.exists()
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // При необходимости можно добавить код для обновления базы данных при изменении версии
    }
    private fun copyDatabaseFromAssets(context: Context) {
        val dbPath = context.getDatabasePath(DATABASE_NAME).absolutePath
        if (!checkDatabaseExists(context)) {
            try {
                val inputStream: InputStream = context.assets.open(DATABASE_NAME)
                val outputStream = FileOutputStream(dbPath)

                val buffer = ByteArray(1024)
                var length: Int

                while ((inputStream.read(buffer)).also { length = it } > 0) {
                    outputStream.write(buffer, 0, length)
                }

                outputStream.flush()
                outputStream.close()
                inputStream.close()

                Log.d("MyLog", "Database copied from assets successfully.")
            } catch (e: IOException) {
                Log.e("MyLog", "Error copying database from assets: ${e.message}")
            }
        }
        val database = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE)
        val cursor = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

        while (cursor.moveToNext()) {
            val tableName = cursor.getString(0)
            Log.d("MyLog", "Table Name: $tableName")
        }

        cursor.close()
        database.close()

    }

    //пользователи
    fun getCurrentUserLogin(): String {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USER LIMIT 1"
        val cursor = db.rawQuery(selectQuery, null)

        var currentUserLogin = ""

        try {
            if (cursor.moveToFirst()) {
                val loginColumnIndex = cursor.getColumnIndex(COLUMN_Login)
                if (loginColumnIndex != -1) {
                    currentUserLogin = cursor.getString(loginColumnIndex)
                }
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error while retrieving current user login: ${e.message}")
        } finally {
            cursor.close()
            db.close() // Close the database connection in the finally block
        }

        return currentUserLogin
    }
    fun isUserExist(login: String, email: String, password: String): Boolean {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USER WHERE $COLUMN_Login = ? AND $COLUMN_Email = ? AND $COLUMN_Password = ?"
        val selectionArgs = arrayOf(login, email, password)

        val cursor = db.rawQuery(selectQuery, selectionArgs)
        val userExists = cursor.count > 0
        cursor.close()
        db.close()

        return userExists
    }
    // Метод для проверки наличия пользователя с заданным логином или email
    fun isRegisterUser(login: String, email: String): Boolean {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USER WHERE $COLUMN_Login = ? OR $COLUMN_Email = ?"
        val selectionArgs = arrayOf(login, email)

        val cursor = db.rawQuery(selectQuery, selectionArgs)
        val userExists = cursor.count > 0
        cursor.close()
        db.close()

        return userExists
    }

    fun getAllUserData(): List<Users> {
        val horoscopeDataList = mutableListOf<Users>()
        val db = readableDatabase

        // Перед выполнением запроса, проверим снова, существует ли таблица
        val checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_USER'"
        val cursor = db.rawQuery(checkTableQuery, null)
        val tableExists = cursor.count > 0
        cursor.close()

        if (!tableExists) {
            // Если таблица не существует, вызываем метод для копирования базы данных из assets
            copyDatabaseFromAssets(appContext)
            Log.d("MyLog", "Users table does not exist. Copied from assets.")
        } else {
            Log.d("MyLog", "Users table already exists.")
        }

        val selectQuery = "SELECT * FROM $TABLE_USER"
        val selectCursor: Cursor = db.rawQuery(selectQuery, null)

        try {
            while (selectCursor.moveToNext()) {
                val idColumnIndex = selectCursor.getColumnIndex(COLUMN_ID)
                val loginColumnIndex = selectCursor.getColumnIndex(COLUMN_Login)
                val emailColumnIndex = selectCursor.getColumnIndex(COLUMN_Email)
                val passwordColumnIndex = selectCursor.getColumnIndex(COLUMN_Password)

                if (idColumnIndex != -1 && loginColumnIndex != -1 && emailColumnIndex != -1 && passwordColumnIndex != -1) {
                    val id = selectCursor.getLong(idColumnIndex)
                    val login = selectCursor.getString(loginColumnIndex)
                    val email = selectCursor.getString(emailColumnIndex)
                    val password = selectCursor.getString(passwordColumnIndex)

                    val horoscopeData = Users(id, login, email, password)
                    horoscopeDataList.add(horoscopeData)
                } else {
                    Log.e("MyLog", "Column index not found")
                }
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error while reading data from the database: ${e.message}")
        } finally {
            selectCursor.close()
            db.close()
        }

        return horoscopeDataList
    }


    fun addUserData(login: String, email: String, password: String) {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_Login, login)
            put(COLUMN_Email, email)
            put(COLUMN_Password, password)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }
    fun deleteUserData(userId: Long) {
        val db = writableDatabase

        try {
            // Условие для удаления записи с определенным ID
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(userId.toString())

            // Выполняем запрос на удаление
            db.delete(TABLE_USER, whereClause, whereArgs)
            Log.d("MyLog", "User with ID $userId deleted successfully.")
        } catch (e: Exception) {
            Log.e("MyLog", "Error while deleting user: ${e.message}")
        } finally {
            db.close()
        }
    }
    fun updateUserData(userId: Long, newLogin: String, newEmail: String, newPassword: String) {
        val db = writableDatabase

        try {
            // Условие для обновления записи с определенным ID
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(userId.toString())

            // Создаем объект ContentValues с обновленными данными
            val values = ContentValues().apply {
                put(COLUMN_Login, newLogin)
                put(COLUMN_Email, newEmail)
                put(COLUMN_Password, newPassword)
            }

            // Выполняем запрос на обновление
            db.update(TABLE_USER, values, whereClause, whereArgs)
            Log.d("MyLog", "User with ID $userId updated successfully.")
        } catch (e: Exception) {
            Log.e("MyLog", "Error while updating user: ${e.message}")
        } finally {
            db.close()
        }
    }
    //Фильмы
    fun getAllMovieData(): List<Movies> {
        val movieDataList = mutableListOf<Movies>()
        val db = readableDatabase

        // Перед выполнением запроса, проверим снова, существует ли таблица
        val checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_MOVIE'"
        val cursor = db.rawQuery(checkTableQuery, null)
        val tableExists = cursor.count > 0
        cursor.close()

        if (!tableExists) {
            // Если таблица не существует, вызываем метод для копирования базы данных из assets
            copyDatabaseFromAssets(appContext)
            Log.d("MyLog", "Tur table does not exist. Copied from assets.")
        } else {
            Log.d("MyLog", "Tur table already exists.")
        }

        val selectQuery = "SELECT * FROM $TABLE_MOVIE"
        val selectCursor: Cursor = db.rawQuery(selectQuery, null)

        try {
            while (selectCursor.moveToNext()) {
                val idColumnIndex = selectCursor.getColumnIndex(COLUMN_ID_MOVIE)
                val imgColumnIndex = selectCursor.getColumnIndex(COLUMN_Img)
                val titleColumnIndex = selectCursor.getColumnIndex(COLUMN_Title)
                val generColumnIndex = selectCursor.getColumnIndex(COLUMN_Gener)
                val limitationColumnIndex = selectCursor.getColumnIndex(COLUMN_Limitation)
                if (idColumnIndex != -1 && imgColumnIndex != -1 && titleColumnIndex != -1
                    && generColumnIndex != -1 && limitationColumnIndex != -1) {
                    val id = selectCursor.getLong(idColumnIndex)
                    val img = selectCursor.getString(imgColumnIndex)
                    val title = selectCursor.getString(titleColumnIndex)
                    val gener = selectCursor.getString(generColumnIndex)
                    val limitation = selectCursor.getString(limitationColumnIndex)
                    val movieData = Movies(id, img, title, gener, limitation)
                    movieDataList.add(movieData)
                } else {
                    Log.e("MyLog", "Column index not found")
                }
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error while reading data from the database: ${e.message}")
        } finally {
            selectCursor.close()
            db.close()
        }

        return movieDataList
    }

    fun deleteMovieData(tursId: Long) {
        val db = writableDatabase

        try {
            // Условие для удаления записи с определенным ID
            val whereClause = "$COLUMN_ID_MOVIE = ?"
            val whereArgs = arrayOf(tursId.toString())

            // Выполняем запрос на удаление
            db.delete(TABLE_MOVIE, whereClause, whereArgs)
            Log.d("MyLog", "User with ID $tursId deleted successfully.")
        } catch (e: Exception) {
            Log.e("MyLog", "Error while deleting user: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun updateMovieData(movieId: Long, img: String,  title: String, gener: String, limitation: String) {
        val db = writableDatabase

        try {
            // Условие для обновления записи с определенным ID
            val whereClause = "$COLUMN_ID = ?"
            val whereArgs = arrayOf(movieId.toString())

            // Создаем объект ContentValues с обновленными данными
            val values = ContentValues().apply {
                put(COLUMN_Img, img)
                put(COLUMN_Title, title)
                put(COLUMN_Gener, gener)
                put(COLUMN_Limitation, limitation)
            }

            // Выполняем запрос на обновление
            db.update(TABLE_MOVIE, values, whereClause, whereArgs)
            Log.d("MyLog", "User with ID $movieId updated successfully.")
        } catch (e: Exception) {
            Log.e("MyLog", "Error while updating user: ${e.message}")
        } finally {
            db.close()
        }
    }

    fun addMovieData(img: String, title: String, gener: String, limitation: String) {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_Img, img)
            put(COLUMN_Title, title)
            put(COLUMN_Gener, gener)
            put(COLUMN_Limitation, limitation)
        }
        db.insert(TABLE_MOVIE, null, values)
        db.close()
    }
    //проверка на сущестование жанры в списке
    fun isTMovieGenerExists(gener: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_MOVIE WHERE $COLUMN_Gener = ?"
        val cursor = db.rawQuery(query, arrayOf(gener))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    //groupGener
    fun getAllGroupGenerData(): List<GroupMovie> {
        val generGroupDataList = mutableListOf<GroupMovie>()
        val db = readableDatabase

        // Перед выполнением запроса, проверим снова, существует ли таблица
        val checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_GroupGener'"
        val cursor = db.rawQuery(checkTableQuery, null)
        val tableExists = cursor.count > 0
        cursor.close()

        if (!tableExists) {
            // Если таблица не существует, вызываем метод для копирования базы данных из assets
            copyDatabaseFromAssets(appContext)
            Log.d("MyLog", "Users table does not exist. Copied from assets.")
        } else {
            Log.d("MyLog", "Users table already exists.")
        }

        val selectQuery = "SELECT * FROM $TABLE_GroupGener"
        val selectCursor: Cursor = db.rawQuery(selectQuery, null)

        try {
            while (selectCursor.moveToNext()) {
                val idColumnIndex = selectCursor.getColumnIndex(COLUMN_ID_GroupGener)
                val generColumnIndex = selectCursor.getColumnIndex(COLUMN_GroupGener)


                if (idColumnIndex != -1 && generColumnIndex != -1 ) {
                    val id = selectCursor.getLong(idColumnIndex)
                    val country= selectCursor.getString(generColumnIndex)

                    val generData = GroupMovie(id, country)
                    generGroupDataList.add(generData)
                } else {
                    Log.e("MyLog", "Column index not found")
                }
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error while reading data from the database: ${e.message}")
        } finally {
            selectCursor.close()
            db.close()
        }

        return generGroupDataList
    }
    fun addGroupGenerData(country: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GroupGener, country)

        }
        db.insert(TABLE_GroupGener, null, values)
        db.close()
    }
    //проверка на сущестование жанры в списке
    fun isGenerGroupExists(gener: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_GroupGener WHERE $COLUMN_GroupGener = ?"
        val cursor = db.rawQuery(query, arrayOf(gener))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }
    fun deleteGroupGenerByName(countryName: String) {
        val db = writableDatabase
        try {
            // Условие для удаления записи с определенным жанром
            val whereClause = "$COLUMN_GroupGener = ?"
            val whereArgs = arrayOf(countryName)

            // Выполняем запрос на удаление
            db.delete(TABLE_GroupGener, whereClause, whereArgs)
            Log.d("MyLog", "Country $countryName deleted from GroupCountry successfully.")
        } catch (e: Exception) {
            Log.e("MyLog", "Error while deleting country from GroupCountry: ${e.message}")
        } finally {
            db.close()
        }
    }
    //zakaz
    fun getAllZakazData(): List<Zakaz> {
        val zakazList = mutableListOf<Zakaz>()
        val db = readableDatabase

        // Перед выполнением запроса, проверим снова, существует ли таблица
        val checkTableQuery = "SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_Zakaz'"
        val cursor = db.rawQuery(checkTableQuery, null)
        val tableExists = cursor.count > 0
        cursor.close()

        if (!tableExists) {
            // Если таблица не существует, вызываем метод для копирования базы данных из assets
            copyDatabaseFromAssets(appContext)
            Log.d("MyLog", "Users table does not exist. Copied from assets.")
        } else {
            Log.d("MyLog", "Users table already exists.")
        }

        val selectQuery = "SELECT * FROM $TABLE_Zakaz"
        val selectCursor: Cursor = db.rawQuery(selectQuery, null)

        try {
            while (selectCursor.moveToNext()) {
                val idColumnIndex = selectCursor.getColumnIndex(COLUMN_ID_Zakaz)
                val CoulumnloginUser = selectCursor.getColumnIndex(COLUMN_Login_User_Zakaz)
                val ColumnntitelMovie = selectCursor.getColumnIndex(COLUMN_Titel_Zakaz)
                val Columntime = selectCursor.getColumnIndex(COLUMN_Time_Zakaz)
                val Columndata = selectCursor.getColumnIndex(COLUMN_Data_Zakaz)
                Log.d("MyLog", "id: ${selectCursor.getLong(idColumnIndex)}")
                Log.d("MyLog", "loginUser: ${selectCursor.getString(CoulumnloginUser)}")
                Log.d("MyLog", "titelMovie: ${selectCursor.getString(ColumnntitelMovie)}")
                Log.d("MyLog", "time: ${selectCursor.getString(Columntime)}")
                Log.d("MyLog", "time: ${selectCursor.getString(Columndata)}")


                if (idColumnIndex != -1 && CoulumnloginUser != -1 && ColumnntitelMovie != -1 && Columntime != -1&&Columndata!=-1 ) {
                    if (selectCursor.isNull(idColumnIndex) || selectCursor.isNull(CoulumnloginUser) || selectCursor.isNull( ColumnntitelMovie) || selectCursor.isNull(Columntime)|| selectCursor.isNull(Columndata) ) {
                        Log.e("MyLog", "Some columns have null values")
                    } else {
                        val id = selectCursor.getLong(idColumnIndex)
                        val login = selectCursor.getString(CoulumnloginUser)
                        val titelMovie = selectCursor.getString( ColumnntitelMovie)
                        val time = selectCursor.getString(Columntime)
                        val data = selectCursor.getString(Columndata)
                        val zakazData = Zakaz(id, login, titelMovie, time,data)
                        zakazList.add(zakazData)
                    }
                } else {
                    Log.e("MyLog", "Column index not found")
                }

            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error while reading data from the database: ${e.message}")
        } finally {
            selectCursor.close()
            db.close()
        }

        return zakazList
    }
    fun addZakazData(loginUser: String, titelMovie: String, time: String,data:String) {

        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_Login_User_Zakaz, loginUser)
            put(COLUMN_Titel_Zakaz, titelMovie)
            put(COLUMN_Time_Zakaz, time)
            put(COLUMN_Data_Zakaz, data)

        }
        db.insert(TABLE_Zakaz, null, values)
        db.close()
    }
    fun getZakazDataForUser(loginUser: String): List<Zakaz> {
        val zakazDataList = mutableListOf<Zakaz>()
        val db = readableDatabase

        val selectQuery = "SELECT * FROM $TABLE_Zakaz WHERE $COLUMN_Login_User_Zakaz = ?"
        val selectionArgs = arrayOf(loginUser)
        val selectCursor = db.rawQuery(selectQuery, selectionArgs)

        try {
            while (selectCursor.moveToNext()) {
                val idColumnIndex = selectCursor.getColumnIndex(COLUMN_ID_Zakaz)
                val loginUserColumnIndex = selectCursor.getColumnIndex(COLUMN_Login_User_Zakaz)
                val titlMovieColumnIndex = selectCursor.getColumnIndex(COLUMN_Titel_Zakaz)
                val timeColumnIndex = selectCursor.getColumnIndex(COLUMN_Time_Zakaz)
                val dataColumnIndex = selectCursor.getColumnIndex(COLUMN_Data_Zakaz)

                if (idColumnIndex != -1 && loginUserColumnIndex != -1 && titlMovieColumnIndex != -1 && timeColumnIndex != -1 && dataColumnIndex != -1) {
                    val id = selectCursor.getLong(idColumnIndex)
                    val loginUser = selectCursor.getString(loginUserColumnIndex)
                    val titlMovie = selectCursor.getString(titlMovieColumnIndex)
                    val time = selectCursor.getString(timeColumnIndex)
                    val data = selectCursor.getString(dataColumnIndex)

                    // Проверяем, чтобы loginUser из базы совпадал с переданным loginUser
                    if (loginUser == loginUser) {
                        val zakazData = Zakaz(id, loginUser, titlMovie, time, data)
                        zakazDataList.add(zakazData)
                    }
                } else {
                    Log.e("MyLog", "Column index not found")
                }
            }
        } catch (e: Exception) {
            Log.e("MyLog", "Error while reading zakaz data from the database: ${e.message}")
        } finally {
            selectCursor.close()
            db.close()
        }

        return zakazDataList
    }

}