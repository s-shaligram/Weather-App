package com.example.sameermidterm

data class Weather(
    var address: String,
    var days:List<Days>?=null,
    var currentConditions: Conditions,
)
