package com.example.firebasee

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.example.firebasee.databinding.ActivityMyListDataBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyListData : AppCompatActivity(), RecyclerViewAdaptor.dataListener {
    //deklarasi variabel for recyclerview
    private var recyclerView: RecyclerView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager?  = null

    //deklarasi variabel database reference & arraylist dgn parameter  class model
    val database = FirebaseDatabase.getInstance()
    private var dataTeman = ArrayList<data_teman>()
    private var auth: FirebaseAuth? = null

    private lateinit var binding: ActivityMyListDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerView = findViewById(R.id.datalist)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        supportActionBar!!.title = "Data Teman"
        auth= FirebaseAuth.getInstance()
        MyRecyclerView()
        GetData()
    }
    //kode untuk mengambil data dari database & show in Adapter
    private fun GetData(){
        Toast.makeText(applicationContext, "Sabarr kocakk", Toast.LENGTH_LONG).show()
        val getUserID : String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        getReference.child("Admin").child(getUserID).child("Firebasee")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()){
                        dataTeman.clear()
                        for (snapshot in dataSnapshot.children){
                            //mapping data pada DataSnapShot ke dalam objek dataTeman
                            val teman = snapshot.getValue(data_teman::class.java)
                            //pick primary key for proses update/delete
                            teman?.key = snapshot.key
                            dataTeman.add(teman!!)
                        }

                        //initiate adapter dan data teman dlm bentuk array
                        adapter = RecyclerViewAdaptor(dataTeman,this@MyListData)
                        //memasang adapter pd RecyclerView
                        recyclerView?.adapter = adapter
                        (adapter as RecyclerViewAdaptor).notifyDataSetChanged()
                        Toast.makeText(applicationContext,"Data oke", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //kode ketika error
                    Toast.makeText(applicationContext, "Data Ga masuk lur ", Toast.LENGTH_LONG).show()
                    Log.e("MyListActivity", databaseError.details + " " + databaseError.message)
                }
            })
    }

    //kode recyclerview
    private fun MyRecyclerView(){
        layoutManager = LinearLayoutManager(this)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.setHasFixedSize(true)

        //buat garis bawah tiap item data
        val itemDecoration = DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.line)!!)
        recyclerView?.addItemDecoration(itemDecoration)
    }

    override fun onDeleteData(data: data_teman?, position: Int){
        val getUserID: String = auth?.getCurrentUser()?.getUid().toString()
        val getReference = database.getReference()
        if (getReference != null) {
            getReference.child("Admin").child(getUserID).child("Firebasee").child(data?.key.toString())
                .removeValue()
                .addOnSuccessListener{
                    Toast.makeText(this@MyListData, "Data sudah dihapus", Toast.LENGTH_SHORT).show();
                }
        } else {
            Toast.makeText(this@MyListData, "Referance Kosong", Toast.LENGTH_SHORT).show();
        }
    }
}