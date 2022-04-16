package com.eways.agent.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MoneyUtils {
    companion object{
        fun getAmountString(value : Int ? ): String? {
            val formatter: DecimalFormat = NumberFormat.getInstance(Locale.GERMAN) as DecimalFormat
            formatter.applyPattern("#,###,###,###")
            val formattedString: String = formatter.format(value)
            return "Rp. $formattedString"
        }
    }
}