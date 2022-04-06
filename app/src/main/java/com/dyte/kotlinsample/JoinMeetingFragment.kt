package com.dyte.kotlinsample

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dyte.kotlinsample.databinding.JoinMeetingFragmentBinding
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class JoinMeetingFragment : Fragment() {

    private var _binding: JoinMeetingFragmentBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = JoinMeetingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getMeetings() {
        val mainActivityJob = Job()

        val errorHandler = CoroutineExceptionHandler { _, exception ->
            binding.llLoaderContainer.visibility = View.GONE
            (activity as MainActivity).showErrorAlert("Error", exception.message)
        }

        val coroutineScope = CoroutineScope(mainActivityJob + Dispatchers.Main)
        coroutineScope.launch(errorHandler) {
            withContext(Dispatchers.Main) {
                binding.llLoaderContainer.visibility = View.VISIBLE
            }
            val api = DyteAPI()
            val meetings = api.getMeetings()
            // Populate the Recycler View using our custom Meeting Adapter
            binding.meetingListView.adapter =
                this@JoinMeetingFragment.context?.let {
                    MeetingAdapter(
                        (activity as Context),
                        meetings.data.meetings
                    )
                }
            withContext(Dispatchers.Main) {
                binding.llLoaderContainer.visibility = View.GONE
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMeetings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}