package com.hmh.hamyeonham.common.activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit

fun AppCompatActivity.replaceFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.commit {
        replace(containerViewId, fragment)
    }
}

fun AppCompatActivity.addFragment(containerViewId: Int, fragment: Fragment) {
    supportFragmentManager.commit {
        add(containerViewId, fragment)
    }
}


