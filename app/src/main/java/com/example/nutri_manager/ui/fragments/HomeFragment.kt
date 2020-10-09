package com.example.nutri_manager.ui.fragments

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.nutri_manager.R
import com.example.nutri_manager.models.FoodConsumption
import com.example.nutri_manager.models.models_progressbar.AgeWeight
import com.example.nutri_manager.models.models_progressbar.Avoid
import com.example.nutri_manager.models.models_progressbar.Take
import com.example.nutri_manager.other.SpinnerHelper
import com.example.nutri_manager.ui.FoodActivity
import com.example.nutri_manager.ui.FoodViewModel
import com.example.nutri_manager.util.Resource
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import kotlinx.android.synthetic.main.dialog_age_weight.*
import kotlinx.android.synthetic.main.dialog_avoid_take_nutrient.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    lateinit var viewModel: FoodViewModel
    var querySnapshot: QuerySnapshot? = null
    var currentDate = Calendar.getInstance()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as FoodActivity).viewModel

        viewModel.getFoodConsumption()
        viewModel.foodConsumption.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    val querySnapshot = response.data
                    if (querySnapshot != null) {
                        this.querySnapshot = querySnapshot
                        setEnergyProgressBar()
                        setAvoidNutrientProgressBar()
                        setTakeNutrientProgressBar()
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


        fab_add.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_searchFoodFragment2)
        }


        circular_progress_energy.setOnClickListener {
            dialogAgeWeight()
        }

        circular_progress_avoid.setOnClickListener {
            dialogAvoidNutrient()
        }

        circular_progress_take.setOnClickListener {
            dialogTakeNutrient()
        }

    }


    private fun dialogAgeWeight() {
        val dialogLayout =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_age_weight, null)
        val dialogBox = AlertDialog.Builder(requireContext()).setView(dialogLayout).show()

        viewModel.getAgeWeight()
        viewModel.getAgeWeightMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val ageWeight = response.data?.toObject<AgeWeight>()
                    if (ageWeight != null) {
                        dialogBox.tvAge.text = "Age: ${ageWeight.age.toString()}"
                        dialogBox.tvWeight.text = "Weight: ${ageWeight.weight.toString()}"
                        dialogBox.tvGender.text = "Gender: ${ageWeight.gender.toString()}"
                    } else {
                        dialogBox.tvAge.text = "Age: "
                        dialogBox.tvWeight.text = "Weight: "
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
        dialogBox.btn_dialog_age_weight_save.setOnClickListener {
            val age: String? = dialogBox.etAge.text.toString().trim()
            val weight: String? = dialogBox.etWeight.text.toString().trim()
            val checkedGenderRadioButtonId = dialogBox.rgGender.checkedRadioButtonId
            val gender = dialogBox.findViewById<RadioButton>(checkedGenderRadioButtonId)

            if (age != null && weight != null && gender != null) {
                viewModel.uploadAgeWeight(
                    AgeWeight(
                        age.toInt(),
                        weight.toInt(),
                        gender.text.toString()
                    )
                )
                viewModel.uploadAgeWeightMutableLiveData.observe(viewLifecycleOwner, { response ->
                    when (response) {
                        is Resource.SuccessFirebaseUpload -> {
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                                .show()
                            setEnergyProgressBar()
                            dialogBox.cancel()
                            hideProgressBar()
                        }
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                                .show()
                            dialogBox.cancel()
                            hideProgressBar()
                        }
                        is Resource.Loading -> {
                            Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                            showProgressBar()
                        }
                    }
                })
            } else {
                Toast.makeText(requireContext(), "Empty field!!", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBox.btn_dialog_age_weight_cancel.setOnClickListener {
            dialogBox.cancel()
        }
    }

    private fun dialogAvoidNutrient() {
        var avoidNutrientId: Int? = null
        var avoidNutrientValue: String? = null
        val dialogLayout =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_avoid_take_nutrient, null)
        val dialogBox = AlertDialog.Builder(requireContext()).setView(dialogLayout).show()
        dialogBox.message.text = "Enter maximum limit of nutrient"
        dialogBox.spNutrientAvoidTake.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    avoidNutrientId = SpinnerHelper.getNutrintId(position)
                    dialogBox.etAvoidTakeNutrientValue.hint =
                        SpinnerHelper.getNutrientUnit(avoidNutrientId!!)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        dialogBox.btn_dialog_avoid_take_save.setOnClickListener {
            avoidNutrientValue = dialogBox.etAvoidTakeNutrientValue.text.toString().trim()
            if (avoidNutrientValue != null && avoidNutrientId != null) {
                viewModel.uploadAvoidNutrient(
                    Avoid(avoidNutrientId, avoidNutrientValue!!.toFloat())
                )
                viewModel.uploadAvoidNutrientMutableLiveData.observe(
                    viewLifecycleOwner,
                    { response ->
                        when (response) {
                            is Resource.SuccessFirebaseUpload -> {
                                Toast.makeText(
                                    requireContext(),
                                    response.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                setAvoidNutrientProgressBar()
                                dialogBox.cancel()
                                hideProgressBar()
                            }
                            is Resource.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    response.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                dialogBox.cancel()
                                hideProgressBar()
                            }
                            is Resource.Loading -> {
                                showProgressBar()
                            }
                        }
                    })
            } else {
                Toast.makeText(requireContext(), "Empty field!!", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBox.btn_dialog_avoid_take_cancel.setOnClickListener {
            dialogBox.cancel()
        }
    }

    private fun dialogTakeNutrient() {
        var takeNutrientId: Int? = null
        var takeNutrientValue: String? = null
        val dialogLayout =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_avoid_take_nutrient, null)
        val dialogBox = AlertDialog.Builder(requireContext()).setView(dialogLayout).show()
        dialogBox.message.text = "Enter maximum limit of nutrient"
        dialogBox.spNutrientAvoidTake.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    takeNutrientId = SpinnerHelper.getNutrintId(position)
                    dialogBox.etAvoidTakeNutrientValue.hint =
                        SpinnerHelper.getNutrientUnit(takeNutrientId!!)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        dialogBox.btn_dialog_avoid_take_save.setOnClickListener {
            takeNutrientValue = dialogBox.etAvoidTakeNutrientValue.text.toString().trim()
            if (takeNutrientValue != null && takeNutrientId != null) {
                viewModel.uploadTakeNutrient(
                    Take(takeNutrientId, takeNutrientValue!!.toFloat())
                )
                viewModel.uploadTakeNutrientMutableLiveData.observe(
                    viewLifecycleOwner,
                    { response ->
                        when (response) {
                            is Resource.SuccessFirebaseUpload -> {
                                Toast.makeText(
                                    requireContext(),
                                    response.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                setTakeNutrientProgressBar()
                                dialogBox.cancel()
                                hideProgressBar()
                            }
                            is Resource.Error -> {
                                Toast.makeText(
                                    requireContext(),
                                    response.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                dialogBox.cancel()
                                hideProgressBar()
                            }
                            is Resource.Loading -> {
                                showProgressBar()
                            }
                        }
                    })
            } else {
                Toast.makeText(requireContext(), "Empty field!!", Toast.LENGTH_SHORT).show()
            }
        }
        dialogBox.btn_dialog_avoid_take_cancel.setOnClickListener {
            dialogBox.cancel()
        }
    }


    private fun setAvoidNutrientProgressBar() {
        viewModel.getAvoidNutrient()
        viewModel.getAvoidNutrientMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val avoidNutrient = response.data?.toObject<Avoid>()
                    var currentValue = 0F
                    val maxValue: Float
                    if (avoidNutrient != null) {
                        maxValue = avoidNutrient.value!!
                        if (avoidNutrient.id != null && querySnapshot != null) {
                            currentValue =
                                sumNutritions(querySnapshot!!, currentDate, avoidNutrient.id)
                            val nutrientName = SpinnerHelper.getNutrientName(avoidNutrient.id)
                            tv_maxNutriMessage.setText("Max $nutrientName/Day")
                            tv_maxNutriMessage.setTextColor(Color.RED)
                        }
                        setProgressBar(
                            currentValue, maxValue,
                            circular_progress_avoid,
                            tv_avoidCurrent,
                            tv_avoidMax,
                            Color.RED
                        )
                    } else {
                        setProgressBar(
                            0F, 0F,
                            circular_progress_avoid,
                            tv_avoidCurrent,
                            tv_avoidMax,
                            Color.RED
                        )
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setTakeNutrientProgressBar() {
        viewModel.getTakeNutrient()
        viewModel.getTakeNutrientMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val takeNutrient = response.data?.toObject<Take>()
                    var currentValue = 0F
                    val maxValue: Float
                    if (takeNutrient != null) {
                        maxValue = takeNutrient.value!!
                        if (takeNutrient.id != null && querySnapshot != null) {
                            currentValue =
                                sumNutritions(querySnapshot!!, currentDate, takeNutrient.id)
                            val nutrientName = SpinnerHelper.getNutrientName(takeNutrient.id)
                            tv_minNutriMessage.setText("Max $nutrientName/Day")
                            tv_minNutriMessage.setTextColor(Color.GREEN)
                        }
                        setProgressBar(
                            currentValue, maxValue,
                            circular_progress_take,
                            tv_takeCurrent,
                            tv_takeMax,
                            Color.GREEN
                        )
                    } else {
                        setProgressBar(
                            0F, 0F,
                            circular_progress_take,
                            tv_takeCurrent,
                            tv_takeMax,
                            Color.GREEN
                        )
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun setEnergyProgressBar() {
        viewModel.getAgeWeight()
        viewModel.getAgeWeightMutableLiveData.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Success -> {
                    val ageWeight = response.data?.toObject<AgeWeight>()
                    var currentValue = 0F
                    if (ageWeight != null) {
                        if (querySnapshot != null) {
                            currentValue = sumNutritions(querySnapshot!!, currentDate, 1008)
                            tv_aveEnergyMessage.setTextColor(Color.MAGENTA)
                        }
                        val averageEnergy: Float = calculateAverageEnergy(ageWeight)
                        averageEnergy.let {
                            setProgressBar(
                                currentValue, it,
                                circular_progress_energy,
                                tv_energyCurrent,
                                tv_energyMax,
                                Color.MAGENTA
                            )
                        }
                    } else {
                        setProgressBar(
                            0F, 0F,
                            circular_progress_avoid,
                            tv_takeCurrent,
                            tv_takeMax,
                            Color.MAGENTA
                        )
                    }
                    hideProgressBar()
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    private fun calculateAverageEnergy(ageWeight: AgeWeight): Float {
        val age = ageWeight.age
        val weight = ageWeight.weight
        if (ageWeight.gender == "Male") {
            when (age) {
                in 0..3 -> return ((60.9 * weight!!) - 54).toFloat()
                in 4..10 -> return ((22.7 * weight!!) + 495).toFloat()
                in 11..18 -> return ((17.5 * weight!!) + 651).toFloat()
                in 19..30 -> return ((15.3 * weight!!) + 679).toFloat()
                in 31..60 -> return ((11.6 * weight!!) + 879).toFloat()
                in 60..Int.MAX_VALUE -> return ((13.5 * weight!!) + 487).toFloat()
            }
        } else {
            when (age) {
                in 0..3 -> return ((61.0 * weight!!) - 51).toFloat()
                in 4..10 -> return ((22.5 * weight!!) + 499).toFloat()
                in 11..18 -> return ((12.2 * weight!!) + 746).toFloat()
                in 19..30 -> return ((14.7 * weight!!) + 496).toFloat()
                in 31..60 -> return ((8.7 * weight!!) + 829).toFloat()
                in 60..Int.MAX_VALUE -> return ((10.5 * weight!!) + 596).toFloat()
            }
        }
        return 0F
    }

    private fun setProgressBar(
        currentValue: Float,
        maxValue: Float,
        circularProgressBar: CircularProgressBar,
        currentTextView: TextView,
        maxTextView: TextView,
        color: Int
    ) {
        currentTextView.text = currentValue.toString()
        maxTextView.text = "/$maxValue"
        circularProgressBar.apply {

            // or with animation
            setProgressWithAnimation(currentValue.toFloat(), 2000)

            // Set Progress Max
            progressMax = maxValue.toFloat()

            // Set ProgressBar Color
            progressBarColor = Color.BLACK
            // or with gradient
            progressBarColorStart = Color.GRAY
            progressBarColorEnd = color
            progressBarColorDirection = CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.WHITE
            backgroundProgressBarColorEnd = color
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT

        }

    }


    private fun sumNutritions(
        responses: QuerySnapshot,
        currentDate: Calendar,
        nutrientId: Int
    ): Float {
        var nutrientSum = 0F
        for (response in responses.documents) {
            val foodConsumptionInstance = response.toObject<FoodConsumption>()
            val foodConsumptionDate =
                foodConsumptionInstance!!.date!!.get("date")!!.toDate()
            if (foodConsumptionDate.year == currentDate.time.year && foodConsumptionDate.month == currentDate.time.month
                && foodConsumptionDate.date == currentDate.time.date
            ) {
                val nutrientList = foodConsumptionInstance.foodNutrientList?.foodNutrients
                if (nutrientList != null) {
                    for (nutrient in nutrientList) {
                        if (nutrient.nutrientId == nutrientId) {
                            nutrientSum = (nutrientSum + nutrient.value!!).toFloat()
                        }
                    }
                }
            }
        }
        return nutrientSum
    }

    private fun hideProgressBar() {
        progress_bar_home_fragment.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        progress_bar_home_fragment.visibility = View.VISIBLE
        isLoading = true
    }

    var isLoading = false
}
