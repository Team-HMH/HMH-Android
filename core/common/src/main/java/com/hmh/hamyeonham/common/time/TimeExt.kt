package com.hmh.hamyeonham.common.time

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

// 현재 시간을 Epoch 밀리초로 반환하는 함수
// 예시 값: 1737077696789L (현재 시간에 따라 변동)
fun getNowTime(): Long {
    return Instant.systemNow().toEpochMilliseconds()
}

// 현재 날짜를 숫자 형식으로 반환하는 함수
// 예시 값: "2024-06-15"
fun getNowDateNumeric(): String {
    return getNowTime().formatNumericDate()
}
// 어제 날짜를 숫자 형식으로 반환하는 함수
// 예시 값: "2024-06-14"
fun getYesterdayDateNumeric(): String {
    return minusDaysFromDate(getCurrentDateOfDefaultTimeZone(), 1).toString()
}

// 현재 시스템 시간을 Instant로 반환하는 함수
// 예시 값: 2024-06-15T12:34:56.789Z
fun Instant.Companion.systemNow(): Instant = Clock.System.now()

// Instant를 기본 타임존의 LocalDate로 변환하는 확장 함수
// 예시 값: 2024-06-15
fun Instant.toDefaultLocalDate(): LocalDate = toLocalDateTime(defaultTimeZone).date

// Instant를 사람이 읽을 수 있는 날짜 형식으로 포맷팅하는 확장 함수
// 예시 값: "2024년 6월 15일"
fun Instant.formatDate(): String {
    val date = this.toLocalDateTime(defaultTimeZone).date
    return "${date.year}년 ${date.monthNumber}월 ${date.dayOfMonth}일"
}

// Long을 사람이 읽을 수 있는 날짜 형식으로 포맷팅하는 확장 함수 (Epoch 밀리초)
// 예시 값: "2024년 6월 15일" (입력: 1737077696789L)
fun Long.formatDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.formatDate()
}

// Instant를 숫자 날짜 형식(yyyy-MM-dd)으로 포맷팅하는 확장 함수
// 예시 값: "2024-06-15"
fun Instant.formatNumericDate(): String {
    val date = this.toLocalDateTime(defaultTimeZone).date
    return "${date.year}-${
        date.monthNumber.toString().padStart(2, '0')
    }-${date.dayOfMonth.toString().padStart(2, '0')}"
}

// Long을 숫자 날짜 형식(yyyy-MM-dd)으로 포맷팅하는 확장 함수 (Epoch 밀리초)
// 예시 값: "2024-06-15" (입력: 1737077696789L)
fun Long.formatNumericDate(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    return instant.formatNumericDate()
}

// LocalDate의 확장 함수로 해당 날짜의 시작 시간을 LocalDateTime으로 반환
// 예시 값: 2024-06-15T00:00
fun LocalDate.toStartOfDayTime(): LocalDateTime =
    this.atStartOfDayIn(defaultTimeZone)
        .toLocalDateTime(defaultTimeZone)

// 현재 시간을 LocalDateTime으로 반환하는 함수
// 예시 값: 2024-06-15T12:34:56.789 (현재 시간에 따라 변동)
fun getNowLocalDateTime(): LocalDateTime =
    Instant.systemNow().toLocalDateTime(defaultTimeZone)

// LocalDateTime을 Epoch 밀리초로 변환하는 확장 함수
// 예시 값: 1737077696789L (입력: 2024-06-15T12:34:56.789, TimeZone: defaultTimeZone)
fun LocalDateTime.toEpochMilliseconds(timeZone: TimeZone): Long =
    toInstant(timeZone).toEpochMilliseconds()

// 현재 날짜의 시작 시간과 종료 시간을 Epoch 밀리초로 반환하는 함수
// 예시 값: Pair(1737072000000L, 1737077696789L) (현재 날짜의 시작 및 종료 시간)
fun getCurrentDayStartEndEpochMillis(): Pair<Long, Long> {
    val currentDate = getCurrentDateOfDefaultTimeZone()
    val startOfDayTime = currentDate.toStartOfDayTime().toEpochMilliseconds(defaultTimeZone)
    val endOfDayTime = getNowLocalDateTime().toEpochMilliseconds(defaultTimeZone)
    if (startOfDayTime > endOfDayTime) {
        throw IllegalArgumentException("startOfDay is greater than endOfDay")
    }
    return Pair(startOfDayTime, endOfDayTime)
}

// 주어진 날짜의 시작 시간과 종료 시간을 Epoch 밀리초로 반환하는 함수
// 예시 값: Pair(1737072000000L, 1737077696789L) (입력: 2024-06-15)
fun getTargetDayStartEndEpochMillis(targetDate: LocalDate): Pair<Long, Long> {
    val startOfDay = targetDate.toStartOfDayTime().toEpochMilliseconds(defaultTimeZone)
    val endOfDay = getNowLocalDateTime().toEpochMilliseconds(defaultTimeZone)
    return Pair(startOfDay, endOfDay)
}

// 현재 날짜를 yyyy-MM-dd 형식으로 반환하는 함수
// 예시 값: 2024-06-15
fun getCurrentDateOfDefaultTimeZone(): LocalDate {
    return Clock.System.now().toLocalDateTime(defaultTimeZone).date
}

// 주어진 날짜에서 특정 일수를 빼는 함수
// 예시 값: 2024-06-10 (입력: 2024-06-15, daysToMinus: 5)
fun minusDaysFromDate(date: LocalDate, daysToMinus: Int): LocalDate {
    return date.minus(daysToMinus, DateTimeUnit.DAY)
}

// 분을 시간과 분으로 변환하여 문자열로 반환하는 함수
// 예시 값: "2시간 5분" (입력: 125)
fun convertTimeToString(minute: Long): String {
    val hours = (minute / 60)
    val minutes = minute % 60

    return buildString {
        if (hours > 0) append(hours.toString() + "시간 ")
        append(minutes.toString() + "분")
    }.trim()
}

// 분을 밀리초로 변환하는 함수
// 예시 값: 300000L (입력: 5)
fun Int.timeToMs(): Long {
    return this * 60 * 1000L
}

// 시간을 밀리초로 변환하는 함수
// 예시 값: 10800000L (입력: 3)
fun Int.hourToMs(): Long {
    return this * 60 * 60 * 1000L
}

// 밀리초를 분으로 변환하는 함수
// 예시 값: 5 (입력: 300000L)
fun Long.msToMin(): Long = this / 1000 / 60