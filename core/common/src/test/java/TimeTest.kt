import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hmh.hamyeonham.common.time.convertTimeToString
import com.hmh.hamyeonham.common.time.defaultTimeZone
import com.hmh.hamyeonham.common.time.formatDate
import com.hmh.hamyeonham.common.time.formatNumericDate
import com.hmh.hamyeonham.common.time.getCurrentDateOfDefaultTimeZone
import com.hmh.hamyeonham.common.time.getCurrentDayStartEndEpochMillis
import com.hmh.hamyeonham.common.time.getNowLocalDateTime
import com.hmh.hamyeonham.common.time.getTargetDayStartEndEpochMillis
import com.hmh.hamyeonham.common.time.hourToMs
import com.hmh.hamyeonham.common.time.minusDaysFromDate
import com.hmh.hamyeonham.common.time.msToMin
import com.hmh.hamyeonham.common.time.systemNow
import com.hmh.hamyeonham.common.time.timeToMs
import com.hmh.hamyeonham.common.time.toDefaultLocalDate
import com.hmh.hamyeonham.common.time.toEpochMilliseconds
import com.hmh.hamyeonham.common.time.toStartOfDayTime
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
import org.junit.Test
import org.junit.runner.RunWith


import kotlin.time.Duration.Companion.minutes

@RunWith(AndroidJUnit4::class)
class TimeUtilsTest {

    // systemNow 함수 테스트
    @Test
    fun testSystemNow() {
        val now = Instant.systemNow()
        val current = Clock.System.now()
        assertTrue(now in (current - 1.minutes)..(current + 1.minutes))
    }

    // Instant를 기본 LocalDate로 변환하는 테스트
    @Test
    fun testToDefaultLocalDate() {
        val now = Instant.systemNow()
        val localDate = now.toDefaultLocalDate()
        val expectedDate = now.toLocalDateTime(defaultTimeZone).date
        assertEquals(expectedDate, localDate)
    }

    // Instant를 포맷팅하는 테스트
    @Test
    fun testFormatInstantDate() {
        val instant = Instant.parse("2024-06-15T00:00:00Z")
        val formattedDate = instant.formatDate()
        val expectedDate = "2024년 6월 15일"
        assertEquals(expectedDate, formattedDate)
    }

    // Long을 포맷팅하는 테스트 (Epoch 밀리초)
    @Test
    fun testFormatLongDate() {
        val epochMillis = Instant.parse("2024-06-15T00:00:00Z").toEpochMilliseconds()
        val formattedDate = epochMillis.formatDate()
        val expectedDate = "2024년 6월 15일"
        assertEquals(expectedDate, formattedDate)
    }

    // Instant를 숫자 날짜로 포맷팅하는 테스트
    @Test
    fun testFormatInstantNumericDate() {
        val instant = Instant.parse("2024-06-15T00:00:00Z")
        val formattedDate = instant.formatNumericDate()
        val expectedDate = "2024-06-15"
        assertEquals(expectedDate, formattedDate)
    }

    // Long을 숫자 날짜로 포맷팅하는 테스트 (Epoch 밀리초)
    @Test
    fun testFormatLongNumericDate() {
        val epochMillis = Instant.parse("2024-06-15T00:00:00Z").toEpochMilliseconds()
        val formattedDate = epochMillis.formatNumericDate()
        val expectedDate = "2024-06-15"
        assertEquals(expectedDate, formattedDate)
    }


    // LocalDate를 하루의 시작 시간 LocalDateTime으로 변환하는 테스트
    @Test
    fun testToStartOfDayTime() {
        val date = LocalDate(2024, 6, 15)
        val startOfDay = date.toStartOfDayTime()
        val expectedStartOfDay = date.atStartOfDayIn(TimeZone.currentSystemDefault())
            .toLocalDateTime(TimeZone.currentSystemDefault())
        assertEquals(expectedStartOfDay, startOfDay)
    }

