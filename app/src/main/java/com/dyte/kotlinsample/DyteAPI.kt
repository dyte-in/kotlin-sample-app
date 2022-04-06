package com.dyte.kotlinsample

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DyteAPI {
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