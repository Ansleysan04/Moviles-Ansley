package cr.ac.utn.petlink.entity

data class User(
    var id: Long,
    var name: String,
    var email: String,
    var phone: String,
    // For simplicity, we'll store the password as a plain string.
    // In a real application, you should hash and salt the password.
    var password: String
)
