package com.dyte.kotlinsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dyteMobileSdk.DyteInstanceManager

import android.os.Build

import android.content.Intent

import android.provider.Settings
import android.util.Log
import android.view.View

import android.view.ViewGroup
import android.widget.LinearLayout
import com.dyte.kotlinsample.databinding.ActivityCustomMeetingBinding

import com.dyteclientmobile.DyteMeeting
import com.dyteclientmobile.DyteMeeting.MeetingEventListener

import com.dyteclientmobile.MeetingConfig
import org.json.JSONObject







class CustomMeetingActivity : AppCompatActivity() {
    private val OVERLAY_PERMISSION_REQ_CODE = 1212
    private val TAG = "CustomMeetingActivity"
    private lateinit var binding: ActivityCustomMeetingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomMeetingBinding.inflate(layoutInflater)

        binding.toggleMicButton.setOnClickListener {
            if (DyteMeeting.self.audioEnabled) {
                DyteMeeting.self.disableAudio();
            } else {
                DyteMeeting.self.enableAudio()
            }
        }

        binding.toggleVideoButton.setOnClickListener {
            if (DyteMeeting.self.videoEnabled) {
                DyteMeeting.self.disableVideo();
            } else {
                DyteMeeting.self.enableVideo()
            }
        }

        binding.endCallButton.setOnClickListener{
            DyteMeeting.self.leaveRoom()
            super.onBackPressed();
        }

        setContentView(binding.root)

        val meetingContainer = findViewById<LinearLayout>(R.id.meetingContainer)

        val roomName = intent.getStringExtra("roomName")
        val authToken = intent.getStringExtra("authToken")

        // Configure meeting parameters
        val config = MeetingConfig()
        config.setRoomName(roomName)
            .setAuthToken(authToken)
        DyteMeeting.setup(config)
        val view: View = DyteMeeting.init(this, meetingContainer.width, meetingContainer.height)

        DyteMeeting.addEventListener(object : MeetingEventListener {
            override fun meetingJoined() {
//                Modify UI config here, as needed!
//                val uiConfig = JSONObject()
//                uiConfig.put("controlBar", false)
//                DyteMeeting.updateUIConfig(uiConfig.toString())
            }
        })


        // Example
        val viewGroup = findViewById<ViewGroup>(R.id.meetingContainer)
        viewGroup.addView(view)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    // SYSTEM_ALERT_WINDOW permission not granted
                }
            }
        }
        if (DyteInstanceManager.instance != null) {
            DyteInstanceManager.instance.onActivityResult(this, requestCode, resultCode, data)
        }
    }


    override fun onPause() {
        super.onPause()
        if (DyteInstanceManager.instance != null) {
            DyteInstanceManager.instance.onHostPause(this)
        }
    }

    override fun onResume() {
        super.onResume()
        if (DyteInstanceManager.instance != null) {
            DyteInstanceManager.instance.onHostResume(this, null)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (DyteInstanceManager.instance != null) {
            DyteInstanceManager.instance.onHostDestroy(this)
        }
        if (DyteInstanceManager.instance != null) {
            DyteInstanceManager.unmountApplication()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (DyteInstanceManager.permissionsHandler != null) {
            DyteInstanceManager.permissionsHandler.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }
    }
}