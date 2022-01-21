package com.example.jetpacknavigation2_worldskills.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.activitynavigation.model.Options
import com.example.jetpacknavigation2_worldskills.R
import com.example.jetpacknavigation2_worldskills.databinding.FragmentBoxBinding
import com.example.jetpacknavigation2_worldskills.databinding.FragmentBoxSelectionBinding
import com.example.jetpacknavigation2_worldskills.databinding.ItemBoxBinding
import com.example.jetpacknavigation2_worldskills.interfaces.HasCustomTitle
import com.example.jetpacknavigation2_worldskills.interfaces.navigator
import java.lang.Long.max
import kotlin.properties.Delegates
import kotlin.random.Random

class BoxSelectionFragment : Fragment(R.layout.fragment_box_selection), HasCustomTitle {


    override fun getTitleRes(): Int = R.string.boxSelection

    private lateinit var options: Options
    private var timerStartTimestamp by Delegates.notNull<Long>()
    private var boxIndex by Delegates.notNull<Int>()
    private var alreadyDone = false
    private lateinit var binding: FragmentBoxSelectionBinding
    private var timerHandler: TimerHandler? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = arguments?.getParcelable(ARG_OPTIONS) ?:
                throw IllegalArgumentException("Can't launch BoxSelectionActivity without options")
        boxIndex = savedInstanceState?.getInt(KEY_INDEX) ?: Random.nextInt(options.boxCount)

        timerHandler = if (options.isTimerEnabled){
            TimerHandler()
        }
        else{
            null
        }

        timerHandler?.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentBoxSelectionBinding.inflate(layoutInflater, container, false)
        createBoxes()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        timerHandler?.onStart()
    }

    override fun onPause() {
        super.onPause()
        timerHandler?.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, boxIndex)
        timerHandler?.onSaveInstanceState(outState)
    }

    private fun createBoxes() {
        val boxBindings = (0 until options.boxCount).map {index ->
            val boxBinding = ItemBoxBinding.inflate(layoutInflater)
            boxBinding.root.id = View.generateViewId()
            boxBinding.tvBoxTitle.text = getString(R.string.box_title, index + 1)
            boxBinding.root.setOnClickListener { view -> onBoxSelected(view) }
            boxBinding.root.tag = index
            binding.root.addView(boxBinding.root)
            boxBinding
        }

        binding.flow.referencedIds = boxBindings.map { it.root.id }.toIntArray()
    }

    private fun onBoxSelected(view: View) {
        if (view.tag as Int == boxIndex) {
            alreadyDone = true // disabling timer if the user made a right choice
            navigator().showCongratulationsScreen()
        } else {
            Toast.makeText(context, R.string.empty_box, Toast.LENGTH_SHORT).show()
        }
    }

    private fun getRemainingSeconds(): Long {
        val finishedAt = timerStartTimestamp + TIMER_DURATION
        return max(0, (finishedAt - System.currentTimeMillis()) / 1000)
    }

    private fun showTimerEndDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("The end")
            .setMessage("Oops^ there is no enough time, try again later.")
            .setCancelable(false)
            .setPositiveButton(android.R.string.ok) { _, _ -> navigator().goBack() }
            .create()
        dialog.show()
    }

    inner class TimerHandler{
        private lateinit var timer: CountDownTimer

        fun onCreate(savedInstanceState: Bundle?) {
            timerStartTimestamp = savedInstanceState?.getLong(KEY_START_TIMESTAMP)
                ?: System.currentTimeMillis()
            alreadyDone = savedInstanceState?.getBoolean(KEY_ALREADY_DONE) ?: false
        }

        fun onSaveInstanceState(outState: Bundle) {
            outState.putLong(KEY_START_TIMESTAMP, timerStartTimestamp)
            outState.putBoolean(KEY_ALREADY_DONE, alreadyDone)
        }

        private fun updateTimerUi() {
            if (getRemainingSeconds() >= 0) {
                binding.tvTimer.visibility = View.VISIBLE
                binding.tvTimer.text = "Timer: " + getRemainingSeconds()
            } else {
                binding.tvTimer.visibility = View.GONE
            }
        }

        fun onStart(){
            if (alreadyDone) return

            timer = object : CountDownTimer(getRemainingSeconds()*1000, 1000){
                override fun onTick(p0: Long) {
                    updateTimerUi()
                }

                override fun onFinish() {
                    updateTimerUi()
                    showTimerEndDialog()
                }

            }
            timer.start()
        }

        fun onStop(){
            timer.cancel()
        }

    }

    companion object {
        private val ARG_OPTIONS = "EXTRA_OPTIONS"
        private val KEY_INDEX = "KEY_INDEX"
        private val KEY_START_TIMESTAMP = "KEY_START_TIMESTAMP"
        private val KEY_ALREADY_DONE = "KEY_ALREADY_DONE"
        private val TIMER_DURATION = 10_000L

        fun createArgs(options: Options) = bundleOf(ARG_OPTIONS to options)
    }

}