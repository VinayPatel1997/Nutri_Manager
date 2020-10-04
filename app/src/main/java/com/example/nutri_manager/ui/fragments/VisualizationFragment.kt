package com.example.nutri_manager.ui.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.charts.Cartesian
import com.anychart.core.cartesian.series.Line
import com.anychart.data.Mapping
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.example.nutri_manager.R
import com.example.nutri_manager.models.FoodConsumption
import com.example.nutri_manager.other.CustomDataEntry
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.example.nutri_manager.util.Resource
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_visualization.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

/*
duration position
0 -> 7 days
1 -> 30 days
2 -> 6 months
3 -> 12 months

type position
0 -> list
1 -> visualization
*/


class VisualizationFragment : Fragment(R.layout.fragment_visualization) {

    lateinit var viewModel: FoodViewModel

    var durationPosition: Int = -1
    var typePosition: Int = -1
    var seriesData: ArrayList<DataEntry> = ArrayList()
    lateinit var staringDate: Calendar
    lateinit var cartesian: Cartesian
    var unit: String? = null
    var type: String? = "etc"



    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel

//        val seriesData = fakeDate()
//        any_chart_view.setChart(getCartesian(seriesData))

//        cartesian = AnyChart.line()
//        cartesian.animation(true)
//        cartesian.padding(10, 20, 5, 20)
//        cartesian.crosshair().enabled(true)
//
//        val stroke: Stroke? = null
//        val string: String? = null
//        cartesian.crosshair().yLabel(true).yStroke(stroke, null, null, string, string)
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
//        cartesian.title("Food Consumption History")
//        cartesian.yAxis(0).title("Unit in ($unit)")
//        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)
//        val set: Set = Set.instantiate()
//        set.data(seriesData)
//        Toast.makeText(requireContext(),"${seriesData.size}", Toast.LENGTH_SHORT).show()
//        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
//        val series1 = cartesian.line(series1Mapping)
//        Toast.makeText(requireContext(),"reset", Toast.LENGTH_SHORT).show()
//        series1.name("$type")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers().type(MarkerType.CIRCLE).size(4)
//        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
//        cartesian.legend().enabled(true)
//        cartesian.legend().fontSize(13)
//        cartesian.legend().padding(0, 0, 10, 0)
//        any_chart_view.setChart(cartesian)
//
        val date = Calendar.getInstance()

        spDuration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    durationPosition = position
                    var job: Job? = null
                    job?.cancel()
                    job = MainScope().launch {
                        viewModel.getFoodConsumption()
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }


