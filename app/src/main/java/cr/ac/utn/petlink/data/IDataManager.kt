package cr.ac.utn.petlink.data

import cr.ac.utn.petlink.entity.*

interface IDataManager {

    // User methods
    fun addUser(user: User)
    fun getUser(id: Long): User?
    fun getAllUsers(): List<User>
    fun updateUser(user: User)
    fun deleteUser(id: Long)

    // Pet methods
    fun addPet(pet: Pet)
    fun getPet(id: Long): Pet?
    fun getAllPets(): List<Pet>
    fun updatePet(pet: Pet)
    fun deletePet(id: Long)

    // AdoptionPost methods
    fun addAdoptionPost(post: AdoptionPost)
    fun getAdoptionPost(id: Long): AdoptionPost?
    fun getAllAdoptionPosts(): List<AdoptionPost>
    fun updateAdoptionPost(post: AdoptionPost)
    fun deleteAdoptionPost(id: Long)

    // LostPetReport methods
    fun addLostPetReport(report: LostPetReport)
    fun getLostPetReport(id: Long): LostPetReport?
    fun getAllLostPetReports(): List<LostPetReport>
    fun updateLostPetReport(report: LostPetReport)
    fun deleteLostPetReport(id: Long)

    // Post methods
    fun addPost(post: Post)
    fun getPost(id: Long): Post?
    fun getAllPosts(): List<Post>
    fun updatePost(post: Post)
    fun deletePost(id: Long)
}
