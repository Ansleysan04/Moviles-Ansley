package cr.ac.utn.petlink.entity

import java.util.Date

data class Comment(
    var id: Long,
    var authorId: Long,
    var text: String,
    var commentDate: Date
)
