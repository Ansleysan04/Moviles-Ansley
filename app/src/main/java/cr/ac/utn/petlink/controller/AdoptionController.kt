package cr.ac.utn.petlink.controller

import cr.ac.utn.petlink.entity.AdoptionPost
import java.util.Date

class AdoptionController {

    fun createAdoptionPost(petId: Long, posterId: Long, description: String, location: String): AdoptionPost {
        val newPost = AdoptionPost(
            id = System.currentTimeMillis(),
            petId = petId,
            posterId = posterId,
            description = description,
            location = location,
            postDate = Date()
        )
        // Save the post to your data manager
        return newPost
    }

    fun getAdoptionPost(postId: Long): AdoptionPost? {
        // Retrieve post from your data manager
        return null // Placeholder
    }

    fun getActiveAdoptionPosts(): List<AdoptionPost> {
        // Retrieve all active posts from your data manager
        return emptyList() // Placeholder
    }

    fun deactivateAdoptionPost(postId: Long) {
        // Retrieve post and set isActive to false
    }
}
