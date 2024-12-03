package com.example.moviebooktracker

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val title = findViewById<TextView>(R.id.title)
        val addtitle = findViewById<EditText>(R.id.addtitle)
        val addgenre = findViewById<EditText>(R.id.addgenre)
        val add = findViewById<EditText>(R.id.addreview)
        val addrating = findViewById<SeekBar>(R.id.addrating)
        val addnew = findViewById<Button>(R.id.addnew)
        val movieorbook = findViewById<RadioGroup>(R.id.movieorbook)

        movieorbook.setOnCheckedChangeListener{_, isChecked ->
            val radioButton = findViewById<RadioButton>(isChecked)
            title.text = radioButton.text.toString()
        }

        addnew.setOnClickListener{_, ->
            Toast.makeText(this, "Wypełnij dobrze wszystkie pola!", Toast.LENGTH_SHORT).show()
        }

    }
}