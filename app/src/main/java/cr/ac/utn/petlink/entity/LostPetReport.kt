package cr.ac.utn.petlink.entity

import java.util.Date

data class LostPetReport(
    var id: Long,
    var petId: Long,
    var reporterId: Long,
    var lastSeenLocation: String,
    var reportDate: Date,
    var description: String,
    var isFound: Boolean = false
)
