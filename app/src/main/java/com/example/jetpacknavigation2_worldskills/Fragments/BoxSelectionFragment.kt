package com.example.jetpacknavigation2_worldskills.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jetpacknavigation2_worldskills.R
import com.example.jetpacknavigation2_worldskills.databinding.FragmentBoxBinding
import com.example.jetpacknavigation2_worldskills.interfaces.HasCustomTitle

class BoxSelectionFragment : Fragment(R.layout.fragment_box_selection), HasCustomTitle {
    override fun getTitleRes(): Int = R.string.boxSelection

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentBoxBinding.inflate(inflater, container, false).apply {

    }.root

}