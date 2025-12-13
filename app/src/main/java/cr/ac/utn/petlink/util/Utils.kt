package cr.ac.utn.petlink.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun formatDate(date: Date): String {
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }

    // You can add other utility functions here, for example:
    // - Image validation
    // - Email validation
    // - etc.
}
