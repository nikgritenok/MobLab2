package com.example.rickandmorty

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var characterAdapter: CharacterAdapter
    private lateinit var viewModel: MainViewModel
    private var characterList: MutableList<Character> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(this)
        characterAdapter = CharacterAdapter(characterList)
        recyclerView.adapter = characterAdapter

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setupObservers()
        viewModel.fetchCharacters()
    }

    private fun setupObservers() {
        viewModel.characters.observe(this) { characters ->
            characterList.clear()
            characterList.addAll(characters)
            characterAdapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(this) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, "Ошибка: $it", Toast.LENGTH_LONG).show()
            }
        }
    }
}
