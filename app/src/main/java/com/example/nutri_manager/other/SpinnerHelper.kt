package com.example.nutri_manager.other

object SpinnerHelper {
    fun getNutrintId(position: Int): Int {
        if (position == 0) {
            return 1008                 // energy   kJ
        } else if (position == 1) {
            return 1003                 // protein  G
        } else if (position == 2) {
            return 1104                 // vitamin A    IU
        } else if (position == 3) {
            return 1089                 // Iron         MG
        } else if (position == 4){
            return 1093                 // sodium       MG
        }
        return 0
    }

    fun getNutrientUnit(id: Int): String? {
        if (id == 1008) {
            return "kJ"
        } else if (id == 1003) {
            return "G"
        } else if (id == 1104) {
            return "IU"
        } else if (id == 1089 || id == 1093) {
            return "MG"
        } else {
            return "Unit"
        }
    }
}