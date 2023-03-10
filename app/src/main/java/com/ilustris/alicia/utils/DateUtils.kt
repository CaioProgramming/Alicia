package com.ilustris.alicia.utils

import java.text.SimpleDateFormat
import java.util.*

enum class DateFormats(val formatted: String) {
    DD_OF_MM_FROM_YYYY("dd 'de' MMMM 'de' yyyy"),
    DD_OF_MM("dd 'de' MMMM"),
    DD_MM_YYY("DD/MM/YYY"),
    DD_MM("dd/MM"),
    MM_DD_YYY("MM/DD/YYY"),
    M_D_Y("Month D, Yr"),
    EE_D_MMM_YYY("EEE, MMM d, ''yy"),
    EE_DD_MMM_YYY_HH_MM("EEE, d MMM yyyy HH:mm"),
    DD_MM_HH("dd.mm - HH"),
    RFC_3339("yyyy-MM-dd'T'HH:mm:ss.SSSZZZZZ"),
    HH_MM("HH:mm")
}


fun Date.formatDate(format: String? = null): String {
    val fmt = if (format != null) SimpleDateFormat(
        format,
        Locale.getDefault()
    ) else SimpleDateFormat.getDateInstance()
    return fmt.format(this)
}

fun Date.format(format: DateFormats) : String {
    val fmt =  SimpleDateFormat(
        format.formatted,
        Locale.getDefault()
    )
    return fmt.format(this)
}