import kotlinx.datetime.TimeZone

fun main() {
    println(TimeZone.currentSystemDefault().id)
    TimeZone.availableZoneIds.forEach {
        println(it)
    }
}