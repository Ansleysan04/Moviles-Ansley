package cr.ac.utn.petlink.entity

data class Tip(
    var id: Long,
    var title: String,
    var description: String,
    var photoUrl: String? = null
)
