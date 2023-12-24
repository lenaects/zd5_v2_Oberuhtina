package com.example.zd5_up_versia3

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.adapt.GroupGenerAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GroupGenerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GroupGenerFragment : Fragment() {
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
    private lateinit var recyclerViewGroupGener: RecyclerView
    private lateinit var adapter: GroupGenerAdapter
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment=inflater.inflate(R.layout.fragment_group_gener, container, false)
        // Инициализируем DatabaseHelper
        databaseHelper = DatabaseHelper(requireContext())
        // Получаем данные из базы данных
        val groupGenerList = databaseHelper.getAllGroupGenerData()

        // Находим RecyclerView в макете
        recyclerViewGroupGener = fragment.findViewById(R.id.recyclerView)

        // Инициализируем адаптер с обработчиком нажатия
        adapter = GroupGenerAdapter(groupGenerList ) { selectedGroupGener ->
            val sharedPreferences = requireContext().getSharedPreferences("SharedGener",
                AppCompatActivity.MODE_PRIVATE
            )
            val editor: SharedPreferences.Editor = sharedPreferences.edit()

            editor.putString("gener", selectedGroupGener.gener)
            editor.apply()

            Toast.makeText(requireContext(), "Выбран жанр: ${selectedGroupGener.gener}", Toast.LENGTH_SHORT).show()

            val intent= Intent(requireContext(), MovieUserActivity::class.java)
            intent.putExtra("selectedGener", selectedGroupGener.gener)
            startActivity(intent)
        }

        recyclerViewGroupGener.adapter = adapter
        recyclerViewGroupGener.layoutManager = LinearLayoutManager(requireContext())
        return fragment
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovieFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GroupGenerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}