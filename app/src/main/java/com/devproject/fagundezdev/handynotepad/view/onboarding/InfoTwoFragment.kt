package com.devproject.fagundezdev.handynotepad.view.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devproject.fagundezdev.handynotepad.R
import kotlinx.android.synthetic.main.fragment_one_onboarding.*
import kotlinx.android.synthetic.main.fragment_two_onboarding.*

class InfoTwoFragment : Fragment() {

    private lateinit var callTwo : callPagerTwo

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callTwo = context as callPagerTwo
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_two_onboarding, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTwotoThree.setOnClickListener {
            callTwo.clickNextThree()
        }

        tvTwotoOne.setOnClickListener {
            callTwo.clickBackOne()
        }
    }
    interface callPagerTwo{
        fun clickNextThree()
        fun clickBackOne()
    }
}