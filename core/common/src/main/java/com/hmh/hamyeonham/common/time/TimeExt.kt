package com.hmh.hamyeonham.common.time

import android.content.Context
import android.text.format.DateUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

val defaultTimeZone: TimeZone = TimeZone.currentSystemDefault()
fun Instant.Companion.systemNow(): Instant = Clock.System.now()

fun Instant.toDefaultLocalDate(): LocalDate = toLocalDateTime(TimeZone.currentSystemDefault()).date

fun Long.formatDate(context: Context): String =
    DateUtils.formatDateTime(
        context,
        this,
        DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE,
    )

fun Instant.formatDate(context: Context): String = toEpochMilliseconds().formatDate(context)

fun Long.formatNumericDate(context: Context): String =
    DateUtils.formatDateTime(
        context,
        this,
        DateUtils.FORMAT_SHOW_YEAR or DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NUMERIC_DATE,
    )

fun Instant.formatNumericDate(context: Context): String =
    toEpochMilliseconds().formatNumericDate(context)

// LocalDate의 확장 함수로 해당 날짜의 시작 시간과 종료 시간을 LocalDateTime 으로 반환
fun LocalDate.toStartOfDayTime(): LocalDateTime =
    this.atStartOfDayIn(TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault())

fun getNowLocalDateTime(): LocalDateTime =
    Instant.systemNow().toLocalDateTime(TimeZone.currentSystemDefault())

// LocalDateTime 의 확장 함수로 해당 시간을 Epoch 밀리초로 변환
fun LocalDateTime.toEpochMilliseconds(timeZone: TimeZone): Long =
    toInstant(timeZone).toEpochMilliseconds()

// 현재 날짜의 시작 시간과 종료 시간을 Epoch 밀리초로 반환하는 함수
fun getCurrentDayStartEndEpochMillis(): Pair<Long, Long> {
    val currentDate = getCurrentDateOfDefaultTimeZone()
    val startOfDayTime = currentDate.toStartOfDayTime().toEpochMilliseconds(defaultTimeZone)
    val endOfDayTime = getNowLocalDateTime().toEpochMilliseconds(defaultTimeZone)
    if (startOfDayTime > endOfDayTime) {
        throw IllegalArgumentException("startOfDay is greater than endOfDay")
    }
    return Pair(startOfDayTime, endOfDayTime)
}

fun getTargetDayStartEndEpochMillis(targetDate: LocalDate): Pair<Long, Long> {
    val startOfDay = targetDate.toStartOfDayTime().toEpochMilliseconds(defaultTimeZone)
    val endOfDay = getNowLocalDateTime().toEpochMilliseconds(defaultTimeZone)
    return Pair(startOfDay, endOfDay)
}

// yyyy-MM-dd 형식의 date를 반환
fun getCurrentDateOfDefaultTimeZone(): LocalDate {
    return Clock.System.now().toLocalDateTime(defaultTimeZone).date
}

fun minusDaysFromDate(date: LocalDate, daysToMinus: Int): LocalDate {
    return date.minus(daysToMinus, DateTimeUnit.DAY)
}

fun convertTimeToString(minute: Long): String {
    val hours = (minute / 60)
    val minutes = minute % 60

    return buildString {
        if (hours > 0) append(hours.toString() + "시간 ")
        append(minutes.toString() + "분")
    }.trim()
}

// 분을 ms로 바꾸는 함수
fun Int.timeToMs(): Long {
    return this * 60 * 1000L
}

fun Long.msToMin(): Long = this / 1000 / 60
