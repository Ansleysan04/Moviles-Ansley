package cr.ac.utn.petlink.controller

import cr.ac.utn.petlink.entity.User

class UserController {

    fun registerUser(name: String, email: String, phone: String, password: String): User {
        // Here you would typically hash the password before saving
        val newUser = User(
            id = System.currentTimeMillis(), // Using timestamp for unique ID
            name = name,
            email = email,
            phone = phone,
            password = password
        )
        // Save the user to your data manager
        return newUser
    }

    fun loginUser(email: String, password: String): User? {
        // Retrieve user from your data manager and check password
        return null // Placeholder
    }

    fun getUserProfile(userId: Long): User? {
        // Retrieve user from your data manager
        return null // Placeholder
    }

    fun updateUserProfile(userId: Long, name: String, email: String, phone: String): User? {
        // Retrieve user, update details, and save back to your data manager
        return null // Placeholder
    }
}
