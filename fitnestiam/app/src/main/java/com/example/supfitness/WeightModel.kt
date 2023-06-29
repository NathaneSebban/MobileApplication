package com.example.supfitness

import java.util.*

data class WeightModel(

    var id: Int = getAutoId(),
    var kg: Int,
    var kg2: Int,
    var date: String = ""
){
    companion object{
        fun getAutoId():Int{
            val random = Random()
            return random.nextInt(100)
        }
    }
}