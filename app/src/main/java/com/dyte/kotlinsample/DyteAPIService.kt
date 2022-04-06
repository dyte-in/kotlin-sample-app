package com.dyte.kotlinsample

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

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