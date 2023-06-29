package com.example.supfitness

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeightAdapter : RecyclerView.Adapter<WeightAdapter.StudentViewHolder>() {
    private var stdList: ArrayList<WeightModel> = ArrayList()
    private var onClickItem: ((WeightModel) -> Unit)? = null
    private var onClickDeleteItem: ((WeightModel) -> Unit)? = null

    fun addItems(items: java.util.ArrayList<WeightModel>){
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnclickItem(callback: (WeightModel) -> Unit) {
        this.onClickItem = callback
    }

    fun setOnclickDeleteItem(callback: (WeightModel) -> Unit) {
        this.onClickDeleteItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std, parent, false)
    )

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std)}
        holder.btnDelete.setOnClickListener { onClickDeleteItem?.invoke(std)}
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class StudentViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var kg = view.findViewById<TextView>(R.id.tvKg)
        private var date = view.findViewById<TextView>(R.id.tvDate)
        var btnDelete = view.findViewById<TextView>(R.id.btnDelete)

        fun bindView(std: WeightModel) {
            kg.text = std.kg.toString() + "." + std.kg2.toString() + " Kg"
            date.text = std.date
        }
    }

}