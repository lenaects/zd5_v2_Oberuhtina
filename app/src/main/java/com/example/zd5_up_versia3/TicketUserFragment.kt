package com.example.zd5_up_versia3

import android.os.Bundle
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.adapt.ZakazUserAdapter
import com.example.zd5_up_versia3.dataclass.Movies
import com.example.zd5_up_versia3.dataclass.Zakaz
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TicketUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TicketUserFragment : Fragment() {
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
    private lateinit var zakazUserAdapter: ZakazUserAdapter
    private lateinit var databaseHelper: DatabaseHelper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databaseHelper = DatabaseHelper(requireContext())

        val fragment = inflater.inflate(R.layout.fragment_ticket_user, container, false)
        recyclerView = fragment.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val currentUserLogin = databaseHelper.getCurrentUserLogin()
        if (currentUserLogin.isNullOrEmpty()) {
            return fragment
        }
        val zakazList = databaseHelper.getZakazDataForUser(currentUserLogin)
        zakazUserAdapter = ZakazUserAdapter(zakazList, currentUserLogin)
        recyclerView.adapter = zakazUserAdapter
        return fragment
    }
    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val currentUserLogin = databaseHelper.getCurrentUserLogin()
        if (currentUserLogin.isNullOrEmpty()) {
            return
        }
        val zakazList = databaseHelper.getZakazDataForUser(currentUserLogin)
        zakazUserAdapter.setData(zakazList)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TicketUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TicketUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}