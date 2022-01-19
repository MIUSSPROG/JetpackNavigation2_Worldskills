package com.example.jetpacknavigation2_worldskills.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import androidx.fragment.app.Fragment
import com.example.activitynavigation.model.Options
import com.example.jetpacknavigation2_worldskills.R
import com.example.jetpacknavigation2_worldskills.databinding.FragmentOptionsBinding
import com.example.jetpacknavigation2_worldskills.interfaces.CustomAction
import com.example.jetpacknavigation2_worldskills.interfaces.HasCustomAction
import com.example.jetpacknavigation2_worldskills.interfaces.HasCustomTitle
import com.example.jetpacknavigation2_worldskills.interfaces.navigator

class OptionsFragment : Fragment(R.layout.fragment_options), HasCustomTitle, HasCustomAction {

    private lateinit var options: Options
    private lateinit var binding: FragmentOptionsBinding
    private lateinit var boxCountItems: List<BoxCountItem>
    private lateinit var adapter: ArrayAdapter<BoxCountItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = Options.DEFAULT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?{
        binding = FragmentOptionsBinding.inflate(inflater, container, false)
        binding.btnConfirm.setOnClickListener { onConfirmPressed() }
        setupCheckBox()
        setupSpinner()
        updateUi()

        return binding.root
    }

    override fun getTitleRes(): Int = R.string.options

    override fun getCustomAction(): CustomAction {
        return CustomAction(
            iconRes = R.drawable.ic_done,
            textRes = R.string.done,
            onCustomAction = Runnable {
                onConfirmPressed()
            }
        )
    }

    private fun updateUi(){
        binding.cbEnableTimer.isChecked = options.isTimerEnabled

        val currentIndex = boxCountItems.indexOfFirst { it.count == options.boxCount }
        binding.spBoxesCount.setSelection(currentIndex)
    }

    private fun setupCheckBox(){
        binding.cbEnableTimer.setOnClickListener{
            options = options.copy(isTimerEnabled = (it as CheckBox).isChecked)
        }
    }

    private fun setupSpinner() {
        boxCountItems = (1..6).map { BoxCountItem(it, "$it boxes") }
        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, boxCountItems)

        binding.spBoxesCount.adapter = adapter
        binding.spBoxesCount.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, view: View?, position: Int, p3: Long) {
                val count = boxCountItems[position].count
                options = options.copy(boxCount = count)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun onConfirmPressed() {
        navigator().publishResult(options)
        navigator().goBack()
    }


    class BoxCountItem(
        val count: Int,
        private val optionTitle: String
    ){
        override fun toString(): String {
            return optionTitle
        }
    }
}