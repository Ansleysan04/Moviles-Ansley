package cr.ac.utn.petlink.controller

import cr.ac.utn.petlink.entity.Pet
import java.util.Date

class PetController {

    fun addPet(name: String, species: String, breed: String, age: Int, ownerId: Long): Pet {
        val newPet = Pet(
            id = System.currentTimeMillis(),
            name = name,
            species = species,
            breed = breed,
            age = age,
            ownerId = ownerId
        )
        // Save the pet to your data manager
        return newPet
    }

    fun getPet(petId: Long): Pet? {
        // Retrieve pet from your data manager
        return null // Placeholder
    }

    fun updatePet(petId: Long, name: String, species: String, breed: String, age: Int): Pet? {
        // Retrieve pet, update details, and save back to your data manager
        return null // Placeholder
    }

    fun addVaccinationRecord(petId: Long, record: String) {
        // Retrieve pet and add vaccination record
    }

    fun uploadPetPhoto(petId: Long, photoUrl: String) {
        // Retrieve pet and update photo url
    }
}
