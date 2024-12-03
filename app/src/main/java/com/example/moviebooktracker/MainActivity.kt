package com.example.moviebooktracker

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader
import java.io.FileWriter

data class MediaItem(
    val title: String,
    val genre: String,
    val review: String,
    val rating: Int,
    val type: String,
    var isCompleted: Boolean = false
)

class MainActivity : AppCompatActivity() {
    private val mediaList = mutableListOf<MediaItem>()
    private lateinit var adapter: MediaAdapter
    private val gson = Gson()
    private val dataFileName = "media_data.json"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleInput = findViewById<EditText>(R.id.inputTitle)
        val genreInput = findViewById<EditText>(R.id.inputGenre)
        val reviewInput = findViewById<EditText>(R.id.inputReview)
        val ratingSeekBar = findViewById<SeekBar>(R.id.seekBarRating)
        val typeRadioGroup = findViewById<RadioGroup>(R.id.radioGroupType)
        val addButton = findViewById<Button>(R.id.buttonAdd)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewMedia)

        loadMediaData()

        adapter = MediaAdapter(mediaList) { item -> showDetailsDialog(item) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        addButton.setOnClickListener {
            Toast.makeText(this, dataDir.path, Toast.LENGTH_SHORT).show()
            val title = titleInput.text.toString()
            val genre = genreInput.text.toString()
            val review = reviewInput.text.toString()
            val rating = ratingSeekBar.progress
            val selectedTypeId = typeRadioGroup.checkedRadioButtonId
            val type = if (selectedTypeId == R.id.radioButtonBook) "Book" else "Movie"

            if (title.isNotBlank() && genre.isNotBlank() && review.isNotBlank()) {
                val newItem = MediaItem(title, genre, review, rating, type)
                mediaList.add(newItem)
                adapter.notifyDataSetChanged()
                saveMediaData()
                titleInput.text.clear()
                genreInput.text.clear()
                reviewInput.text.clear()
                ratingSeekBar.progress = 0
            } else {
                Toast.makeText(this, "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDetailsDialog(item: MediaItem) {
        val dialog = AlertDialog.Builder(this)
            .setTitle(item.title)
            .setMessage("""
                Gatunek: ${item.genre}
                Recenzja: ${item.review}
                Ocena: ${item.rating}
                Typ: ${item.type}
                Ukończono: ${if (item.isCompleted) "Tak" else "Nie"}
            """.trimIndent())
            .setPositiveButton("Zamknij", null)
            .setNeutralButton("Zmień status") { _, _ ->
                item.isCompleted = !item.isCompleted
                adapter.notifyDataSetChanged()
                saveMediaData()
            }
            .setNegativeButton("Usuń") { _, _ ->
                showDeleteConfirmationDialog(item)
            }
            .create()
        dialog.show()
    }

    private fun showDeleteConfirmationDialog(item: MediaItem) {
        AlertDialog.Builder(this)
            .setTitle("Potwierdzenie usunięcia")
            .setMessage("Czy na pewno chcesz usunąć '${item.title}'?")
            .setPositiveButton("Tak") { _, _ ->
                mediaList.remove(item)
                adapter.notifyDataSetChanged()
                saveMediaData()
                Toast.makeText(this, "Usunięto pomyślnie", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Nie", null)
            .show()
    }

    private fun loadMediaData() {
        val file = File(filesDir, dataFileName)
        if (file.exists()) {
            val type = object : TypeToken<List<MediaItem>>() {}.type
            val reader = FileReader(file)
            val items: List<MediaItem> = gson.fromJson(reader, type)
            mediaList.addAll(items)
            reader.close()
        }
    }

    private fun saveMediaData() {
        val file = File(filesDir, dataFileName)
        val writer = FileWriter(file)
        gson.toJson(mediaList, writer)
        writer.close()
    }
}