    // 현재 LocalDateTime을 얻는 함수 테스트
    @Test
    fun testGetNowLocalDateTime() {
        val now = getNowLocalDateTime()
        val currentInstant = Clock.System.now()

        // 현재 시간에서 1분을 빼고 더하는 Duration 사용
        val oneMinuteBefore =
            currentInstant.minus(1.minutes).toLocalDateTime(TimeZone.currentSystemDefault())
        val oneMinuteAfter =
            currentInstant.plus(1.minutes).toLocalDateTime(TimeZone.currentSystemDefault())

        assertTrue(now in oneMinuteBefore..oneMinuteAfter)
    }


    // LocalDateTime을 Epoch 밀리초로 변환하는 함수 테스트
    @Test
    fun testToEpochMilliseconds() {
        val now = LocalDateTime(2024, 6, 15, 12, 0, 0)
        val epochMillis = now.toEpochMilliseconds(defaultTimeZone)
        val expectedMillis = now.toInstant(defaultTimeZone).toEpochMilliseconds()
        assertEquals(expectedMillis, epochMillis)
    }

    // 현재 날짜의 시작 시간과 종료 시간을 Epoch 밀리초로 반환하는 함수 테스트
    @Test
    fun testGetCurrentDayStartEndEpochMillis() {
        val (startMillis, endMillis) = getCurrentDayStartEndEpochMillis()
        val currentDate = getCurrentDateOfDefaultTimeZone()
        val expectedStartMillis =
            currentDate.toStartOfDayTime().toEpochMilliseconds(defaultTimeZone)
        val expectedEndMillis = getNowLocalDateTime().toEpochMilliseconds(defaultTimeZone)
        assertEquals(expectedStartMillis, startMillis)
        assertEquals(expectedEndMillis, endMillis)
    }

    // 타겟 날짜의 시작 시간과 종료 시간을 Epoch 밀리초로 반환하는 함수 테스트
    @Test
    fun testGetTargetDayStartEndEpochMillis() {
        val targetDate = LocalDate(2024, 6, 15)
        val (startMillis, endMillis) = getTargetDayStartEndEpochMillis(targetDate)
        val expectedStartMillis = targetDate.toStartOfDayTime().toEpochMilliseconds(defaultTimeZone)
        val expectedEndMillis = getNowLocalDateTime().toEpochMilliseconds(defaultTimeZone)
        assertEquals(expectedStartMillis, startMillis)
        assertEquals(expectedEndMillis, endMillis)
    }

    // 기본 타임존의 현재 날짜를 반환하는 함수 테스트
    @Test
    fun testGetCurrentDateOfDefaultTimeZone() {
        val currentDate = getCurrentDateOfDefaultTimeZone()
        val expectedDate = Clock.System.now().toLocalDateTime(defaultTimeZone).date
        assertEquals(expectedDate, currentDate)
    }

    // 날짜에서 일수를 빼는 함수 테스트
    @Test
    fun testMinusDaysFromDate() {
        val date = LocalDate(2024, 6, 15)
        val resultDate = minusDaysFromDate(date, 5)
        val expectedDate = date.minus(5, DateTimeUnit.DAY)
        assertEquals(expectedDate, resultDate)
    }

    // 시간을 문자열로 변환하는 함수 테스트
    @Test
    fun testConvertTimeToString() {
        val minutes = 125L
        val timeString = convertTimeToString(minutes)
        val expectedString = "2시간 5분"
        assertEquals(expectedString, timeString)
    }

    // 분을 밀리초로 변환하는 함수 테스트
    @Test
    fun testTimeToMs() {
        val minutes = 5
        val millis = minutes.timeToMs()
        val expectedMillis = 5 * 60 * 1000L
        assertEquals(expectedMillis, millis)
    }

    // 시간을 밀리초로 변환하는 함수 테스트
    @Test
    fun testHourToMs() {
        val hours = 3
        val millis = hours.hourToMs()
        val expectedMillis = 3 * 60 * 60 * 1000L
        assertEquals(expectedMillis, millis)
    }

    // 밀리초를 분으로 변환하는 함수 테스트
    @Test
    fun testMsToMin() {
        val millis = 300000L
        val minutes = millis.msToMin()
        val expectedMinutes = 5L
        assertEquals(expectedMinutes, minutes)
    }
}
