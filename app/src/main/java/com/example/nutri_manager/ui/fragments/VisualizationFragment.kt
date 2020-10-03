package com.example.nutri_manager.ui.fragments

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

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

//    lateinit var viewModel: FoodViewModel

//    var durationPosition: Int = -1
//    var typePosition: Int = -1
//    var seriesData: ArrayList<DataEntry> = ArrayList()

    val collectionRef = FirebaseAuth.getInstance().uid!!.let {
        FirebaseFirestore.getInstance().collection(
            it
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as FoodActivity).viewModel
        retriveRealTimeFoodConsuptionData()
        } /// delete this after uncommnet

    private fun retrive() = CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot = collectionRef.get().await()
        for (document in querySnapshot.documents){
            val foodConsumption = document.toObject<FoodConsumption>()
//            withContext(Dis)
        }
    }


    private fun retriveRealTimeFoodConsuptionData() {
        collectionRef?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            querySnapshot?.let {
                for (document in it) {
                    val foodConsumption = document.toObject<FoodConsumption>()
                }
            }
        }
    }

//
//
//
//
//
//
//
//        spDuration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                adapterView: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
////                durationPosition = position
////                var job: Job? = null
////                job?.cancel()
////                job = MainScope().launch {
////                    viewModel.getFoodConsumption()
////                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {}
//        }
//
//
//        spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(
//                adapterView: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                typePosition = position
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {}
//        }

