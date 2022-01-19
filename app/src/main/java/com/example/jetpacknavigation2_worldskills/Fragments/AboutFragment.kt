package com.example.jetpacknavigation2_worldskills.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.jetpacknavigation2_worldskills.BuildConfig
import com.example.jetpacknavigation2_worldskills.R
import com.example.jetpacknavigation2_worldskills.databinding.FragmentAboutBinding
import com.example.jetpacknavigation2_worldskills.interfaces.HasCustomTitle
import com.example.jetpacknavigation2_worldskills.interfaces.navigator

class AboutFragment : Fragment(R.layout.fragment_about), HasCustomTitle {

    private lateinit var binding: FragmentAboutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = FragmentAboutBinding.inflate(inflater, container, false).apply {
        tvAppName2.text = BuildConfig.VERSION_NAME
        tvVerCode2.text = BuildConfig.VERSION_CODE.toString()
        btnOk.setOnClickListener { navigator().goBack() }
    }.root

    override fun getTitleRes(): Int = R.string.about
}