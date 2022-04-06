package com.dyte.kotlinsample

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
    var roleName: String? = null,
    var presetName: String? = null
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