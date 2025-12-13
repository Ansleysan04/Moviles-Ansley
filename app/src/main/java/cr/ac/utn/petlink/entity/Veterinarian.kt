package cr.ac.utn.petlink.entity

data class Veterinarian(
    var id: Long,
    var name: String,
    var address: String,
    var phone: String,
    var website: String? = null,
    var imageUrl: String? = null,
    var rating: Float = 0f,
    var distance: Float = 0f
)