        spType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    typePosition = position
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        viewModel.foodConsumption.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    val querySnapshot = response.data
                    if (querySnapshot != null) {
                        // hide progress ba
                        initializingChartData(response.data)
//                        createChart()
                    } else {
                        Toast.makeText(context, "No record found!", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
                }
                is Resource.Error -> {
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun retrive() = CoroutineScope(Dispatchers.IO).launch {
//        val querySnapshot = collectionRef.get().await()
//        for (document in querySnapshot.documents) {
//            val foodConsumption = document.toObject<FoodConsumption>()
//            val mapValue = foodConsumption!!.date!!.get("date")
//            withContext(Dispatchers.Main) {
//                Toast.makeText(requireContext(), "date ${mapValue.toString()}", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        }
//    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun initializingChartData(responses: QuerySnapshot?) {
        val seriesData: ArrayList<DataEntry> = ArrayList()

        try {
            val startingDate = getStartingDate()
            for (index in 1..getDayRange()) {

                startingDate.add(Calendar.DATE , 1)
//                Log.d("DATE TRACKING", "${startingDate.get(Calendar.DATE)}/${startingDate.get(Calendar.MONTH)}/${startingDate.time}" )
//                Toast.makeText(requireContext(),"starting ${startingDate.time} // added ${startingDate.time}", Toast.LENGTH_SHORT).show()

//                val dateTime: LocalDateTime = startingDate.plusDays(index.toLong())
                val XAxisDate = "${startingDate.get(Calendar.DATE)}/${(startingDate.get(Calendar.MONTH))+1}"
                var YAxisValue = 0.0
                for (response in responses!!.documents) {
//                    Toast.makeText(context, "size = ${responses.size()}", Toast.LENGTH_SHORT).show()
                    val foodConsumptionInstance = response.toObject<FoodConsumption>()
                    val foodConsumptionDate = foodConsumptionInstance!!.date!!.get("date")!!.toDate()
                    Log.d("DATE TRACKING", "ORIGINAL::${startingDate.time.month}/ FIRESTORE::${foodConsumptionDate.month}" )
//                    Toast.makeText(context, "4 inside for inner loop", Toast.LENGTH_SHORT).show()
                    if (foodConsumptionDate.year == startingDate.time.year && foodConsumptionDate.month == startingDate.time.month
                        && foodConsumptionDate.date == startingDate.time.date) {
                        val nutrientList = foodConsumptionInstance.foodNutrientList?.foodNutrients
//                        Toast.makeText(context, "5 ", Toast.LENGTH_SHORT).show()
                        if (nutrientList != null) {
                            for (nutrient in nutrientList) {
                                if (nutrient.nutrientId == 1008) {
                                    YAxisValue = YAxisValue + nutrient.value!!
                                }
                            }
                        }
                    }
                }
                seriesData.add(CustomDataEntry(XAxisDate, YAxisValue))
            }
            clearFindViewByIdCache()
            frameLayout.resetPivot()
            any_chart_view.removeView(any_chart_view)
            any_chart_view.setChart(getCartesian(seriesData))
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    fun getCartesian(seriesData : ArrayList<DataEntry>): Cartesian {
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10, 20, 5, 20)
        cartesian.crosshair().enabled(true)

        val stroke: Stroke? = null
        val string: String? = null
        cartesian.crosshair().yLabel(true).yStroke(stroke, null, null, string, string)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.title("Food Consumption History")
        cartesian.yAxis(0).title("Unit in ($unit)")
        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)
        val set: Set = Set.instantiate()
        set.data(seriesData)
        Toast.makeText(requireContext(),"${seriesData.size}", Toast.LENGTH_SHORT).show()
        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series1 = cartesian.line(series1Mapping)
        Toast.makeText(requireContext(),"reset", Toast.LENGTH_SHORT).show()
        series1.name("$type")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4)
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13)
        cartesian.legend().padding(0, 0, 10, 0)
        return cartesian
    }

    fun update(seriesData : ArrayList<DataEntry>){
        val set: Set = Set.instantiate()
        set.data(seriesData)
//        Toast.makeText(requireContext(),"${seriesData.size}", Toast.LENGTH_SHORT).show()
        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series1 = cartesian.line(series1Mapping)
        val seriesData = cartesian.getSeriesAt(0)
        val sr = cartesian.getSeriesCount()
        Toast.makeText(requireContext(),"count ${sr.toString()}", Toast.LENGTH_SHORT).show()
        series1.name("$type")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers().type(MarkerType.CIRCLE).size(4)
        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13)
        cartesian.legend().padding(0, 0, 10, 0)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStartingDate(): Calendar {
        val date = Calendar.getInstance()
        if (durationPosition == 0) {
            date.add(Calendar.DATE,-7)
            return date
        } else if (durationPosition == 1) {
            date.add(Calendar.DATE,-30)
            return date
        } else if (durationPosition == 2) {
            date.add(Calendar.DATE,-180)
            return date
        } else {
            date.add(Calendar.DATE,-365)
            return date
        }
    }

