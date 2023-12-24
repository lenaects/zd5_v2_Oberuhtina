package com.example.zd5_up_versia3

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.api.FillScreen
import com.example.zd5_up_versia3.api.Movie
import com.example.zd5_up_versia3.api.MyObjTwo
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OldMovieFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OldMovieFragment : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment=inflater.inflate(R.layout.fragment_old_movie, container, false)
        val apiKey = "46fc067b"
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiUrl = "https://www.omdbapi.com/?apikey=$apiKey&s=movie&type=movie&plot=full&r=json&y=2023"
                val response = URL(apiUrl).readText()

                val gson = Gson()
                val data = gson.fromJson(response, Map::class.java)

                if (data.containsKey("Search")) {
                    val movies = (data["Search"] as List<Map<String, Any>>).take(6)

                    val answer = mutableListOf<Movie>()

                    for (movie in movies) {
                        val id = "${movie["imdbID"]}"
                        val img = "${movie["Poster"]}"
                        val title = "${movie["Title"]}"
                        val detailedApiUrl = "https://www.omdbapi.com/?apikey=$apiKey&i=$id&plot=full&r=json"

                        val detailedResponse = URL(detailedApiUrl).readText()
                        val detailedData = gson.fromJson(detailedResponse, JsonObject::class.java)

                        val gener = "${detailedData["Genre"]}"
                        val time = "${detailedData["Runtime"]}"

                        val wr: Movie = Movie(id, img, title, gener, time)
                        answer.add(wr)
                    }

                    withContext(Dispatchers.Main) {
                        val rec: RecyclerView = fragment.findViewById(R.id.recyclerView2)
                        val fillScreenAdapter = FillScreen(requireContext(), MyObjTwo(answer).list)
                        rec.layoutManager = GridLayoutManager(requireContext(), 1)
                        rec.adapter = fillScreenAdapter
                        fillScreenAdapter.notifyDataSetChanged()
                    }
                } else {
                    Log.e(ContentValues.TAG, "Не удалось получить данные. Пожалуйста, проверьте ваш API-ключ и запрос.")
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Ошибка при выполнении запроса: ${e.message}")
            }
        }
        return fragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OldMovieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OldMovieFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}