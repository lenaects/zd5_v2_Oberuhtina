package com.example.zd5_up_versia3

import android.app.AlertDialog
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.adapt.MovieWorkerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieMoviesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieMoviesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieWorkerAdapter
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var addbut: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment=inflater.inflate(R.layout.fragment_movie_movies, container, false)
        databaseHelper = DatabaseHelper(requireContext())
        recyclerView = fragment.findViewById(R.id.movierecycleView)
        printList()
        //добавление
        addbut = fragment.findViewById(R.id.buttonadd)
        addbut.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Введите данные")
            // Создайте контейнер LinearLayout для размещения двух EditText
            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL
            // EditText для ссылки на картинку
            val EditImg = EditText(requireContext())
            EditImg.hint = "Вставьте ссылку на картинку"
            layout.addView(EditImg)

            // EditText для названия фильма
            val EditTitle = EditText(requireContext())
            EditTitle.hint = "Название фильма"
            layout.addView(EditTitle)

            // Spinner для жанра
            val generSpinner = Spinner(requireContext())
            val generAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.genre_array,
                android.R.layout.simple_spinner_item
            )
            generAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            generSpinner.adapter = generAdapter
            layout.addView(generSpinner)

            // Spinner для возрастного ограничения
            val limitationSpinner = Spinner(requireContext())
            val limitationAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.limitation_array,
                android.R.layout.simple_spinner_item
            )
            limitationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            limitationSpinner.adapter = limitationAdapter
            layout.addView(limitationSpinner)

            builder.setView(layout)

            // Установите кнопку "OK" для сохранения данных
            builder.setPositiveButton("OK") { dialog, _ ->
                val img = EditImg.text.toString()
                val title = EditTitle.text.toString()
                val gener = generSpinner.selectedItem.toString()
                val limitation = limitationSpinner.selectedItem.toString()

                // Обработка введенных данных и запись в БД
                if (img.isEmpty() || title.isEmpty() || gener.isEmpty() || limitation.isEmpty()) {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Не сохранено")
                        .setMessage("Необходимо заполнить все поля")
                        .setPositiveButton("ОК") { dialog, id -> dialog.cancel() }
                    builder.create()
                } else {
                    if (!databaseHelper.isGenerGroupExists(gener)) {
                        databaseHelper.addGroupGenerData(gener)
                    }
                    databaseHelper.addMovieData(img, title, gener, limitation)
                    printList()
                }
            }

            builder.setNegativeButton("Отмена") { dialog, _ ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }
        return fragment
    }
    fun printList(){
        // Получаем данные из базы данных
        val movieList = databaseHelper.getAllMovieData()


        // Создаем и устанавливаем менеджер компоновки для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Создаем экземпляр адаптера и устанавливаем его для RecyclerView
        movieAdapter = MovieWorkerAdapter(movieList,
            onDeleteClickListener = { deletedMovie ->
                val geners: String = deletedMovie.gener
                // Вызываем метод для удаления
                databaseHelper.deleteMovieData(deletedMovie.id)
                //провеки для GroupCountry
                if (!databaseHelper.isTMovieGenerExists(geners)) {
                    databaseHelper.deleteGroupGenerByName(geners)
                }
                databaseHelper.deleteMovieData(deletedMovie.id)
                printList()
            },
            onEditClickListener = { editedMovie ->
                val movie: Array<String> = arrayOf(
                    editedMovie.id.toString(),
                    editedMovie.img,
                    editedMovie.title,
                    editedMovie.gener,
                    editedMovie.limitation
                )
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Введите данные")

                // Создайте контейнер LinearLayout для размещения элементов ввода и Spinner'ов
                val layout = LinearLayout(requireContext())
                layout.orientation = LinearLayout.VERTICAL

                // EditText для ссылки на картинку
                val EditImg = EditText(requireContext())
                EditImg.hint = "Вставьте ссылку на картинку"
                EditImg.setText(movie[1])
                layout.addView(EditImg)

                // EditText для названия фильма
                val EditTitle = EditText(requireContext())
                EditTitle.hint = "Название фильма"
                EditTitle.setText(movie[2])
                layout.addView(EditTitle)

                // Spinner для жанра
                val generSpinner = Spinner(requireContext())
                val generAdapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.genre_array,
                    android.R.layout.simple_spinner_item
                )
                generAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                generSpinner.adapter = generAdapter
                val generPosition = generAdapter.getPosition(movie[3])
                generSpinner.setSelection(generPosition)
                layout.addView(generSpinner)

                // Spinner для возрастного ограничения
                val limitationSpinner = Spinner(requireContext())
                val limitationAdapter = ArrayAdapter.createFromResource(
                    requireContext(),
                    R.array.limitation_array,
                    android.R.layout.simple_spinner_item
                )
                limitationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                limitationSpinner.adapter = limitationAdapter
                val limitationPosition = limitationAdapter.getPosition(movie[4])
                limitationSpinner.setSelection(limitationPosition)
                layout.addView(limitationSpinner)

                builder.setView(layout)

                builder.setPositiveButton("OK") { dialog, _ ->
                    var img = EditImg.text.toString()
                    var titel = EditTitle.text.toString()
                    var gener = generSpinner.selectedItem.toString()
                    var limitation = limitationSpinner.selectedItem.toString()

                    val id = movie[0]
                    if (img.isEmpty()) {
                        img = movie[1]
                    }
                    if (titel.isEmpty()) {
                        titel = movie[2]
                    }

                    // Изменения в бд
                    databaseHelper.updateMovieData(
                        id.toLong(),
                        img,
                        titel,
                        gener,
                        limitation
                    )

                    // Проверки для GroupCountry
                    if (!databaseHelper.isTMovieGenerExists(movie[3])) {
                        databaseHelper.deleteGroupGenerByName(movie[3])
                    }
                    if (!databaseHelper.isGenerGroupExists(gener)) {
                        databaseHelper.addGroupGenerData(gener)
                    }

                    printList()
                }

                builder.setNegativeButton("Отмена") { dialog, _ ->
                    dialog.cancel()
                }

                val dialog = builder.create()
                dialog.show()
            }
        )
        recyclerView.adapter =  movieAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovieMoviesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieMoviesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}