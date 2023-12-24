package com.example.zd5_up_versia3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class RegistrationActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private val sharedPrefFile = "com.example.sharedPrefFile"
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // Инициализируем DatabaseHelper
        databaseHelper = DatabaseHelper(this)
        val iHaveAccountButton: Button = findViewById(R.id.iHaveAccountButton)
        iHaveAccountButton.setOnClickListener {
            val intent = Intent(this, FragmentUzerVhod::class.java)
            startActivity(intent)
        }

        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        val registerButton: Button = findViewById(R.id.registerButton)
        registerButton.setOnClickListener {
            val rePasswordEditText: EditText = findViewById(R.id.re_passwordEditText)
            val emailEditText: EditText = findViewById(R.id.emailEditText)
            val passwordEditText: EditText = findViewById(R.id.passwordEditText)
            val loginEditText: EditText =findViewById(R.id.loginEditText)
            if (loginEditText.text.toString().isEmpty() || emailEditText.text.toString().isEmpty() || passwordEditText.text.toString().isEmpty() ||
                rePasswordEditText.text.toString().isEmpty()) {

                Toast.makeText(this, "Заполните все поля для ввода.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (passwordEditText.text.toString() != rePasswordEditText.text.toString()){
                Toast.makeText(this, "Пароли не совпадают.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (!isEmailValid(emailEditText.text.toString())) {
                Toast.makeText(this, "Невверный ввод почты.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            else{
                if(!databaseHelper.isRegisterUser(loginEditText.text.toString(), emailEditText.text.toString())){
                    Toast.makeText(this, "Вы успешно зарегистировались", Toast.LENGTH_SHORT).show()
                    databaseHelper.addUserData(loginEditText.text.toString(), emailEditText.text.toString(), passwordEditText.text.toString())
                    val intent = Intent(this, FragmentUzerVhod::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, "Ошибка. Пользователь с таким логином или email уже найден", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun isEmailValid(email: String): Boolean {
        val pattern = Regex("[^@]+@[^@]+\\.[^@]+")
        return pattern.matches(email)
    }
}