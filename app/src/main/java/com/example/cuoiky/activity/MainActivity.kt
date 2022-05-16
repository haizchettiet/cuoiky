package com.example.cuoiky.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cuoiky.R
import com.example.cuoiky.adapter.QuizAdapter
import com.example.cuoiky.databinding.ActivityMainBinding
import com.example.cuoiky.model.Quiz
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    lateinit var adapter: QuizAdapter
    private var quizList = mutableListOf<Quiz>()
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        populate()
        setUpViews()
    }

    private fun populate() {
        quizList.add(Quiz("1", "What is the capital of Vietnam?", ))
        quizList.add(Quiz("2", "What is the capital of Vietnam?", ))
    }

    fun setUpViews() {
        setUpFireStore()
        setUpRecyclerView()
    }

    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collection = firestore.collection("quizz")
        collection.addSnapshotListener { value, error ->
            if(value == null || error != null) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("TAG", value.toObjects(Quiz::class.java).toString())
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        binding.quizRecyclerView.layoutManager = GridLayoutManager(this, 2)
        binding.quizRecyclerView.adapter = adapter
    }

}
