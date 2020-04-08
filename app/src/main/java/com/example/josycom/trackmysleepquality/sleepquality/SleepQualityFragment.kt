package com.example.josycom.trackmysleepquality.sleepquality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.josycom.trackmysleepquality.R
import com.example.josycom.trackmysleepquality.database.SleepDatabase
import com.example.josycom.trackmysleepquality.databinding.FragmentSleepQualityBinding

/**
 * Fragment that displays a list of clickable icons,
 * each representing a sleep quality rating.
 * Once the user taps an icon, the quality is set in the current sleepNight
 * and the database is updated.
 */
class SleepQualityFragment : Fragment() {

    /**
     * Called when the Fragment is ready to display content to the screen.
     *
     * This function uses DataBindingUtil to inflate R.layout.fragment_sleep_quality.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Get a reference to the binding object and inflate the fragment views.
        val binding: FragmentSleepQualityBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_sleep_quality, container, false)

        val arguments = SleepQualityFragmentArgs.fromBundle(arguments!!)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val sleepQualityViewModelFactory = SleepQualityViewModelFactory(arguments.sleepNightKey, dataSource)
        val sleepQualityViewModel = ViewModelProvider(this, sleepQualityViewModelFactory).get(SleepQualityViewModel::class.java)
        sleepQualityViewModel.navigateToSleepTracker.observe(viewLifecycleOwner, Observer {
            if (it == true){
                this.findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToSleepTrackerFragment())
                sleepQualityViewModel.doneNavigating()
            }
        })

        binding.sleepQualityViewModel = sleepQualityViewModel
        return binding.root
    }
}
