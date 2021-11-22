package com.dyte.kotlinsample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dyte.kotlinsample.databinding.CreateMeetingFragmentBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class CreateMeetingFragment : Fragment() {

    private var _binding: CreateMeetingFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = CreateMeetingFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Create a new meeting on clicking the "Create" button, using the meeting
        // title entered by the user
        binding.createMeetingButton.setOnClickListener{
            (activity as MainActivity).createNewMeeting(binding.textInputLayout.editText?.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}