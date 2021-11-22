package com.dyte.kotlinsample

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.dyte.kotlinsample.databinding.ActivityMainBinding
import com.dyteclientmobile.DyteMeeting
import com.dyteclientmobile.DyteMeetingActivity
import com.dyteclientmobile.MeetingConfig
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Dyte Kotlin Sample"

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.createMeetingTabButton.setOnClickListener {
            navController.navigate(R.id.CreateMeetingFragment)
        }

        binding.JoinMeetingTabButton.setOnClickListener {
            navController.navigate(R.id.JoinMeetingFragment)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    /*
    * Creates a new Dyte meeting with the given title, and then shows a popup to join the
    * newly created meeting
    * */
    fun createNewMeeting(meetingTitle: String) {
        val mainActivityJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            showErrorAlert("Error", exception.message)
        }

        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            val api = DyteAPI()
            val meeting = api.createMeeting(CreateMeetingBody(meetingTitle, Authorization(false))).data.meeting
            showMeetingJoinAlert(meeting)
        }
    }

    /*
    * Joins a meeting with a given display name and role.
    * */
    private fun joinMeeting(meeting: Meeting, displayName: String, role: String) {
        val mainActivityJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            showErrorAlert("Error", exception.message)
        }

        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            val api = DyteAPI()
            val participantResponse = api.addParticipant(
                AddParticipantBody(
                    meeting.id,
                    "kotlinSample",
                    UserDetails(displayName),
                    role,
                )
            ).data.authResponse

            val config = MeetingConfig()
            config.setRoomName(meeting.roomName)
            config.setAuthToken(participantResponse.authToken)

            DyteMeeting.setup(config)

            val meetingIntent = Intent(this@MainActivity, DyteMeetingActivity::class.java)
            startActivity(meetingIntent)
        }
    }

    /*
    * Helper function to show error alerts for all exceptions
    * raised by the app
    * */
    fun showErrorAlert(title: String, message: String?) {
        AlertDialog.Builder(this).setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    /*
    * Shows an alert to join a meeting, and prompts the user for the participant
    * name to join as. Also shows two buttons, one to join the meeting with the
    * host role, and one to join with the participant role.
    * */
    fun showMeetingJoinAlert(meeting: Meeting) {

        val viewInflated = layoutInflater.inflate(R.layout.participant_name, findViewById(android.R.id.content),false)
        val txtName = viewInflated.findViewById<EditText>(R.id.participant_name_input)

        AlertDialog.Builder(this)
            .setTitle("Join meeting")
            .setMessage("You are joining the meeting ${meeting.title}")
            .setView(viewInflated)
            .setPositiveButton("Join as Host") {_, _ ->
                joinMeeting(meeting, txtName.text.toString(), "host")
            }
            .setNegativeButton("Join as Participant") {_, _ ->
                joinMeeting(meeting, txtName.text.toString(), "participant")
            }
            .show()

    }
}