package com.grandtrek.ui.routes

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.grandtrek.data.model.Route
import com.grandtrek.databinding.AdapterRouteBinding

class RoutesAdapter(val context: Context,
                    val onItemClick: (route: Route) -> Unit,
                    val onLongItemClick: (route: Route) -> Unit)
    : RecyclerView.Adapter<RoutesAdapter.ViewHolder>() {

    var routes = listOf<Route>()

    fun swapData(routes: List<Route>) {
        this.routes = routes
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val binding = AdapterRouteBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return routes.size
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val part = routes.get(position)
        viewHolder.bind(part, onItemClick, onLongItemClick)
    }

    val inflater by lazy { LayoutInflater.from(context) }

    class ViewHolder(val binding: AdapterRouteBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(route: Route,
                 onItemClick: (route: Route) -> Unit,
                 onLongItemClick: (route: Route) -> Unit) {
            binding.route = route
            with(binding) {
                root.setOnClickListener { onItemClick(route) }
                root.setOnLongClickListener {
                    onLongItemClick(route)
                    true
                }
                executePendingBindings()
            }
        }
    }
}