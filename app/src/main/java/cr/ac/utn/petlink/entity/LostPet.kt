package cr.ac.utn.petlink.entity

import java.util.Date

data class LostPet(
    var id: Long,
    var name: String,
    var species: String,
    var breed: String,
    var lastSeenLocation: String,
    var lostDate: Date,
    var contactPhone: String,
    var description: String,
    var photoUrl: String? = null
)
