package com.eways.agent.utils.date

import android.util.Log
import java.io.Serializable
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class SLDate : Serializable {
    var date: Date? = null

    private var monthName = arrayOf(
        "Januari",
        "Februari",
        "Maret",
        "April",
        "Mei",
        "Juni",
        "Juli",
        "Agustus",
        "September",
        "Oktober",
        "November",
        "Desember"
    )

    fun getLocalizeDateString(): String? {
        if (date == null) return "-"
        val sdfDate = SimpleDateFormat("dd")
        val sdfMonth = SimpleDateFormat("MM")
        val sdfYear = SimpleDateFormat("yyyy")
        val monthValue: Int = sdfMonth.format(date).toInt()
        return sdfDate.format(date).toString() + " " + monthName[monthValue - 1] + " " + sdfYear.format(
            date
        )
    }

    fun getDateString(): String? {
        return getFormattedDateString("yyyy-MM-dd")
    }

    private fun getFormattedDateString(format: String?): String? {
        if (date == null) return ""
        val sdf = SimpleDateFormat()
        sdf.applyPattern(format)
        return sdf.format(date)
    }

    fun setDate(date: Date?): SLDate? {
        this.date = date
        return this
    }

    fun parseDateString(date: String?, format: String?) {
        if (date == null) return
        val sdf = SimpleDateFormat(format)
        try {
            this.date = sdf.parse(date)
        } catch (ex: ParseException) {
            Log.v("Exception", ex.localizedMessage)
        }
    }
}