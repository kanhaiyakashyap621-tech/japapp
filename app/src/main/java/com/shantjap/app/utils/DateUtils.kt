package com.shantjap.app.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

object DateUtils {
    private val formatter = DateTimeFormatter.ISO_DATE

    fun today(): LocalDate = LocalDate.now()

    fun format(date: LocalDate): String = date.format(formatter)

    fun parse(dateKey: String): LocalDate = LocalDate.parse(dateKey, formatter)

    fun todayKey(): String = format(today())

    fun dayOfYear(): Int = today().dayOfYear

    fun weekKey(date: LocalDate = today()): String {
        val weekFields = WeekFields.of(Locale.getDefault())
        val week = date.get(weekFields.weekOfWeekBasedYear())
        val year = date.get(weekFields.weekBasedYear())
        return String.format("%04d-W%02d", year, week)
    }

    fun daysBetween(startKey: String, endKey: String): Int {
        val start = parse(startKey)
        val end = parse(endKey)
        return ChronoUnit.DAYS.between(start, end).toInt()
    }
}
