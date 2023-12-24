package com.example.zd5_up_versia3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentWorker.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentWorker : Fragment() {
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
    private lateinit var loginText: EditText
    private lateinit var emailText : EditText
    private lateinit var passwordText: EditText
    private lateinit var vhod: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment=inflater.inflate(R.layout.fragment_worker, container, false)
        val sharedPreferences = requireContext().getSharedPreferences("agent", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("log", "movie")
        editor.putString("email", "movie@mail.ru")
        editor.putString("pass", "movie")
        editor.apply()

        loginText = fragment.findViewById(R.id.loginwEditText)
        emailText = fragment.findViewById(R.id.emailwEditText)
        passwordText = fragment.findViewById(R.id.passwordwEditText)
        vhod = fragment.findViewById(R.id.signInwButton)
        vhod.setOnClickListener{
            val sharedPreferences = requireContext().getSharedPreferences("agent", Context.MODE_PRIVATE)
            val savedLogin = sharedPreferences.getString("log", "")
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPass = sharedPreferences.getString("pass", "")
            if(loginText.text.toString() == savedLogin.toString() && passwordText.text.toString() == savedPass.toString() && emailText.text.toString() == savedEmail.toString()){
                val intent= Intent(requireContext(), EmployeeModeActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(requireContext(), "Ошибка. Неверные данные", Toast.LENGTH_SHORT).show()
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
         * @return A new instance of fragment FragmentWorker.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentWorker().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}