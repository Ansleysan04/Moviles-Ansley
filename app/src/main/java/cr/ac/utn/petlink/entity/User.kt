package cr.ac.utn.petlink.entity

import java.util.Date

data class User(
    var id: Long,
    var firstName: String,
    var lastName: String,
    var email: String,
    var phone: String,
    var password: String, // Added password
    var photoUrl: String? = null,
    var birthDate: Date? = null,
    var address: String = ""
)
