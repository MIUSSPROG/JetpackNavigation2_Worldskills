package com.example.jetpacknavigation2_worldskills.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.activitynavigation.model.Options
import com.example.jetpacknavigation2_worldskills.R
import com.example.jetpacknavigation2_worldskills.databinding.FragmentMenuBinding
import com.example.jetpacknavigation2_worldskills.interfaces.navigator

class MenuFragment : Fragment(R.layout.fragment_menu){

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMenuBinding.inflate(inflater, container, false).apply {

        navigator().listenResult(Options::class.java, viewLifecycleOwner, {
            options = it
            Toast.makeText(requireContext(), options.toString(), Toast.LENGTH_SHORT).show()
        })

        btnAbout.setOnClickListener{ navigator().showAboutScreen() }
        btnOptions.setOnClickListener { navigator().showOptionsScreen(options) }
    }.root

    companion object {
        private val KEY_OPTIONS = "OPTIONS"
    }

}