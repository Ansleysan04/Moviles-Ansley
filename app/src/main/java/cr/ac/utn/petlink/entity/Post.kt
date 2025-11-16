package cr.ac.utn.petlink.entity

import java.util.Date

data class Post(
    var id: Long,
    var authorId: Long,
    var text: String,
    var imageUrl: String? = null,
    var postDate: Date,
    var likes: MutableList<Long> = mutableListOf(),
    var comments: MutableList<Comment> = mutableListOf()
)
