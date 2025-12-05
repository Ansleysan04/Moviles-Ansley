package cr.ac.utn.petlink.entity

import java.util.Date

data class Pet(
    var id: Long,
    var name: String,
    var species: String,
    var breed: String,
    var age: Int,
    var ownerId: Long,
    var location: String,
    var description: String,
    var photoUrl: String? = null,
    var vaccinationRecords: MutableList<String> = mutableListOf(),
    var isForAdoption: Boolean = false,
    var personality: String = "",
    var behavior: String = "",
    var health: String = "",
    var needs: String = ""
)