    private fun getDayRange(): Int {
        if (durationPosition == 0) {
            unit = "g"
            type= "Energy"
            return 7
        } else if (durationPosition == 1) {
            unit = "mg"
            type = "Protein"
            return 30
        } else if (durationPosition == 2) {
            unit = "kg"
            type = "fat"
            return 180
        } else {
            return 365
        }
    }

//    private fun createChart(cartesian: Cartesian) {
//        clearFindViewByIdCache()
//        APIlib.getInstance().setActiveAnyChartView(any_chart_view)
//        this.any_chart_view.setChart(cartesian)
//    }

//
//    private fun lineChartHelper(seriesData : ArrayList<DataEntry>, unit: String?, type: String?) : Cartesian{
//        cartesian.animation(true)
//        cartesian.padding(10, 20, 5, 20)
//        cartesian.crosshair().enabled(true)
//
//        val stroke: Stroke? = null
//        val string: String? = null
//        cartesian.crosshair().yLabel(true).yStroke(stroke, null, null, string, string)
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
//        cartesian.title("Food Consumption History")
//        cartesian.yAxis(0).title("Unit in ($unit)")
//        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)
//
//        val set: Set = Set.instantiate()
//
//        set.data(seriesData)
//
//        Toast.makeText(requireContext(),"${seriesData.size}", Toast.LENGTH_SHORT).show()
//        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
//
//        val series1 = cartesian.line(series1Mapping)
//        Toast.makeText(requireContext(),"reset", Toast.LENGTH_SHORT).show()
//        series1.name("$type")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers().type(MarkerType.CIRCLE).size(4)
//        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
//
//        cartesian.legend().enabled(true)
//        cartesian.legend().fontSize(13)
//        cartesian.legend().padding(0, 0, 10, 0)
//        return cartesian
//    }




    fun fakeDate():ArrayList<DataEntry>{
        val seriesData: ArrayList<DataEntry> = ArrayList()
        seriesData.add(CustomDataEntry("2001",5.5))
        seriesData.add(CustomDataEntry("2002",6.5))
        seriesData.add(CustomDataEntry("2003",3.5))
        seriesData.add(CustomDataEntry("2004",9.5))
        seriesData.add(CustomDataEntry("2005",4.5))
        seriesData.add(CustomDataEntry("2006",7.5))
        seriesData.add(CustomDataEntry("2007",3.5))
        seriesData.add(CustomDataEntry("2008",5.5))
        seriesData.add(CustomDataEntry("2009",7.5))
        seriesData.add(CustomDataEntry("2010",9.5))
        seriesData.add(CustomDataEntry("2011",7.5))
        seriesData.add(CustomDataEntry("2012",6.5))
        seriesData.add(CustomDataEntry("2013",4.5))
        seriesData.add(CustomDataEntry("2014",3.5))
        seriesData.add(CustomDataEntry("2015",9.5))
        seriesData.add(CustomDataEntry("2016",5.5))
        return seriesData
    }
}




//        Toast.makeText(requireContext(), "${date.time}", Toast.LENGTH_LONG).show()
//        Toast.makeText(requireContext(), "Month $date")

//        Log.d("TIME", "${date.time}")
//        Log.d("YEAR", "${date.get(Calendar.YEAR)}")
//        Log.d("MONTH", "${date.get(Calendar.MONTH)}")
//        Log.d("DATE", "${date.get(Calendar.DATE)}")




//        val timestamp = Timestamp.now()
//        Toast.makeText(requireContext(), "stamp ${timestamp.toDate()}", Toast.LENGTH_LONG).show()

//        date.get(Calendar.DATE)

//        date.add(Calendar.DATE, -2)
//        Toast.makeText(requireContext(), "1 Minus Date ${date.time}", Toast.LENGTH_SHORT).show()
//
//        date.add(Calendar.DATE, 2)
//        Toast.makeText(requireContext(), "2 Plus Date ${date.time}", Toast.LENGTH_SHORT).show()
//
//        date.add(Calendar.HOUR, 4)
//        Toast.makeText(requireContext(), "3 Plus 4 hours ${date.time}", Toast.LENGTH_SHORT).show()
//
//
//        val date2 = Calendar.getInstance()
//        Toast.makeText(requireContext(), "4 Plus 4 hours ${date.time}", Toast.LENGTH_SHORT).show()
//
//        if (date.time.date == date2.time.date){
//            Toast.makeText(requireContext(), "5 Plus 4 hours ${date.time} & ${date2.time}", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(requireContext(), "6 Not equal", Toast.LENGTH_SHORT).show()
//        }