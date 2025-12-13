package cr.ac.utn.petlink.controller

import cr.ac.utn.petlink.entity.LostPetReport
import java.util.Date

class LostPetController {

    fun reportLostPet(petId: Long, reporterId: Long, lastSeenLocation: String, description: String): LostPetReport {
        val newReport = LostPetReport(
            id = System.currentTimeMillis(),
            petId = petId,
            reporterId = reporterId,
            lastSeenLocation = lastSeenLocation,
            reportDate = Date(),
            description = description
        )
        // Save the report to your data manager
        return newReport
    }

    fun getLostPetReport(reportId: Long): LostPetReport? {
        // Retrieve report from your data manager
        return null // Placeholder
    }

    fun getActiveLostPetReports(): List<LostPetReport> {
        // Retrieve all active reports from your data manager
        return emptyList() // Placeholder
    }

    fun markAsFound(reportId: Long) {
        // Retrieve report and set isFound to true
    }
}
