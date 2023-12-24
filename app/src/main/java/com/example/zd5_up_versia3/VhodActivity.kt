package com.example.zd5_up_versia3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.zd5_up_versia3.databinding.ActivityVhodBinding
import com.google.android.material.appbar.MaterialToolbar

class VhodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVhodBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVhodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {
            initializeViews()
        } catch (e: Exception) {
            Log.e("VhodScreen", "Error initializing views", e)
        }
    }
    private fun initializeViews() {
        val toolbar: MaterialToolbar = binding.toolbar

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment? ?: return
        navController = host.navController

        navController.graph = navController.navInflater.inflate(R.navigation.navigate_mode)

        setSupportActionBar(toolbar)

        // Устанавливаем контроллер для Toolbar
        toolbar.setupWithNavController(navController)

        // Настраиваем обработчик для перехода на предыдущий уровень
        toolbar.setNavigationOnClickListener {
            navController.navigateUp()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.munu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        try {
            when (item.itemId) {
                R.id.fragmentUzerVhod -> {
                    Log.d("Log", "Clicked on fragmentUzerVhod menu item")
                    findNavController(R.id.fragment)
                        .navigate(R.id.action_fragmentWorker_to_fragmentUzerVhod)
                    return true
                }

                R.id.fragmentWorker -> {
                    Log.d("Log", "Clicked on navturAhentVhod menu item")
                    findNavController(R.id.fragment)
                        .navigate(R.id.action_fragmentUzerVhod_to_fragmentWorker)
                    return true
                }
                else -> {
                    return super.onOptionsItemSelected(item)
                }
            }
        } catch (e: Exception) {
            Log.e("VhodScreen", "Error handling menu item click", e)
            return super.onOptionsItemSelected(item)
        }
    }
}