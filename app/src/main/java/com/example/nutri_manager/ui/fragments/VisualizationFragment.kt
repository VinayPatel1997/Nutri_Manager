package com.example.nutri_manager.ui.fragments

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.nutri_manager.R
import com.example.nutri_manager.models.FoodConsumption
import com.example.nutri_manager.util.SpinnerHelper
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.ViewModel
import com.example.nutri_manager.util.Resource
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_visualization.*
import kotlinx.coroutines.*
import lecho.lib.hellocharts.model.*
import java.util.*
import kotlin.collections.ArrayList

class VisualizationFragment : Fragment(R.layout.fragment_visualization) {

    lateinit var viewModel: ViewModel

    var durationPosition: Int = -1
    var typePosition: Int = -1
    var querySnapshot: QuerySnapshot? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel

        CoroutineScope(Dispatchers.IO).launch {
            showProgressBar()
            viewModel.getFoodConsumption().join()
            hideProgressBar()
        }

        val xAxisValue: ArrayList<AxisValue> = ArrayList()
        val yAxisValue: ArrayList<PointValue> = ArrayList()
        createChart(xAxisValue, yAxisValue, "Nutrient (unit)", 100.0)

        spDuration.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    durationPosition = position
                    if (querySnapshot != null && typePosition != -1) {
                        val nutrientId = SpinnerHelper.getNutrintId(typePosition)
                        val unit = SpinnerHelper.getNutrientUnit(nutrientId)
                        val lebel = "${SpinnerHelper.getNutrientName(nutrientId)}($unit)"
//                        val lebel = "${adapterView?.getItemAtPosition(typePosition)}($unit)"
                        initializingChartData(querySnapshot, nutrientId, lebel)
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
                    if (querySnapshot != null && durationPosition != -1){
                        typePosition = position
                        val nutrientId = SpinnerHelper.getNutrintId(typePosition)
                        val unit = SpinnerHelper.getNutrientUnit(nutrientId)
                        val lebel = "${SpinnerHelper.getNutrientName(nutrientId)}($unit)"
                        initializingChartData(querySnapshot, nutrientId, lebel )
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        viewModel.foodConsumptionMutableLiveData.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    val querySnapshot = response.data
                    if (querySnapshot != null) {
                        this.querySnapshot = querySnapshot
                        hideProgressBar()
                    } else {
                        hideProgressBar()
                        Toast.makeText(context, "No record found!", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(context, "Error ${response.message}", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun createChart(
        xAxisValueArgs: ArrayList<AxisValue>,
        yAxisValueArgs: ArrayList<PointValue>,
        title: String,
        maxValue: Double
    ) {
        val xAxisValue: ArrayList<AxisValue> = xAxisValueArgs
        val yAxisValue: ArrayList<PointValue> = yAxisValueArgs
        val line: Line = Line(yAxisValue).setColor(Color.parseColor("#9C27B0"))
        val lines: ArrayList<Line> = ArrayList()
        lines.add(line)
        val data = LineChartData()
        data.setLines(lines)

        val axis = Axis()
        axis.setValues(xAxisValue)
        axis.setTextSize(16)
        axis.setTextColor(Color.parseColor("#03A9F4"))
        data.axisXBottom = axis
        axis.setName("Time")


        val yAxis = Axis()
        yAxis.setTextSize(16)
        yAxis.setTextColor(Color.parseColor("#03A9F4"))
        yAxis.setName(title)
        data.axisYLeft = yAxis
        lineChart.lineChartData = data
        val viewport = Viewport(lineChart.maximumViewport)
        viewport.bottom = 0F
        viewport.left = 0F
        viewport.top = ((maxValue*130)/100).toFloat()
        lineChart.maximumViewport = viewport
        lineChart.currentViewport = viewport
    }


    @RequiresApi(Build.VERSION_CODES.P)
    private fun initializingChartData(responses: QuerySnapshot?, nutrientId: Int, lebel: String) {

        try {
            val startingDate = getStartingDate()
            var XAxisDate: String
            var YAxisValue: Double
            val xAxisValue: ArrayList<AxisValue> = ArrayList()
            val yAxisValue: ArrayList<PointValue> = ArrayList()
            var maxValue = 0.0

            for (index in 0..getDayRange()) {
                startingDate.add(Calendar.DATE, 1)
                XAxisDate =
                    "${startingDate.get(Calendar.DATE)}/${(startingDate.get(Calendar.MONTH)) + 1}"
                YAxisValue = 0.0
                for (response in responses!!.documents) {
                    val foodConsumptionInstance = response.toObject<FoodConsumption>()
                    val foodConsumptionDate =
                        foodConsumptionInstance!!.date!!.get("date")!!.toDate()

                    if (foodConsumptionDate.year == startingDate.time.year && foodConsumptionDate.month == startingDate.time.month
                        && foodConsumptionDate.date == startingDate.time.date
                    ) {
                        val nutrientList = foodConsumptionInstance.foodNutrientList?.foodNutrients
                        if (nutrientList != null) {
                            for (nutrient in nutrientList) {
                                if (nutrient.nutrientId == nutrientId) {
                                    YAxisValue = YAxisValue + nutrient.value!!
                                }
                            }
                        }
                    }
                }
                if (YAxisValue > maxValue) {
                    maxValue = YAxisValue
                }
                xAxisValue.add(index, AxisValue(index.toFloat()).setLabel(XAxisDate))
                yAxisValue.add(PointValue(index.toFloat(), YAxisValue.toFloat()))
            }
            createChart(xAxisValue, yAxisValue, lebel, maxValue)

        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getStartingDate(): Calendar {
        val date = Calendar.getInstance()
        if (durationPosition == 0) {
            date.add(Calendar.DATE, -7)
            return date
        } else if (durationPosition == 1) {
            date.add(Calendar.DATE, -30)
            return date
        } else if (durationPosition == 2) {
            date.add(Calendar.DATE, -180)
            return date
        } else {
            date.add(Calendar.DATE, -365)
            return date
        }
    }

    private fun getDayRange(): Int {
        if (durationPosition == 0) {
            return 7
        } else if (durationPosition == 1) {
            return 30
        } else if (durationPosition == 2) {
            return 180
        } else {
            return 365
        }
    }


    private fun hideProgressBar() {
        progress_bar_visualization.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progress_bar_visualization.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false

}
