package com.ashish.currencyconverter.ui.codelist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ashish.currencyconverter.R
import com.ashish.currencyconverter.ui.home.CurrencyClass


class CodeListAdapter(items: ArrayList<CurrencyClass>) : RecyclerView.Adapter<CodeListAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null
    private var items = ArrayList<CurrencyClass>()

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.code_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // display stuff on view item
        var codeModel=items.get(position)
        holder.tvCodeTitle?.text = codeModel.code +" - "+ codeModel.name
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

    inner class ViewHolder
    // declare other views

        (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
         var tvCodeTitle: TextView? = null

        init {
            tvCodeTitle = itemView.findViewById(R.id.tvCodeTitle) as TextView
            itemView.setOnClickListener(this)

        }

        override fun onClick(view: View) {
            if (clickListener != null) {
                clickListener!!.onItemClick(view, adapterPosition)
            }
        }
    }
}