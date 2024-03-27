package com.example.firebasee

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecylerViewAdapter (private val dataTeman: ArrayList<data_teman>, context: Context) :
        RecyclerView.Adapter<RecylerViewAdapter.ViewHolder>() {
        private val context: Context

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val Nama: TextView
            val Alamat: TextView
            val NoHP: TextView
            val ListItem: LinearLayout

            init {
                Nama = itemView.findViewById(R.id.namax)
                Alamat = itemView.findViewById(R.id.alamatx)
                NoHP = itemView.findViewById(R.id.no_hpx)
                ListItem = itemView.findViewById(R.id.list_item)
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val V: View = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.view_design, parent, false)
            return  ViewHolder(V)
        }

        @SuppressLint("SetTextI18n")

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val Nama: String? = dataTeman.get(position).nama
            val Alamat: String? = dataTeman.get(position).alamat

            holder.Nama.text = "Nama: $Nama"
            holder.Alamat.text = "Alamat: $Alamat"
            holder.ListItem.setOnLongClickListener(
                object : View.OnLongClickListener {
                    override fun onLongClick(v: View?): Boolean {

                        return true
                    }
                })
        }

        override fun getItemCount(): Int {
            return  dataTeman.size
        }
        init {
            this.context = context
        }

    }
