package com.example.mav2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.example.mav2.`class`.fkVolunteer
import com.example.mav2.`class`.fkactivity
import com.example.mav2.`class`.user
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_fkpage.*
import java.text.SimpleDateFormat
import java.util.*
import org.jetbrains.anko.toast


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


                        //if(!fkact.volunteer || fkact.creator_id.equals(FirebaseAuth.getInstance().uid ?:""))
                        if(!fkact.volunteer){
                           cna_button.isVisible = false
                        }else{

                            var userid = FirebaseAuth.getInstance().uid ?: ""
                            val voluref = FirebaseDatabase.getInstance().getReference("/Volunteer").orderByChild("activityid").equalTo(fkact.activity_id)
                            val dbup = FirebaseDatabase.getInstance().getReference("/Volunteer/${fkact.activity_id}")
                            voluref.addListenerForSingleValueEvent(object : ValueEventListener{

                                override fun onDataChange(p0: DataSnapshot) {
                                    p0.children.forEach{
                                        val fkvolu = it.getValue(fkVolunteer::class.java)
                                        if(fkvolu != null){
                                            val tmpstr : String = "("+fkvolu.space+"/"+fkvolu.size_of_volunteer+") Join Volunteer"
                                            cna_button.setText(tmpstr)


                                            val i =0
                                            var found : Boolean = false

                                            fkvolu.userlist.forEach{
                                                if(it.contentEquals(userid))
                                                    found = true
                                            }


                                            if(found){
                                                cna_button.setText("Not Going")
                                                cna_button.setOnClickListener {

                                                    fkvolu.userlist.remove(userid)
                                                    fkvolu.space--

                                                    dbup.child("userlist")
                                                        .setValue(fkvolu.userlist)
                                                    dbup.child("space").setValue(fkvolu.space)

                                                    finish()
                                                    startActivity(getIntent())

                                                }
                                            }else {
                                                    cna_button.setOnClickListener {
                                                        if (fkvolu.space < fkvolu.size_of_volunteer) {
                                                        fkvolu.userlist.add(userid)
                                                        fkvolu.space++

                                                        val tmpstr: String =
                                                            "(" + fkvolu.space + "/" + fkvolu.size_of_volunteer + ") Join Volunteer"
                                                        cna_button.setText(tmpstr)

                                                        dbup.child("userlist")
                                                            .setValue(fkvolu.userlist)
                                                        dbup.child("space").setValue(fkvolu.space)
                                                            finish()
                                                            startActivity(getIntent())
                                                        } else {
                                                            toast("It's full")
                                                        }
                                                    }
                                            }
                                        }
                                    }
                                }
                                override fun onCancelled(p0: DatabaseError) {}
                            })
                        }
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }



}
