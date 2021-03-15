package com.example.pos.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat.canScrollVertically
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pos.R
import com.example.pos.adapter.FunctionItem
import com.example.pos.utils.CustomGridLayoutManager


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        homeViewModel.funcListData.observe(viewLifecycleOwner, Observer {
            setupRecyclerView(root.findViewById(R.id.item_list), it)
        })

        return root
    }
    private fun setupRecyclerView(recyclerView: RecyclerView, data: MutableList<FunctionItem>) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, data)
        recyclerView.isNestedScrollingEnabled=false
        recyclerView.layoutManager = CustomGridLayoutManager(context, 3)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentFragment: HomeFragment,
        private val values: List<FunctionItem>
    ) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                Toast.makeText(parentFragment.context, "click", Toast.LENGTH_SHORT).show()
//                val item = v.tag as FunctionItem
//                    val fragment = ItemDetailFragment().apply {
//                        arguments = Bundle().apply {
//                            putInt(ItemDetailFragment.ARG_ITEM_ID, item.id)
//                        }
//                    }
//                    parentActivity.activity?.supportFragmentManager
//                            .beginTransaction()
//                            .replace(R.id.item_detail_container, fragment)
//                            .commit()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_func_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.name.text = item.name
            holder.icon.setImageResource(item.icon)
            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val icon: ImageView = view.findViewById(R.id.func_icon)
            val name: TextView = view.findViewById(R.id.func_name)
        }
    }

}