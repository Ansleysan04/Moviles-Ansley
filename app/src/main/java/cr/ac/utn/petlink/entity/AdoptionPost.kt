package cr.ac.utn.petlink.entity

import java.util.Date

data class AdoptionPost(
    var id: Long,
    var petId: Long,
    var posterId: Long,
    var description: String,
    var location: String,
    var postDate: Date,
    var isActive: Boolean = true
)
