package com.dyte.kotlinsample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class Meeting(
    val id: String,
    val title: String,
    val roomName: String,
    val status: String
)

data class MeetingField(
    val meeting: Meeting
)

data class MeetingResponse(
    val success: Boolean,
    val data: MeetingField
);

data class CreateMeetingBody(
    val title: String,
    val authorization: Authorization = Authorization(),
    var presetName: String? = null
)

data class Authorization(
    val waitingRoom: Boolean = false,
    val closed: Boolean = false
)

data class AddParticipantBody(
    val meetingId: String,
    val clientSpecificId: String,
    val userDetails: UserDetails? = null,
    val roleName: String? = null,
    val presetName: String? = null
)

data class UserDetails(
    val name: String,
    val picture: String? = null
)

data class AuthResponse(
    val userAdded: Boolean,
    val authToken: String
)

data class AuthResponseField(
    val authResponse: AuthResponse
)

data class AddParticipantResponse(
    val success: Boolean,
    val data: AuthResponseField
)

data class MeetingsData(
    val meetings: List<Meeting>
)

data class GetMeetingsResponse(
    val success: Boolean,
    val data: MeetingsData
)


/*
* Retrofit API Definitions to interact with the Dyte Sample App Backend. All API calls should be made
* through your own backend, and NEVER from the frontend/client side. The source for the backend sample
* app can be found here https://github.com/dyte-in/backend-sample-app
* */
interface DyteAPIService {
    @POST("/meeting/create")
    suspend fun createMeeting(@Body params: CreateMeetingBody): MeetingResponse

    @POST("/participant/create")
    suspend fun addParticipant(@Body params: AddParticipantBody): AddParticipantResponse

    @GET("/meetings")
    suspend fun getMeetings(): GetMeetingsResponse
}

public class DyteAPI {
    private val service: DyteAPIService

    companion object {
        const val BASE_URL = "https://dyte-sample.herokuapp.com/"
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(DyteAPIService::class.java)
    }

    suspend fun createMeeting(body: CreateMeetingBody): MeetingResponse {
        return service.createMeeting(body)
    }

    suspend fun addParticipant(body: AddParticipantBody): AddParticipantResponse {
        return service.addParticipant(body)
    }

    suspend fun getMeetings(): GetMeetingsResponse {
        return service.getMeetings()
    }
}