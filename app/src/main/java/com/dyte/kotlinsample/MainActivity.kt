package com.dyte.kotlinsample

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Display
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
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
        supportActionBar?.setTitle("Dyte Kotlin Sample")

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        binding.button.setOnClickListener {
            navController.navigate(R.id.FirstFragment)
        }

        binding.button2.setOnClickListener {
            navController.navigate(R.id.SecondFragment)
        }
//        appBarConfiguration = AppBarConfiguration(navController.graph)
//        setupActionBarWithNavController(navController, appBarConfiguration)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    public fun createNewMeeting(meetingTitle: String) {
        val mainActivityJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            showErrorAlert("Error", exception.message)
        }

        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            val api = DyteAPI()
            val meeting = api.createMeeting(CreateMeetingBody(meetingTitle, Authorization(false))).data.meeting;
            showMeetingJoinAlert(meeting)
        }
    }

    public fun joinMeeting(meeting: Meeting, displayName: String, role: String) {
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
                    UserDetails(displayName)
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

    public fun showErrorAlert(title: String, message: String?) {
        AlertDialog.Builder(this).setTitle(title)
            .setMessage(message)
            .setPositiveButton(android.R.string.ok) { _, _ -> }
            .setIcon(android.R.drawable.ic_dialog_alert).show()
    }

    public fun showMeetingJoinAlert(meeting: Meeting) {

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