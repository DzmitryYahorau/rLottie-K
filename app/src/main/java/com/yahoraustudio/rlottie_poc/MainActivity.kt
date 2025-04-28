package com.yahoraustudio.rlottie_poc

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.RecyclerView
import com.yahoraustudio.rlottie_poc.data.Repository
import com.yahoraustudio.rlottie_poc.grid.GridAdapter

class MainActivity : ComponentActivity(R.layout.activity_main) {

    companion object {
        init {
            System.loadLibrary("rlottie")
        }
    }

    private external fun stringFromJNI(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val rv = findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = GridAdapter()

//        val data = List(10) { index -> Repository.tgRes[index % Repository.tgRes.size] }
        adapter.submitList(Repository.tgAll)
        rv.adapter = adapter
    }
}