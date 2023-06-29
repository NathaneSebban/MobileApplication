package com.example.supfitness

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainFitness : AppCompatActivity() {


    var tabTitle = arrayOf("Poids","Courbes","Courses","Historique", "Carte")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_fitness)



        var pager = findViewById<ViewPager2>(R.id.viewPager)

        var tabl = findViewById<TabLayout>(R.id.tabLayout)

        pager.adapter = TabPageAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabl, pager) { tab, position ->
            tab.text = tabTitle[position]

        }.attach()

        /*val fab: View = findViewById(R.id.btnWeightDialog)
        fab.setOnClickListener { view ->
            val newFragment = AddWeightDialog()
            newFragment.show(supportFragmentManager, "missiles")
        }*/

        /* findViewById<FloatingActionButton>(R.id.btnWeightDialog).setOnClickListener {
              val newFragment = AddWeightDialog()
              newFragment.show(supportFragmentManager, "missiles")
          }*/


      /* var helper = MyDBHelper(applicationContext)
        var db = helper.readableDatabase;
        var rs = db.rawQuery("SELECT * FROM WEIGHTPERDAY",null)


        if(rs.moveToNext())
            Toast.makeText(applicationContext, rs.getString(1), Toast.LENGTH_LONG).show()*/


    }


}