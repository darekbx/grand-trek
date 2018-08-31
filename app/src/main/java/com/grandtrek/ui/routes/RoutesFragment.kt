package com.grandtrek.ui.routes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import kotlinx.android.synthetic.main.fragment_routes.*
import javax.inject.Inject

class RoutesFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: RoutesViewModel

    private lateinit var routesAdapter: RoutesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_routes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as GrandTrekApplication)?.appComponent.inject(this)

        context?.run {

            routes_list.layoutManager = LinearLayoutManager(this)

            viewModel = ViewModelProviders.of(this@RoutesFragment, viewModelFactory)[RoutesViewModel::class.java]
            viewModel.fetchRoutes().observe(this@RoutesFragment, Observer { routes ->
                routes?.let {

                    routesAdapter = RoutesAdapter(this, { route -> })
                    routes_list.adapter = routesAdapter
                    routesAdapter.swapData(it)
                }
            })
        }
    }
}