package com.example.zd5_up_versia3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.adapt.ZakazWorkerAdapter
import com.example.zd5_up_versia3.dataclass.Zakaz

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieTicketFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieTicketFragment : Fragment() {
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
    private lateinit var adapter: ZakazWorkerAdapter
    private lateinit var zakazList: List<Zakaz>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val  fragment=inflater.inflate(R.layout.fragment_movie_ticket, container, false)
        recyclerView = fragment.findViewById(R.id.tiketrecycleView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        zakazList = getZakazListFromDatabase()  // Получение списка бронирований из базы данных
        adapter = ZakazWorkerAdapter(zakazList)
        recyclerView.adapter = adapter
        return fragment
    }
    private fun getZakazListFromDatabase(): List<Zakaz> {
        val databaseHelper = DatabaseHelper(requireContext())
        return databaseHelper.getAllZakazData()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovieTicketFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieTicketFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}