package com.digitalsanctuary.habittracker.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hour) {
            in 5..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            else -> "Good Night"
        }
    }

    fun getCurrentDateString(): String =
        SimpleDateFormat("EEEE, MMM dd", Locale.getDefault()).format(Date()).uppercase()

    fun getDayAbbrev(offsetFromToday: Int): String {
        val cal = Calendar.getInstance(); cal.add(Calendar.DAY_OF_YEAR, offsetFromToday)
        return SimpleDateFormat("EEE", Locale.getDefault()).format(cal.time).uppercase().take(3)
    }

    fun getDayNumber(offsetFromToday: Int): String {
        val cal = Calendar.getInstance(); cal.add(Calendar.DAY_OF_YEAR, offsetFromToday)
        return SimpleDateFormat("d", Locale.getDefault()).format(cal.time)
    }

    fun formatDate(millis: Long): String =
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(millis))

    fun getTodayDateString(): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    fun getDatesOfWeek(): List<Date> {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DAY_OF_WEEK, cal.firstDayOfWeek)
        val dates = mutableListOf<Date>()
        for (i in 0..6) {
            dates.add(cal.time)
            cal.add(Calendar.DAY_OF_MONTH, 1)
        }
        return dates
    }

    fun formatDateToIso(date: Date): String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    fun getDayAbbrev(date: Date): String = SimpleDateFormat("EEE", Locale.getDefault()).format(date)
    fun getDayNumber(date: Date): String = SimpleDateFormat("d", Locale.getDefault()).format(date)

    fun parseIsoDate(dateStr: String): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr)
        } catch (e: Exception) {
            null
        }
    }
}
