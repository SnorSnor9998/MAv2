package com.example.mav2


import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.mav2.`class`.fkactivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_addactivity.*
import java.text.SimpleDateFormat
import java.util.*
import android.content.ContentResolver as ContentResolver1

/**
 * A simple [Fragment] subclass.
 */
class addactivity : Fragment() {

    var formate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    var selectedPhotoUri : Uri? = null

    private var fkact = fkactivity()


    override fun onStart() {
        super.onStart()


        cna_butt_uploadphoto.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent,0)

        }



        cna_time_from.setOnClickListener{
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(this.requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                val time = timeFormat.format(selectedTime.time)
                cna_time_from.setText(time)
            },
                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
            timePicker.show()
        }

        cna_time_to.setOnClickListener {
            val now = Calendar.getInstance()
            val timePicker = TimePickerDialog(this.requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                val time = timeFormat.format(selectedTime.time)
                cna_time_to.setText(time)
            },
                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
            timePicker.show()
        }




        cna_date.setOnClickListener {
            val now = Calendar.getInstance()
            val selectedDate = Calendar.getInstance()
            val datePicker = DatePickerDialog(this.requireContext(), DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->

                selectedDate.set(Calendar.YEAR,year)
                selectedDate.set(Calendar.MONTH,month)
                selectedDate.set(Calendar.DAY_OF_MONTH,dayOfMonth)

                val date = formate.format(selectedDate.time)
                cna_date.setText(date.toString())
            },
                    now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        }


        cna_button.setOnClickListener {



            var validate : Boolean = true

            if(cna_date.text.toString().equals("Date Picker")||cna_date.text.toString().equals("Date is required")){
                cna_date.setText("Date is required")
                textView22.setTextColor(Color.RED)
                validate = false
            }

            if(cna_time_from.text.toString().equals("Start")||cna_time_from.text.toString().equals("Required")){
                cna_time_from.setText(R.string.validate_required)
                textView15.setTextColor(Color.RED)
                validate = false
            }

            if(cna_time_to.text.toString().equals("End")||cna_time_to.text.toString().equals("Required")){
                cna_time_to.setText(R.string.validate_required)
                textView15.setTextColor(Color.RED)
                validate = false
            }

            if(cna_title.text.isEmpty()){
                cna_title.setHint(R.string.validate_required)
                textView14.setTextColor(Color.RED)
                validate = false
            }

            if(cna_address.text.isEmpty()){
                cna_address.setHint(R.string.validate_required)
                textView16.setTextColor(Color.RED)
                validate = false
            }

            if(cna_desc.text.isEmpty()){
                cna_desc.setHint(R.string.validate_required)
                textView17.setTextColor(Color.RED)
                validate = false
            }

            if(!(cb_dryfood.isChecked||cb_fandv.isChecked||cb_fresh.isChecked||cb_frozen.isChecked||cb_meat.isChecked||cb_refriger.isChecked)){
                tv_typefooderror.setText("(Pick At Least One)")
                tv_typefooderror.setTextColor(Color.RED)
                tv_typefooderror.isVisible = true
                textView18.setTextColor(Color.RED)
                validate = false
            }

            if(cna_butt_uploadphoto.text.toString().equals("Upload A Photo")){
                cna_butt_uploadphoto.setTextColor(Color.RED)
                validate = false
            }


            fkact.activity_title = cna_title.text.toString()
            fkact.activity_time_start = cna_time_from.text.toString()
            fkact.activity_time_end = cna_time_to.text.toString()
            fkact.activity_date = cna_date.text.toString()
            fkact.activity_address = cna_address.text.toString()
            fkact.activity_desc = cna_desc.text.toString()
            fkact.creator_id = FirebaseAuth.getInstance().uid.toString()



            if(validate == true){
                uploadImageToFirebase()
                Toast.makeText(this.requireContext(),"Activity is created",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebase (){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!).addOnSuccessListener {
            ref.downloadUrl.addOnSuccessListener {

                createFKActivity(it.toString())
                //fkact.activity_imageUrl = it.toString()
            }
        }


    }
    private fun createFKActivity(imageUrl : String){
        val filename = UUID.randomUUID().toString()
        val dbact = FirebaseDatabase.getInstance().getReference("Activity/$filename")

        fkact.activity_imageUrl = imageUrl
        fkact.activity_id = filename

        dbact.setValue(fkact)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver,selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            cna_butt_uploadphoto.setText("")
            cna_butt_uploadphoto.setBackgroundDrawable(bitmapDrawable)

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addactivity, container, false)
    }


}
