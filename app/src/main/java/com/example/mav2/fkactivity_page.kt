package com.example.mav2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_fkpage.*

class fkactivity_page : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fkpage)

        supportActionBar?.title = "Activity Detail"

        val act_id = intent.getStringExtra(homeview.FKACT_KEY)
        textView19.setText(act_id)

    }
}
