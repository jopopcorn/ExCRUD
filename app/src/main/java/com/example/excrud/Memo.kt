package com.example.excrud

data class Memo(
    var id: Int?,
    var content: String,
    var date: String,
    var bookmark: Boolean
){
    constructor(): this(null, "", "", false)
}