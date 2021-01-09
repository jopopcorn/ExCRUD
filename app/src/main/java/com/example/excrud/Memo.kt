package com.example.excrud

data class Memo(
    var content: String,
    var date: String,
    var bookmark: Boolean
){
    constructor(): this("", "", false)
}