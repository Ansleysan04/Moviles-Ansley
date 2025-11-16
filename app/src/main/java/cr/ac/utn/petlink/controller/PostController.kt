package cr.ac.utn.petlink.controller

import cr.ac.utn.petlink.entity.Comment
import cr.ac.utn.petlink.entity.Post
import java.util.Date

class PostController {

    fun createPost(authorId: Long, text: String, imageUrl: String?): Post {
        val newPost = Post(
            id = System.currentTimeMillis(),
            authorId = authorId,
            text = text,
            imageUrl = imageUrl,
            postDate = Date()
        )
        // Save the post to your data manager
        return newPost
    }

    fun getPost(postId: Long): Post? {
        // Retrieve post from your data manager
        return null // Placeholder
    }

    fun getAllPosts(): List<Post> {
        // Retrieve all posts from your data manager
        return emptyList() // Placeholder
    }

    fun addCommentToPost(postId: Long, authorId: Long, text: String) {
        val comment = Comment(
            id = System.currentTimeMillis(),
            authorId = authorId,
            text = text,
            commentDate = Date()
        )
        // Retrieve post and add comment
    }

    fun likePost(postId: Long, userId: Long) {
        // Retrieve post and add user to likes list
    }
}
