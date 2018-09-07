package com.grandtrek.ui.routes

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import com.grandtrek.data.model.Route
import com.grandtrek.ui.route.RouteFragment
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
                routes?.let { fillData(it) }
            })
        }
    }

    private fun fillData(it: List<Route>) {
        context?.run {
            routesAdapter = RoutesAdapter(
                    this,
                    { route -> openRoute(route) },
                    { route -> askDeleteRoute(route) })
            routes_list.adapter = routesAdapter
            routesAdapter.swapData(it)
        }
    }

    private fun openRoute(route: Route) {
        route.id?.let { routeId ->
            activity?.supportFragmentManager?.run {
                beginTransaction()
                        .replace(R.id.container,  RouteFragment().apply { this.routeId = routeId })
                        .addToBackStack(null)
                        .commit()
            }
        }
    }

    private fun askDeleteRoute(route: Route) {
        context?.run {
            route.id?.let { routeId ->
                val message = Html.fromHtml(getString(R.string.delete_confirm, route.name), Html.FROM_HTML_MODE_COMPACT)
                AlertDialog
                        .Builder(this)
                        .setMessage(message)
                        .setPositiveButton(R.string.delete_yes, { a, b -> deleteRoute(routeId) })
                        .setNegativeButton(R.string.delete_no, null)
                        .show()
            }
        }
    }

    private fun deleteRoute(routeId: Long) {
        viewModel.deleteRoute(routeId)
    }
}