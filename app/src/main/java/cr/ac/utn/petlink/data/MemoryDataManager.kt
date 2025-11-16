package cr.ac.utn.petlink.data

import cr.ac.utn.petlink.entity.*

class MemoryDataManager : IDataManager {

    private val users = mutableListOf<User>()
    private val pets = mutableListOf<Pet>()
    private val adoptionPosts = mutableListOf<AdoptionPost>()
    private val lostPetReports = mutableListOf<LostPetReport>()
    private val posts = mutableListOf<Post>()

    override fun addUser(user: User) {
        users.add(user)
    }

    override fun getUser(id: Long): User? {
        return users.find { it.id == id }
    }

    override fun getAllUsers(): List<User> {
        return users
    }

    override fun updateUser(user: User) {
        val index = users.indexOfFirst { it.id == user.id }
        if (index != -1) {
            users[index] = user
        }
    }

    override fun deleteUser(id: Long) {
        users.removeAll { it.id == id }
    }

    override fun addPet(pet: Pet) {
        pets.add(pet)
    }

    override fun getPet(id: Long): Pet? {
        return pets.find { it.id == id }
    }

    override fun getAllPets(): List<Pet> {
        return pets
    }

    override fun updatePet(pet: Pet) {
        val index = pets.indexOfFirst { it.id == pet.id }
        if (index != -1) {
            pets[index] = pet
        }
    }

    override fun deletePet(id: Long) {
        pets.removeAll { it.id == id }
    }

    override fun addAdoptionPost(post: AdoptionPost) {
        adoptionPosts.add(post)
    }

    override fun getAdoptionPost(id: Long): AdoptionPost? {
        return adoptionPosts.find { it.id == id }
    }

    override fun getAllAdoptionPosts(): List<AdoptionPost> {
        return adoptionPosts
    }

    override fun updateAdoptionPost(post: AdoptionPost) {
        val index = adoptionPosts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            adoptionPosts[index] = post
        }
    }

    override fun deleteAdoptionPost(id: Long) {
        adoptionPosts.removeAll { it.id == id }
    }

    override fun addLostPetReport(report: LostPetReport) {
        lostPetReports.add(report)
    }

    override fun getLostPetReport(id: Long): LostPetReport? {
        return lostPetReports.find { it.id == id }
    }

    override fun getAllLostPetReports(): List<LostPetReport> {
        return lostPetReports
    }

    override fun updateLostPetReport(report: LostPetReport) {
        val index = lostPetReports.indexOfFirst { it.id == report.id }
        if (index != -1) {
            lostPetReports[index] = report
        }
    }

    override fun deleteLostPetReport(id: Long) {
        lostPetReports.removeAll { it.id == id }
    }

    override fun addPost(post: Post) {
        posts.add(post)
    }

    override fun getPost(id: Long): Post? {
        return posts.find { it.id == id }
    }

    override fun getAllPosts(): List<Post> {
        return posts
    }

    override fun updatePost(post: Post) {
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) {
            posts[index] = post
        }
    }

    override fun deletePost(id: Long) {
        posts.removeAll { it.id == id }
    }
}
