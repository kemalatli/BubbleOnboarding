package com.kemalatli.sample

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.kemalatli.bubbleonboarding.BubbleOnboarding
import com.kemalatli.bubbleonboarding.content.bubble.ArrowLocation
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), View.OnClickListener {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button0.setOnClickListener(this)
        button1.setOnClickListener(this)
        button2.setOnClickListener(this)
        button4.setOnClickListener(this)
        button5.setOnClickListener(this)
        button6.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null) {
            BubbleOnboarding.with(requireActivity())
                .focusInRectangle(v)
                .arrowLocation(ArrowLocation.Right())
                .liveIn(lifecycle)
                .title("Here is a title")
                .subtitle("This is a long description of what we can do with this library. This long text should be handled.")
                .okLabel("Ok got it")
                .cancelLabel("No forget it!")
                .show()
        }
    }
}