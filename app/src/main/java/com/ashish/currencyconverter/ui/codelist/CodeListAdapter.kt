package com.ashish.currencyconverter.ui.codelist

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ashish.currencyconverter.databinding.CodeListItemViewBinding
import com.ashish.currencyconverter.ui.home.CurrencyClass


class CodeListAdapter(items: ArrayList<CurrencyClass>) :
    RecyclerView.Adapter<CodeListAdapter.ViewHolder>() {

    private var clickListener: ClickListener? = null
    private var items = ArrayList<CurrencyClass>()

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CodeListItemViewBinding.inflate(inflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size


    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

    inner class ViewHolder(private val binding: CodeListItemViewBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        fun bind(item: CurrencyClass) {
            binding.currencyObject = item
            binding.executePendingBindings()
            binding.root.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            if (clickListener != null) {
                clickListener!!.onItemClick(view, adapterPosition)
            }
        }
    }
}