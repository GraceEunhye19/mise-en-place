package com.example.miseenplace.features.timer

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class TimerState{
    IDLE, RUNNING, PAUSED, DONE
}

class TimerViewModel : ViewModel(){

    private  val _timeLeftMillis = MutableStateFlow(0L)
    val timeLeftMillis: StateFlow<Long> = _timeLeftMillis.asStateFlow()

    private  val _timerState = MutableStateFlow(TimerState.IDLE)
    val timerState: StateFlow<TimerState> = _timerState.asStateFlow()

    private var countDownTimer: CountDownTimer? = null
    private var totalMillis = 0L

    private val _selectedHrs = MutableStateFlow(0)
    val selectedHrs: StateFlow<Int> = _selectedHrs.asStateFlow()

    private val _selectedMins = MutableStateFlow(5)
    val selectedMins: StateFlow<Int> = _selectedMins.asStateFlow()

    private val _selectedSecs = MutableStateFlow(0)
    val selectedSecs: StateFlow<Int> = _selectedSecs.asStateFlow()

    fun setHrs(h: Int) { _selectedHrs.value = h }
    fun setMins(m: Int) { _selectedMins.value = m }
    fun setSecs(s: Int) { _selectedSecs.value = s }


    fun start(){
        totalMillis = ((_selectedHrs.value * 3600) + (_selectedMins.value * 60) + _selectedSecs.value) * 1000L //converting everything to millisecs
        if(totalMillis == 0L) return  //no set time

        _timeLeftMillis.value = totalMillis
        _timerState.value = TimerState.RUNNING

        countDownTimer?.cancel() //kill an old timer
        countDownTimer = object : CountDownTimer(totalMillis, 1000L){ //new timer, counting every 1 sec
            override fun onTick(millisUntilFinished: Long) {
                _timeLeftMillis.value = millisUntilFinished  //update
            }
            override fun onFinish() {  //update to done
                _timeLeftMillis.value = 0L
                _timerState.value = TimerState.DONE
            }
        }.start()  //start immediately created
    }

    fun pause(){  //stop, but dont reset
        countDownTimer?.cancel()
        _timerState.value = TimerState.PAUSED
    }

    fun resume(){
        val remaining = _timeLeftMillis.value
        if (remaining == 0L) return
        _timerState.value = TimerState.RUNNING
        countDownTimer = object : CountDownTimer(remaining, 1000L){  //new timer using remaining time
            override fun onTick(millisUntilFinished: Long) {
                _timeLeftMillis.value = millisUntilFinished
            }
            override fun onFinish() {
                _timeLeftMillis.value = 0L
                _timerState.value = TimerState.DONE
            }
        }.start()  //same as start
    }

    fun reset(){  //kill and swipe
        countDownTimer?.cancel()
        _timeLeftMillis.value = 0L
        _timerState.value = TimerState.IDLE
    }

    override fun onCleared() {  //still running if app is minimized
        super.onCleared()
    }
}