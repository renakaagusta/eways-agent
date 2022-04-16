package com.eways.agent.utils.customsupportactionbar

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.eways.agent.R
import kotlinx.android.synthetic.main.supportactionbar.*

class CustomSupportActionBar {
    companion object{
        @JvmStatic
        fun setCustomActionBar(mActivity:AppCompatActivity, text:String){
            val supportActionBar = mActivity.supportActionBar
            supportActionBar?.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
            supportActionBar?.setDisplayShowCustomEnabled(true)
            supportActionBar?.setCustomView(R.layout.supportactionbar)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            supportActionBar?.elevation = 2f

            val actionbarTitle = supportActionBar?.customView?.findViewById<TextView>(R.id.tvActionbarTitle)
            actionbarTitle?.text = text

            mActivity.ibActionbarClose.setOnClickListener{
                mActivity.finish()
            }
        }

        fun setCustomActionBarKabarCluster(mActivity: AppCompatActivity,layout: Int){
            val supportActionBar = mActivity.supportActionBar
            supportActionBar?.displayOptions=ActionBar.DISPLAY_SHOW_CUSTOM
            supportActionBar?.setDisplayShowCustomEnabled(true)
            supportActionBar?.setCustomView(layout)
            supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            supportActionBar?.elevation = 2f

            mActivity.ibActionbarClose.setOnClickListener{
                mActivity.finish()
            }
        }
    }
}
