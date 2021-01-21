package com.example.excrud

import java.util.*

data class Memo(
    var id: Int,
    var content: String,
    var date: String,
    var bookmark: Boolean
) : Comparable<Memo> {
    constructor() : this(0, "", Date().toString(), false)
    constructor(id: Int) : this(id, "", Date().toString(), false)
    constructor(id: Int, date: String) : this(id, "", date, false)

    override fun compareTo(other: Memo) =
        when {
            this.date < other.date -> -1
            this.date > other.date -> 1
            else -> {
                when {
                    this.id < other.id -> -1
                    else -> 1
                }
            }
        }
}