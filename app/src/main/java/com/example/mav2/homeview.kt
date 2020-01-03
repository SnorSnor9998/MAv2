package com.example.mav2


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import com.example.mav2.`class`.fkactivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
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

       // tf_search.doAfterTextChanged { serachFk(tf_search.text.toString()) }
        tf_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                serachFk(tf_search.text.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                serachFk(tf_search.text.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                serachFk(tf_search.text.toString())
            }
        })
        fetchFKactivity()

    }

    companion object{
        val FKACT_KEY = ""
    }

    private fun fetchFKactivity(){
        val ref = FirebaseDatabase.getInstance().getReference("/Activity").orderByChild("activity_date/time")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()


                p0.children.forEach {
                    val fkact  = it.getValue(fkactivity::class.java)
                    if(fkact != null){
                        adapter.add(FKItem(fkact))
                    }

                }
                
                adapter.setOnItemClickListener{ item, view ->
                    val fkactItem = item as FKItem
                    val intent = Intent(view.context,fkactivity_page::class.java)
                    intent.putExtra(FKACT_KEY,fkactItem.fkact.activity_id)
                    startActivity(intent)
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

    private fun serachFk(search : String){
        val ref = FirebaseDatabase.getInstance().getReference("/Activity").orderByChild("activity_date/time")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<GroupieViewHolder>()
                adapter.clear()


                p0.children.forEach {
                    val fkact  = it.getValue(fkactivity::class.java)
                    if(fkact != null){

                            if(fkact.activity_title.toLowerCase().contains(search.toLowerCase()))
                                adapter.add(FKItem(fkact))


                    }

                }

                adapter.setOnItemClickListener{ item, view ->
                    val fkactItem = item as FKItem
                    val intent = Intent(view.context,fkactivity_page::class.java)
                    intent.putExtra(FKACT_KEY,fkactItem.fkact.activity_id)
                    startActivity(intent)
                }
                review_listActivity.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
            }
        })


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_homeview, container, false)
    }


}
