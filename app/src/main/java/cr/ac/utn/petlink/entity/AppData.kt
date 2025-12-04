package cr.ac.utn.petlink.entity

object AppData {
    val pets = mutableListOf<Pet>()
    val veterinarians = mutableListOf<Veterinarian>()
    val lostPets = mutableListOf<LostPet>()
    val tips = mutableListOf<Tip>()
    val users = mutableListOf<User>()
    var currentUser: User? = null
}
