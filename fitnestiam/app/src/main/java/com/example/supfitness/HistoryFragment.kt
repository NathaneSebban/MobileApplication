package com.example.supfitness

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.androidplot.xy.*
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.util.*
import kotlin.collections.ArrayList


class HistoryFragment : Fragment() {

    private lateinit var viewOfLayout: View

    private lateinit var sqliteHelper: SQLiteHelper

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewOfLayout = inflater!!.inflate(R.layout.fragment_history, container, false)

        val plot = viewOfLayout.findViewById<XYPlot>(R.id.plot)

        var helper = SQLiteHelper(inflater.context)
        var db = helper.readableDatabase;
        var cursor = db.rawQuery("SELECT ${SQLiteHelper.KG}, ${SQLiteHelper.DATE} FROM ${SQLiteHelper.TBL_WEIGHT}",null)

        val plotLabelsT =  ArrayList<String>()
        val series1NumberT =  ArrayList<Number>()


        if (cursor.moveToFirst()) {
            do {

                val date  = cursor.getString(0);
                val weight  = cursor.getString(1);



                plotLabelsT.add(weight.toString())
                series1NumberT.add(date.toInt())
            } while (cursor.moveToNext());
        }

        if(!plotLabelsT.isEmpty()){


        val plotLabels =  plotLabelsT.toTypedArray()
        val series1Number = series1NumberT.toTypedArray()

        val series1 : XYSeries = SimpleXYSeries(
            Arrays.asList(* series1Number),
            SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series 1")
        val series1Frt = LineAndPointFormatter(Color.BLUE, Color.BLACK, null, null)

        series1Frt.setInterpolationParams(CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal))
        plot.addSeries(series1, series1Frt)

        plot.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object: Format(){
            override fun format(
                obj: Any?,
                toAppendTo: StringBuffer,
                pos: FieldPosition
            ): StringBuffer? {
                Log.d("obj",obj.toString())
                val i = Math.round((obj as Number).toFloat())
                return toAppendTo.append(plotLabels[i])

            }

            override fun parseObject(source: String?, pos: ParsePosition?): Any? {
                return null
            }

        }

        PanZoom.attach(plot)
        }
        return viewOfLayout

    }

}