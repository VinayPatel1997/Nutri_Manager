package com.example.nutri_manager.other

import com.anychart.chart.common.dataentry.ValueDataEntry

class CustomDataEntry(
    var x: String,
    var value: Number
) : ValueDataEntry(x, value) {
}