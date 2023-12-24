package com.example.zd5_up_versia3

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.adapt.MovieUserAdapter
import com.example.zd5_up_versia3.dataclass.Movies
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.LinearLayout
import java.text.SimpleDateFormat
import java.util.*

class MovieUserActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieUserAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private var selectedDate: Calendar? = null
    private var selectedTime: String? = null
    private lateinit var currentUser: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_user)
        // Инициализируем DatabaseHelper
        databaseHelper = DatabaseHelper(this)

        // Находим RecyclerView в макете
        recyclerView = findViewById(R.id.recyclerView)

        // Создаем и устанавливаем менеджер компоновки для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Создаем экземпляр адаптера
        movieAdapter = MovieUserAdapter ( onBuyClickListener ={ butMovie ->
            // Вызываем метод для удаления
            Toast.makeText(this, "Вы забронировали билет на фильм: ${butMovie.title}", Toast.LENGTH_SHORT).show()

            // сохраняем фильм в sharedpreferences для корзины
            val movie = Movies( butMovie.id,  butMovie.img,  butMovie.title, butMovie.gener, butMovie.limitation)

            // Инициализируем список или получаем его из SharedPreferences
            val sharedPreferences = getSharedPreferences("ShopMovie", MODE_PRIVATE)
            val gson = Gson()
            val movieListJson = sharedPreferences.getString("movieList", "")
            val movieListType = object : TypeToken<List<Movies>>() {}.type
            var movieList: MutableList<Movies> = gson.fromJson(movieListJson, movieListType) ?: mutableListOf()

            // Добавляем новый фильм в список
            movieList.add(movie)

            // Сериализуем список и сохраняем его в SharedPreferences
            val updatedMovieListJson = gson.toJson(movieList)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putString("movieList", updatedMovieListJson)
            editor.apply()

        },
            onBookClickListener = { butMovie ->
                showSessionTimeDialog(butMovie)
            }
        )
        // Устанавливаем адаптер для RecyclerView
        recyclerView.adapter = movieAdapter

        // Загружаем и отображаем фильмы по умолчанию
        val sharedPreferences = getSharedPreferences("SharedGener", MODE_PRIVATE)
        val gener = sharedPreferences.getString("gener", "DefaultValueIfNotPresent")
        updateList(gener.toString())
        // Получаем текущего пользователя
        currentUser = databaseHelper.getCurrentUserLogin()
    }
    private fun updateList(gener: String) {
        // Получаем данные из базы данных
        val movieList = databaseHelper.getAllMovieData()

        // Обновляем данные в адаптере с учетом фильма для фильтрации
        movieAdapter.setData(movieList, gener)
    }
    private fun showSessionTimeDialog(movie: Movies) {
        if (movie.limitation == "18+") {
            showAgeRestrictionDialog(movie)
        } else {
            showDateTimeSelectionDialog(movie)
        }
    }

    private fun showAgeRestrictionDialog(movie: Movies) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Внимание")
        builder.setMessage("Данный фильм является 18+. Просим при покупке предоставить документ, подтверждающий возраст.")
        builder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
            showDateTimeSelectionDialog(movie)
        }

        builder.show()
    }

    private fun showDateTimeSelectionDialog(movie: Movies) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Выберите дату и время сеанса")
        val sessionTimes = arrayOf("11:00", "13:50", "16:40", "19:30", "22:20", "1:10")
        val timeSpinner = Spinner(this)
        timeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, sessionTimes)
        val currentDate = Calendar.getInstance()
        currentDate.add(Calendar.DAY_OF_MONTH, 1) // Start from tomorrow
        val maxDate = Calendar.getInstance()
        maxDate.add(Calendar.MONTH, 1)
        val datePicker = DatePicker(this)
        datePicker.minDate = currentDate.timeInMillis // Set minimum date
        datePicker.maxDate = maxDate.timeInMillis // Set maximum date
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(timeSpinner)
        layout.addView(datePicker)

        builder.setView(layout)

        builder.setPositiveButton("Забронировать") { dialog, which ->
            val selectedTime = sessionTimes[timeSpinner.selectedItemPosition]
            this.selectedTime = selectedTime

            // Get the selected date from the DatePicker
            val selectedDate = Calendar.getInstance()
            selectedDate.set(datePicker.year, datePicker.month, datePicker.dayOfMonth)
            this.selectedDate = selectedDate
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate.time)
            val reservationMessage =
                "Билет забронирован. Подойдите к кассе для оплаты билета за 20 минут до начала сеанса."

            showReservationConfirmationDialog(reservationMessage, movie, currentUser)
        }

        builder.setNegativeButton("Отмена") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }
    private fun showReservationConfirmationDialog(message: String, movie: Movies, currentUser: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Бронь подтверждена")
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which ->
            saveReservationToDatabase(movie, currentUser)
            dialog.dismiss()
        }

        builder.show()
    }
    private fun saveReservationToDatabase(movie: Movies, currentUser: String) {
        val databaseHelper = DatabaseHelper(this)

        // Сохраняем резервацию в базу данных
        selectedDate?.let {
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it.time)
            databaseHelper.addZakazData(currentUser, movie.title, selectedTime ?: "", formattedDate)
            Toast.makeText(this, "Бронь подтверждена. Ожидайте оплаты.", Toast.LENGTH_SHORT).show()
        }
    }

}