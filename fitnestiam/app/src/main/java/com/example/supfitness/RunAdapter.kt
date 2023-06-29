package com.example.supfitness

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RunAdapter : RecyclerView.Adapter<RunAdapter.StudentViewHolder>() {
    private var stdList: ArrayList<RunModel> = ArrayList()
    private var onClickItem: ((RunModel) -> Unit)? = null

    fun addItems(items:ArrayList<RunModel>){
        this.stdList = items
        notifyDataSetChanged()
    }

    fun setOnclickItem(callback: (RunModel) -> Unit) {
        this.onClickItem = callback
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StudentViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.card_items_std_history, parent, false)
    )

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val std = stdList[position]
        holder.bindView(std)
        holder.itemView.setOnClickListener { onClickItem?.invoke(std)}
    }

    override fun getItemCount(): Int {
        return stdList.size
    }

    class StudentViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        private var id = view.findViewById<TextView>(R.id.tvIdhistory)
        private var startdate = view.findViewById<TextView>(R.id.tvStartTimeHistory)
        private var enddate = view.findViewById<TextView>(R.id.tvEndTimeHistory)
        private var totaltime = view.findViewById<TextView>(R.id.tvtotalTimeHistory)
        private var totalposition = view.findViewById<TextView>(R.id.tvtotalPositionHistory)

        fun bindView(std: RunModel) {
            startdate.text = "Début : " + std.startdate.toString()
            enddate.text = "Fin : " + std.endate.toString()
            totaltime.text = "Temps Total : " + std.totaltime.toString() + " (H:M:S)"
            totalposition.text = "Nombre de position : " + std.totalposition
        }
    }

}