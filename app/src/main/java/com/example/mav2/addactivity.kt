package com.example.mav2


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_addactivity.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class addactivity : Fragment() {

    var formate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.US)

    override fun onStart() {
        super.onStart()

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



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_addactivity, container, false)
    }


}
