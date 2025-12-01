package cr.ac.utn.petlink.entity

import java.util.Date

data class Pet(
    var id: Long,
    var name: String,
    var species: String,
    var breed: String,
    var age: Int,
    var ownerId: Long,
    var photoUrl: String? = null,
    var vaccinationRecords: MutableList<String> = mutableListOf(),
    var isForAdoption: Boolean = false
)
