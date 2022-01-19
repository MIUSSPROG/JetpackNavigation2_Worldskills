package com.example.jetpacknavigation2_worldskills.interfaces

import androidx.annotation.StringRes

interface HasCustomTitle {

    @StringRes
    fun getTitleRes(): Int
}