//
//        viewModel.foodConsumption.observe(viewLifecycleOwner, Observer { response ->
//            when (response) {
//                is Resource.Success -> {
//                    val querySnapshot = response.data
//                    if (querySnapshot != null) {
//                        // hide progress ba
//
//
//                        for (document in querySnapshot.documents) {
//                            val foodConsumptionInstance = document.toObject<FoodConsumption>()!!
//                            Toast.makeText(context, " = ${foodConsumptionInstance.foodNutrientList!!.fdcId}", Toast.LENGTH_SHORT).show()
//                        }
////                        initializingChartData(response.data)
////                        createChart()
//                    } else {
//                        Toast.makeText(context, "No record found!", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                is Resource.Loading -> {
//                    Toast.makeText(context, "Loading", Toast.LENGTH_SHORT).show()
//                }
//                is Resource.Error -> {
//                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        })
    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun initializingChartData(responses: QuerySnapshot?) {
//        try {
//            val startingDate = getStartingDate()
//            for (index in 1..getDayRange()) {
//                val dateTime: LocalDateTime = startingDate.plusDays(index.toLong())
//                val XAxisDate = "${dateTime.dayOfMonth}/${dateTime.monthValue}"
//                var YAxisValue = 0.0
//                for (response in responses!!.documents!!) {
//                    Toast.makeText(context, "size = ${responses.size()}", Toast.LENGTH_SHORT).show()
//                    val foodConsumptionInstance = response.toObject<FoodConsumption>()
//                    val foodConsumptionDate = foodConsumptionInstance!!.date?.get("date")
//                    Toast.makeText(context, "4 inside for inner loop", Toast.LENGTH_SHORT).show()
//                    if (foodConsumptionDate?.year == dateTime.year && foodConsumptionDate.dayOfYear == dateTime.dayOfYear) {
//                        val nutrientList = foodConsumptionInstance.foodNutrientList?.foodNutrients
//                        Toast.makeText(context, "5 ", Toast.LENGTH_SHORT).show()
//                        if (nutrientList != null) {
//                            for (nutrient in nutrientList) {
//                                if (nutrient.nutrientId == 1008) {
//                                    YAxisValue = YAxisValue + nutrient.value!!
//                                }
//                            }
//                        }
//                    }
//                }
//                seriesData.add(CustomDataEntry(XAxisDate, YAxisValue))
//            }
//        } catch (e: Exception) {
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//        }
//    }


//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun getStartingDate(): LocalDateTime {
//        val localDateTime: LocalDateTime = LocalDateTime.now()
//        if (durationPosition == 0) {
//            localDateTime.minusDays(7)
//            return localDateTime
//        } else if (durationPosition == 1) {
//            localDateTime.minusDays(30)
//            return localDateTime
//        } else if (durationPosition == 2) {
//            localDateTime.minusDays(180)
//            return localDateTime
//        } else {
//            localDateTime.minusDays(365)
//            return localDateTime
//        }
//    }
//
//    private fun getDayRange(): Int {
//        if (durationPosition == 0) {
//            return 7
//        } else if (durationPosition == 1) {
//            return 30
//        } else if (durationPosition == 2) {
//            return 180
//        } else {
//            return 365
//        }
//    }
//
//    private fun createChart() {
//        var cartesian: Cartesian = AnyChart.line()
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
//        cartesian.yAxis(0).title("Unit in (g)")
//        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)
//
//        val set: Set = Set.instantiate()
//        set.data(seriesData)
//        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
//
//        val series1: Line = cartesian.line(series1Mapping)
//        series1.name("Energy")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers().type(MarkerType.CIRCLE).size(4)
//        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
//
//        cartesian.legend().enabled(true)
//        cartesian.legend().fontSize(13)
//        cartesian.legend().padding(0,0,10,0)
//        any_chart_view.setChart(cartesian)
//    }
//

//}














// Before that















//
//        val cartesian: Cartesian = AnyChart.line()
//        cartesian.animation(true)
//        cartesian.padding(10, 20, 5, 20)
//        cartesian.crosshair().enabled(true)
//
//        val stroke: Stroke? = null
//        val string: String? = null
//        cartesian.crosshair().yLabel(true).yStroke(stroke, null, null, string, string)
//        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
//        cartesian.title("Food Consumption History")
//        cartesian.yAxis(0).title("Unit in (g)")
//        cartesian.xAxis(0).labels().padding(5, 5, 5, 5)
////        retrieveFoodConsumption()
////
////        seriesData.add(CustomDataEntry("2001",5.5))
////        seriesData.add(CustomDataEntry("2002",6.5))
////        seriesData.add(CustomDataEntry("2003",3.5))
////        seriesData.add(CustomDataEntry("2004",9.5))
////        seriesData.add(CustomDataEntry("2005",4.5))
////        seriesData.add(CustomDataEntry("2006",7.5))
////        seriesData.add(CustomDataEntry("2007",3.5))
////        seriesData.add(CustomDataEntry("2008",5.5))
////        seriesData.add(CustomDataEntry("2009",7.5))
////        seriesData.add(CustomDataEntry("2010",9.5))
////        seriesData.add(CustomDataEntry("2011",7.5))
////        seriesData.add(CustomDataEntry("2012",6.5))
////        seriesData.add(CustomDataEntry("2013",4.5))
////        seriesData.add(CustomDataEntry("2014",3.5))
////        seriesData.add(CustomDataEntry("2015",9.5))
////        seriesData.add(CustomDataEntry("2016",5.5))
//
//
//// Insert Data
//
//        val set: Set = Set.instantiate()
//        set.data(seriesData)
//        val series1Mapping: Mapping = set.mapAs("{ x: 'x', value: 'value' }")
////        var series2Mapping : Mapping = set.mapAs("{ x: 'x', value: 'value' }")
//
//        val series1: Line = cartesian.line(series1Mapping)
//        series1.name("Energy")
//        series1.hovered().markers().enabled(true)
//        series1.hovered().markers().type(MarkerType.CIRCLE).size(4)
//        series1.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
//
//        any_chart_view.setChart(cartesian)
//
////
////        var series2: Line = cartesian.line(series2Mapping)
////        series2.name("Protin")
////        series2.hovered().markers().enabled(true)
////        series2.hovered().markers().type(MarkerType.CIRCLE).size(4)
////        series2.tooltip().position("right").anchor(Anchor.LEFT_CENTER).offsetX(5).offsetY(5)
//
//
//    }
//
//    fun toast(int: Int) {
//        Toast.makeText(
//            requireContext(),
//            "Selected $int and size  ${seriesData.size}",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun retrieveFoodConsumption() {
//
////        withContext(Dispatchers.Main) {
//        Toast.makeText(context, "0 inside function", Toast.LENGTH_SHORT)
////        }
//        val foodConsumptionInstanceList: ArrayList<FoodConsumption> = ArrayList()
//        seriesData.clear()
//
//        val job: Job = CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val querySnapshot = collectionRef.get().await()
//                for (document in querySnapshot.documents) {
//                    val foodConsumptionInstance = document.toObject<FoodConsumption>()!!
//                    foodConsumptionInstanceList.add(foodConsumptionInstance)
////                    withContext(Dispatchers.Main) {
////                        Toast.makeText(context, "2 after fatch", Toast.LENGTH_SHORT).show()
////                    }
//                }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        runBlocking {
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "fatching....", Toast.LENGTH_SHORT)
//            }
//            job.join()
//            withContext(Dispatchers.Main) {
//                Toast.makeText(context, "Complete", Toast.LENGTH_SHORT)
//            }
//        }
//        try {
//            for (index in 1..getDayRange()) {
//                val dateTime: LocalDateTime = getStartingDate().plusDays(index.toLong())
//                val XAxisDate = "${dateTime.dayOfMonth}/${dateTime.monthValue}"
//                var YAxisValue = 0.0
//                Toast.makeText(context, "3 inside for loop for day range", Toast.LENGTH_SHORT)
//                    .show()
//
//                for (foodConsumptionInstance in foodConsumptionInstanceList) {
//                    val foodConsumptionDate = foodConsumptionInstance.date?.get("date")
//                    Toast.makeText(context, "4 inside for inner loop", Toast.LENGTH_SHORT).show()
//                    if (foodConsumptionDate?.year == dateTime.year && foodConsumptionDate.dayOfYear == dateTime.dayOfYear) {
//                        val nutrientList = foodConsumptionInstance.foodNutrientList?.foodNutrients
//                        Toast.makeText(context, "5 ", Toast.LENGTH_SHORT).show()
//                        if (nutrientList != null) {
//                            for (nutrient in nutrientList) {
//                                Toast.makeText(
//                                    context,
//                                    "6 inside for loop for day range",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//
//                                if (nutrient.nutrientId == 1008) {
//                                    Toast.makeText(
//                                        context,
//                                        "7 inside for loop for day range",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//
//                                    YAxisValue = YAxisValue + nutrient.value!!
//                                }
//                            }
//                        }
//                    }
//                }
//                seriesData.add(CustomDataEntry(XAxisDate, YAxisValue))
//            }
//        } catch (e: Exception) {
//            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//        }
//    }
//


