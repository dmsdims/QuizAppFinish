package com.example.quizapp.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.codepolitan.quizapp.databinding.ActivityMainBinding
import com.example.quizapp.repository.Repository
import com.example.quizapp.ui.prepare.PrepareActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        onClick()
        val contents = Repository.getDataContents(this)
        Log.d("MainActivity", "onCreate: ${contents?.size}")
        Log.d("MainActivity", "onCreate: ${contents?.get(0)?.answers?.get(0)?.answer}")

    }

    private fun onClick() {
        mainBinding.btnPlay.setOnClickListener{
            startActivity(Intent(this, PrepareActivity::class.java))
        }
    }
}