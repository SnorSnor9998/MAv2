package com.example.mav2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_register.*
import com.google.firebase.auth.FirebaseAuth
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.text.TextUtils
import android.widget.Toast
import org.jetbrains.anko.toast


class RegisterActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        cb_male.isChecked = true
        cb_male.setOnClickListener{
            cb_female.isChecked=false
        }
        cb_female.setOnClickListener{
            cb_male.isChecked = false
        }


        text_gologinpage.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)

        }

        button_register.setOnClickListener {

//            val username = tf_username.text.toString()
            val email = tf_email.text.toString()
            val pass = tf_password.text.toString()
//            val phnum = tf_phnum.text.toString()
//            var gender : Char? = null
//
//            if(cb_male.isChecked){
//                gender = 'M'
//            }else if(cb_female.isChecked){
//                gender='F'
//            }


            if(TextUtils.isEmpty(email)){
                tf_email.error = "Email is Require"
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(pass)){
                tf_password.error = "Password is Require"
                return@setOnClickListener
            }



            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                if(!it.isSuccessful){
                    return@addOnCompleteListener
                }else{
                    toast("User Created Successfully")

                    val intent = Intent(this,MainActivity::class.java)
                    finish()
                    startActivity(intent)

                }


            }

        }



    }




}
