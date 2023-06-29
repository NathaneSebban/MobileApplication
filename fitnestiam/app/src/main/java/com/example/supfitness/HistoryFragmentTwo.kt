package com.example.supfitness

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryFragmentTwo: Fragment() {

    private var adapter: RunAdapter? = null
    private lateinit var viewOfLayouthistory: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var sqliteHelper: SQLiteHelperRun


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreate(savedInstanceState)
        viewOfLayouthistory = inflater!!.inflate(R.layout.fragment_history_two, container, false)
        sqliteHelper = context?.let { SQLiteHelperRun(it) }!!

        initView()
        initRecyclerView()
        run()

        return viewOfLayouthistory
    }

    private fun run() {
        val stdList = sqliteHelper.getAllRuns()
        adapter?.addItems(stdList)

    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager( context)
        adapter = RunAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        recyclerView = viewOfLayouthistory.findViewById(R.id.recyclerViewhistory)
    }

}
