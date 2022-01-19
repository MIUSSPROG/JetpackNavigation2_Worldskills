package com.example.jetpacknavigation2_worldskills.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jetpacknavigation2_worldskills.R
import com.example.jetpacknavigation2_worldskills.databinding.FragmentBoxBinding
import com.example.jetpacknavigation2_worldskills.interfaces.HasCustomTitle
import com.example.jetpacknavigation2_worldskills.interfaces.navigator

class BoxFragment : Fragment(R.layout.fragment_box), HasCustomTitle {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentBoxBinding.inflate(inflater, container, false).apply {
        btnToMain.setOnClickListener { navigator().goToMenu() }
    }.root

    override fun getTitleRes(): Int  = R.string.box
}