package com.example.supfitness

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.NumberPicker
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class WeightTracker : Fragment() {
    private lateinit var edKg1: NumberPicker
    private lateinit var edKg2: NumberPicker
    private lateinit var btnAdd: Button

    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var recyclerView: RecyclerView
    private var adapter: WeightAdapter? = null
    private var std: WeightModel? = null

    private lateinit var viewOfLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        viewOfLayout = inflater!!.inflate(R.layout.weight_tracker, container, false)

        initView()
        initRecyclerView()
        sqliteHelper = context?.let { SQLiteHelper(it) }!!

        btnAdd.setOnClickListener { addWeight() }


        adapter?.setOnclickDeleteItem {
            deleteWeight(it.id)

        }
        getWeight()
        return viewOfLayout
    }


    private fun getWeight() {
        edKg1.minValue = 0
        edKg1.maxValue = 500
        edKg1.wrapSelectorWheel = true
        edKg2.minValue = 0
        edKg2.maxValue = 1000
        edKg2.wrapSelectorWheel = true
        val stdList = sqliteHelper.getAllStudent()
        Log.e("pppp", "${stdList.size}")

        adapter?.addItems(stdList)


    }

    private fun addWeight() {
        val kg: Int = edKg1.value
        val kg2: Int = edKg2.value
        val tempdate = SimpleDateFormat("dd/M/yyyy")
        val date: String = tempdate.format(Date())

        if (!sqliteHelper.getToday()) {
            if (kg == 0) {
                Toast.makeText(context, "Merci de rentrer votre poid !", Toast.LENGTH_SHORT).show()
            } else {
                val std = WeightModel(kg = kg, kg2 = kg2, date = date)
                val status = sqliteHelper.insertStudent(std)
                if (status > -1) {
                    Toast.makeText(context, "Poid Ajouté !", Toast.LENGTH_SHORT).show()
                    clearEditText()
                    getWeight()
                } else {
                    Toast.makeText(context, "Oups, une erreur est survenue !", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Vous avez déjà indiqué votre poids aujourd'hui !", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteWeight(id:Int) {
        if (id == null) return

        val builder = activity?.let { AlertDialog.Builder(it) }
        builder!!.setMessage("Voulez vous supprimer ce poids ?")
        builder!!.setCancelable(true)
        builder!!.setPositiveButton("Oui") { dialog, _ ->
            sqliteHelper.deleteStudentById(id)
            getWeight()
            dialog.dismiss()
        }
        builder!!.setNegativeButton("Non") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder!!.create()
        alert.show()

    }

    private fun clearEditText() {
        edKg1.value = 0
        edKg2.value = 0
        edKg1.requestFocus()
        edKg2.requestFocus()
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager( context)
        adapter = WeightAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        edKg1 = viewOfLayout.findViewById(R.id.edKg1)
        edKg2 = viewOfLayout.findViewById(R.id.edKg2)
        btnAdd = viewOfLayout.findViewById(R.id.btnAdd)
        recyclerView = viewOfLayout.findViewById(R.id.recyclerView)
    }
}