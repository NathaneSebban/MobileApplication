package com.example.supfitness

import java.util.*

data class RunModel(

    var id: Int = getAutoId(),
    var startdate: String = "",
    var endate: String = "",
    var totaltime: String = "",
    var totalposition: String = ""
){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}