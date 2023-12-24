package com.example.zd5_up_versia3

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zd5_up_versia3.adapt.AdapterUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovieUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieUserFragment : Fragment() {
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
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var usersAdapter: AdapterUser
    private lateinit var recyclerView: RecyclerView
    private lateinit var addbut: Button
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragment=inflater.inflate(R.layout.fragment_movie_user, container, false)
        databaseHelper = DatabaseHelper(requireContext())

        //показываем список
        val navController= NavHostFragment.findNavController(this)

        recyclerView = fragment.findViewById(R.id.userrecycleView)
        addbut = fragment.findViewById(R.id.buttonadd)
        printList()

        addbut.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Введите данные")

            // Создайте контейнер LinearLayout для размещения двух EditText
            val layout = LinearLayout(requireContext())
            layout.orientation = LinearLayout.VERTICAL

            val EditLogin = EditText(requireContext())
            val EditEmail = EditText(requireContext())
            val EditPassword = EditText(requireContext())

            EditLogin.hint = "Введите логин"
            EditEmail.hint = "Введите email"
            EditPassword.hint = "Введите пароль"

            // Добавьте EditText к контейнеру
            layout.addView(EditLogin)
            layout.addView(EditEmail)
            layout.addView(EditPassword)

            builder.setView(layout)

            // Установите кнопку "OK" для сохранения данных
            builder.setPositiveButton("OK") { dialog, _ ->
                val login = EditLogin.text.toString()
                val email = EditEmail.text.toString()
                val password = EditPassword.text.toString()
                if (login.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Необходимо заполнить все поля", Toast.LENGTH_SHORT).show()
                } else if (databaseHelper.isRegisterUser(login, email)) {
                    Toast.makeText(requireContext(), "Пользователь с таким логином или email уже существует", Toast.LENGTH_SHORT).show()
                } else if (!isEmailValid(email)) {
                    Toast.makeText(requireContext(), "Неверный ввод почты", Toast.LENGTH_SHORT).show()
                } else {
                    databaseHelper.addUserData(login, email, password)
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
    private fun isEmailValid(email: String): Boolean {
        val pattern = Regex("[^@]+@[^@]+\\.[^@]+")
        return pattern.matches(email)
    }
    fun printList() {
        // Получаем данные из базы данных
        val userList = databaseHelper.getAllUserData()

        // Создаем и устанавливаем менеджер компоновки для RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Создаем экземпляр адаптера и устанавливаем его для RecyclerView
        usersAdapter = AdapterUser(
            userList,
            onDeleteClickListener = { deletedUser ->
                // Вызываем метод для удаления
                databaseHelper.deleteUserData(deletedUser.id)
                printList()
            },
            onEditClickListener = { editedUser ->
                var user: Array<String> = arrayOf(
                    editedUser.id.toString(),
                    editedUser.login,
                    editedUser.password,
                    editedUser.password
                )

                val builder = AlertDialog.Builder(requireActivity())
                builder.setTitle("Введите данные")

                // Создайте контейнер LinearLayout для размещения двух EditText
                val layout = LinearLayout(requireContext())
                layout.orientation = LinearLayout.VERTICAL

                val EditLogin = EditText(requireContext())
                val EditEmail = EditText(requireContext())
                val EditPassword = EditText(requireContext())

                EditLogin.hint = "Введите логин"
                EditEmail.hint = "Введите email"
                EditPassword.hint = "Введите пароль"

                EditLogin.setText(user[1])
                EditEmail.setText(user[2])
                EditPassword.setText(user[3])

                layout.addView(EditLogin)
                layout.addView(EditEmail)
                layout.addView(EditPassword)

                builder.setView(layout)

                builder.setPositiveButton("OK") { dialog, _ ->
                    var login = EditLogin.text.toString()
                    var email = EditEmail.text.toString()
                    var password = EditPassword.text.toString()
                    val id = user[0]
                    if (login.isEmpty()) {
                        login = user[1]
                    }
                    if (email.isEmpty()) {
                        email = user[2]
                    }
                    if (password.isEmpty()) {
                        password = user[3]
                    }

                    // изменения в бд
                    databaseHelper.updateUserData(id.toLong(), login, email, password)
                    // обновление листа
                    printList()
                }

                builder.setNegativeButton("Отмена") { dialog, _ ->
                    dialog.cancel()
                }

                val dialog = builder.create()
                dialog.show()
            }
        )
        recyclerView.adapter = usersAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MovieUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MovieUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}