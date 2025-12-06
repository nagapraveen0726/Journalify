package uk.ac.tees.mad.journalify.util


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTime(millis: Long): String {
    if (millis == 0L) return ""
    val sdf = SimpleDateFormat("HH:mm , dd MMM yyyy", Locale.getDefault())
    return sdf.format(Date(millis))
}