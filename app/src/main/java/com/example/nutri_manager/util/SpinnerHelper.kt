package com.example.nutri_manager.util

object SpinnerHelper {
    fun getNutrintId(position: Int): Int {
        if (position == 0) {
            return 1008                 // energy   kJ
        } else if (position == 1) {
            return 1003                 // protein  G
        } else if (position == 2) {
            return 1087                 // Calcium, Ca
        } else if (position == 3) {
            return 1004                 // Total lipid (fat)
        } else if (position == 4){
            return 1005                 // Carbohydrate, by difference
        } else if (position == 5) {
            return 1011                 // Glucose (dextrose)
        } else if (position == 6){
            return 1085                 //Total fat (NLEA)
        } else if (position == 7){
            return 1104                 //Vitamin A, IU
        } else if (position == 8){
            return 1106                 // Vitamin A, RAE
        } else if (position == 9) {
            return 1109                 // Vitamin E (alpha-tocopherol)
        } else if (position ==10){
            return 1110                 //Vitamin D (D2 + D3), International Units
        } else if (position == 11){
            return 1111                 //Vitamin D2 (ergocalciferol)
        } else if (position == 12){
            return 1112                 //Vitamin D3 (cholecalciferol)
        } else if (position == 13){
            return 1114                 //Vitamin D (D2 + D3)
        } else if (position == 14){
            return 1158                 //Vitamin E
        } else if (position == 15){
            return 1168                 //Vitamin C, total ascorbic acid
        } else if (position == 16){
            return 1175                 // Vitamin B-6
        } else if (position == 17){
            return 1178                 //Vitamin B-12
        } else if (position == 18){
            return 1257                 // Fatty acids, total trans
        } else if (position == 19){
            return 1258                 //Fatty acids, total saturated
        } else if (position == 20){
            return 1079                 // Fiber, total dietary
        } else if (position == 21){
            return 1082                 //Fiber, soluble
        } else if (position == 22){
            return 1084                 //Fiber, insoluble
        } else if (position == 23){
            return 1089                 // Iron, Fe
        } else if (position == 24){
            return 1090                 //Magnesium, Mg
        } else if (position == 25){
            return 1091                 // Phosphorus, P
        } else if (position == 26){
            return 1092                 //Potassium, K
        } else if (position == 27){
            return 1093                 //Sodium, Na
        } else if (position == 28){
            return 1094                 // Sulfur, S
        } else if (position == 29){
            return 1095                 //Zinc, Zn
        } else if (position == 30){
            return 1096                 //Chromium, Cr
        } else if (position == 31){
            return 1097                 //Cobalt, Co
        } else if (position == 32){
            return 1098                 //Copper, Cu
        } else if (position == 33){
            return 1099                 //Fluoride, F
        } else if (position == 34){
            return 1100                 //Iodine, I
        } else if (position == 35){
            return 1101                 //Manganese, Mn
        } else if(position == 36){
            return 1102                 //Molybdenum, Mo
        } else if (position == 37){
            return 1103                 //Selenium, Se
        } else if (position == 38){
            return 1088                 //Chlorine, Cl
        } else if (position == 39){
            return 1009                 // Starch
        } else if (position == 40){
            return 1051                 //Water
        } else if (position == 41){
            return 1253                 //Cholesterol
        }
        return 0
    }

    fun getNutrientUnit(id: Int): String? {
        return if (id == 1008) {
            "KCAL"
        } else if (id == 1003 || id == 1004 || id == 1005 || id ==1009 || id ==1011 || id == 1051 || id ==1079|| id ==1082|| id ==1084|| id ==1085 || id ==1257|| id ==1258) {
            "G"
        } else if (id == 1104|| id ==1110) {
            "IU"
        } else if (id == 1089 || id == 1093|| id ==1087|| id ==1088|| id ==1090|| id ==1091|| id ==1092|| id ==1094|| id ==1095|| id ==1098|| id ==1101|| id ==1109|| id ==1162|| id ==1175|| id ==1153) {
            "MG"
        } else if (id == 1196|| id ==1097|| id ==1099|| id ==1100|| id ==1102|| id ==1103|| id ==1106|| id ==1111|| id ==1112|| id ==1114|| id ==1178){
            "UG"
        } else if (id ==1158){
            "MG_ATE"
        }else {
            "Unit"
        }
    }

    fun getNutrientName(id: Int): String{
        if (id == 1008) {
            return "Energy"
        } else if (id == 1003) {
            return "Protein"
        } else if (id == 1087){
            return "Calcium, Ca"
        } else if (id == 1004){
            return "Total lipid (fat)"
        } else if (id ==1005 ){
            return "Carbohydrate, by difference"
        } else if (id == 1011){
            return "1011"
        } else if (id == 1085){
            return "Total fat (NLEA)"
        } else if (id == 1104){
            return "Vitamin A, IU"
        } else if (id == 1106){
            return "Vitamin A, RAE"
        } else if (id == 1109){
            return "Vitamin E (alpha-tocopherol)"
        } else if (id == 1110){
            return "Vitamin D (D2 + D3), International Units"
        } else if (id == 1111){
            return "Vitamin D2 (ergocalciferol)"
        } else if (id == 1112){
            return "Vitamin D3 (cholecalciferol)"
        } else if (id == 1114){
            return "Vitamin D (D2 + D3)"
        } else if (id == 1158){
            return "Vitamin E"
        } else if (id == 1168){
            return "Vitamin C, total ascorbic acid"
        } else if (id == 1175){
            return "Vitamin B-6"
        } else if (id == 1178){
            return "Vitamin B-12"
        } else if (id == 1257){
            return "Fatty acids, total trans"
        } else if (id == 1258){
            return "Fatty acids, total saturated"
        } else if (id == 1079){
            return "Fiber, total dietary"
        } else if (id == 1082){
            return "Fiber, soluble"
        } else if (id == 1084){
            return "Fiber, insoluble"
        } else if (id == 1089){
            return "Iron, Fe"
        } else if (id == 1090){
            return "Magnesium, Mg"
        } else if (id == 1091){
            return "Phosphorus, P"
        } else if (id == 1092){
            return "Potassium, K"
        } else if (id == 1093){
            return "Sodium, Na"
        } else if (id == 1094 ){
            return "Sulfur, S"
        } else if (id == 1095){
            return "Zinc, Zn"
        } else if (id == 1096){
            return "Chromium, Cr"
        } else if (id == 1097){
            return "Cobalt, Co"
        } else if (id == 1098){
            return "Copper, Cu"
        } else if (id == 1099){
            return "Fluoride, F"
        } else if (id == 1100){
            return "Iodine, I"
        } else if (id == 1101){
            return "Manganese, Mn"
        } else if (id == 1102){
            return "Molybdenum, Mo"
        } else if (id == 1103){
            return "Selenium, Se"
        } else if (id == 1088){
            return "Chlorine, Cl"
        } else if (id == 1009){
            return "Starch"
        } else if (id == 1051){
            return "Water"
        } else if (id == 1253){
            return "Cholesterol"
        } else {
            return "Nutrient"
        }
    }
}