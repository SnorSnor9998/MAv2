package com.example.mav2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.mav2.`class`.fkactivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fkpage.*
import java.text.SimpleDateFormat
import java.util.*

class fkactivity_page : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fkpage)

        supportActionBar?.title = "Activity Detail"

        var act_id = intent.getStringExtra(homeview.FKACT_KEY)

        if(act_id == null){
            act_id = intent.getStringExtra(addactivity.FKACT_KEY)
        }


        val ref = FirebaseDatabase.getInstance().getReference("/Activity").orderByChild("activity_id").equalTo(act_id)

        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val fkact  = it.getValue(fkactivity::class.java)
                    if(fkact != null){


                        textView12.setText(fkact.activity_title)
                        Picasso.get().load(fkact.activity_imageUrl).into(cna_butt_uploadphoto)
                        cna_time_from.setText(fkact.activity_time_start)
                        cna_time_to.setText(fkact.activity_time_end)

                        var formate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
                        val tmpdate = fkact.activity_date
                        cna_date.text = formate.format(tmpdate)

                        cna_address.setText(fkact.activity_address)

                        cna_desc.setText(fkact.activity_desc)

                        if(fkact.fkcat.dryfood)
                            cb_dryfood.isChecked = true
                        else
                            cb_dryfood.isVisible = false

                        if(fkact.fkcat.freshfood)
                            cb_fresh.isChecked = true
                        else
                            cb_fresh.isVisible = false

                        if(fkact.fkcat.frozenfood)
                            cb_frozen.isChecked = true
                        else
                            cb_frozen.isVisible = false

                        if(fkact.fkcat.fruitandvege)
                            cb_fandv.isChecked = true
                        else
                            cb_fandv.isVisible = false

                        if(fkact.fkcat.meat)
                            cb_meat.isChecked = true
                        else
                            cb_meat.isVisible = false

                        if(fkact.fkcat.refrige)
                            cb_refriger.isChecked = true
                        else
                            cb_refriger.isVisible = false




                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }

        })




    }
}