//            else if (localDateTimeCurrent.year == (startingYear + 1)) {
//                try {
//                    val querySnapshot = collectionRef
//                        .whereGreaterThanOrEqualTo("date.date.chronology.dayOfYear", startingDayOfYear)
//                        .orderBy("date.date.chronology.dayOfYear", Query.Direction.ASCENDING)
//                        .get()
//                        .await()
//                    val querySnapshot2 =
//                        collectionRef.whereEqualTo("date.date.chronology.year", (startingYear + 1))
//                            .orderBy("date.date.chronology.dayOfYear", Query.Direction.ASCENDING)
//                            .get()
//                            .await()
//
////                    val combineQuery = QuerySnapshot
//                } catch (e: Exception) {
//
//                }
//            }


//
//@RequiresApi(Build.VERSION_CODES.O)
//private fun retriveFoodConsumptionData() = CoroutineScope(Dispatchers.IO).launch {
////        var foodConsumption: FoodConsumption = FoodConsumption()
//    seriesData.clear()
//    val localDateTimeStarting = getStartingDate()
//    val localDateTimeCurrent: LocalDateTime = LocalDateTime.now()
//    if (localDateTimeStarting != null) {
//        val startingYear: Int = localDateTimeStarting.year
//        val startingDayOfYear: Int = localDateTimeStarting.dayOfYear
//        withContext(Dispatchers.Main) {
//            Toast.makeText(context, "current ${localDateTimeCurrent.year} starting $startingYear", Toast.LENGTH_SHORT).show()
//        }
//        if (localDateTimeCurrent.year == startingYear) {
//            try {
//                val querySnapshot = collectionRef.get().await()
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, "Size ${querySnapshot.isEmpty}", Toast.LENGTH_SHORT).show()
//                }
//                for (document in querySnapshot.documents) {
//                    val foodConsumption = document.toObject<FoodConsumption>()!!
//                    val dateTime: LocalDateTime? = foodConsumption.date!!.get("date")
//                    val food = foodConsumption.foodNutrientList
//                    val stringDateTime = "${dateTime!!.dayOfMonth}/${dateTime!!.monthValue}"
//                    if (seriesData.last().getValue("x") != stringDateTime){
//                        for (nutrient in food!!.foodNutrients!!){
//                            if (nutrient.nutrientId == 2000){
//                                seriesData.add(CustomDataEntry(stringDateTime, nutrient.value!!))
//                            }
//                        }
//                    } else {
//                        val sameDateValue: Double = seriesData.last().getValue("value") as Double
//                        seriesData.removeLast()
//                        for (nutrient in food!!.foodNutrients!!){
//                            if (nutrient.nutrientId == 2000){
//                                seriesData.add(CustomDataEntry(stringDateTime, ((nutrient.value!!)+(sameDateValue))))
//                            }
//                        }
//                    }
//                }
////                        withContext(Dispatchers.Main) {
////                            Toast.makeText(context, "Fatch Successfully ${foodConsumption.foodNutrientList!!.fdcId}", Toast.LENGTH_SHORT).show()
////                        }
//            } catch (e: Exception) {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    } else {
//        withContext(Dispatchers.Main) {
//            Toast.makeText(context, "Error while fetching current date and time from system", Toast.LENGTH_SHORT).show()
//        }
//    }
//}