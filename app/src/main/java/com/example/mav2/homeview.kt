package com.example.mav2


import android.os.Bundle
import android.os.LocaleList
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.*
import com.example.mav2.`class`.fkactivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fkactivity_row_view.view.*
import kotlinx.android.synthetic.main.fragment_homeview.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class homeview : Fragment() {



    override fun onStart() {
        super.onStart()

        fetchFKactivity()

    }

    private fun fetchFKactivity(){
        val ref = FirebaseDatabase.getInstance().getReference("/Activity").orderByChild("activity_date")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()


                p0.children.forEach {
                    val fkact  = it.getValue(fkactivity::class.java)
                    if(fkact != null){
                        adapter.add(FKItem(fkact))
                    }

                }

                review_listActivity.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }


        })
    }

    class FKItem(val fkact : fkactivity): Item<GroupieViewHolder>(){
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.fkact_title_view.text = fkact.activity_title

            var formate = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            val tmpdate = fkact.activity_date
            viewHolder.itemView.fkact_date_view.text = formate.format(tmpdate)

            val time:String = fkact.activity_time_start + " - " + fkact.activity_time_end
            viewHolder.itemView.fkact_time_view.text = time

            Picasso.get().load(fkact.activity_imageUrl).into(viewHolder.itemView.fkact_image_view)
        }

        override fun getLayout(): Int {
            return R.layout.fkactivity_row_view
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homeview, container, false)
    }


}
