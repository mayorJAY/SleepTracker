package com.example.josycom.trackmysleepquality.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.josycom.trackmysleepquality.database.SleepDatabaseDao
import kotlinx.coroutines.*

class SleepQualityViewModel(
        private val sleepNightKey: Long = 0L,
        private val databaseDao: SleepDatabaseDao) : ViewModel() {

    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val _navigateToSleepTracker = MutableLiveData<Boolean>()
    val navigateToSleepTracker: LiveData<Boolean>
        get() = _navigateToSleepTracker

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating(){
        _navigateToSleepTracker.value = null
    }

    fun onSetSleepQuality(quality: Int){
        uiScope.launch {
            withContext(Dispatchers.IO){
                val tonight = databaseDao.get(sleepNightKey) ?: return@withContext
                tonight.sleepQuality = quality
                databaseDao.update(tonight)
            }
            _navigateToSleepTracker.value = true
        }
    }
}
