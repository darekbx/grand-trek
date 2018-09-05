package com.grandtrek.ui.route

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import com.grandtrek.data.model.Point
import com.grandtrek.data.model.Route
import com.grandtrek.databinding.FragmentRouteBinding
import com.grandtrek.usecases.TripMap
import kotlinx.android.synthetic.main.fragment_route.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import java.io.File
import javax.inject.Inject

class RouteFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: RouteViewModel

    private lateinit var fragmentRouteBinding: FragmentRouteBinding

    var routeId = 0L
    private val overlay = RouteMapOverlay()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentRouteBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_route, container, false)
        return fragmentRouteBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as GrandTrekApplication)?.appComponent.inject(this)

        initializeMap()

        context?.run {
            viewModel = ViewModelProviders.of(this@RouteFragment, viewModelFactory)[RouteViewModel::class.java]
            with(viewModel) {
                fetchRoute(routeId).observe(this@RouteFragment, Observer { applyRoute(it) })
                fetchRoutePoints(routeId).observe(this@RouteFragment, Observer { applyPoints(it) })
            }
        }
    }

    private fun applyPoints(points: List<Point>?) {
        points?.let { points ->
            overlay.points = points.toMutableList()
            points.first()?.let { point ->
                with(map_view.controller) {
                    setZoom(18.0)
                    setCenter(GeoPoint(point.latitudePoint, point.longitudePoint))
                }
            }
            diplayChart(points)
        }
    }

    private fun diplayChart(points: List<Point>) {
        altitude_chart.altitudes.addAll(points.map { it.altitude.toFloat() })
        altitude_chart.invalidate()
        speed_chart.speeds.addAll(points.map { it.speed })
        speed_chart.invalidate()
    }

    private fun applyRoute(route: Route?) {
        route?.let { route ->
            fragmentRouteBinding.route = route
            fragmentRouteBinding.executePendingBindings()

            overlay.updateColor(route.color)
            map_view.invalidate()
        }
    }

    private fun initializeMap() {
        with(context) {
            Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
            Configuration.getInstance().osmdroidBasePath = File(TripMap.OSM_BASE_PATH)
        }

        with(map_view) {
            setTileSource(TileSourceFactory.MAPNIK)
            setBuiltInZoomControls(true)
            setMultiTouchControls(true)
            setMaxZoomLevel(TripMap.MAX_ZOOM)
            overlays.add(this@RouteFragment.overlay)
        }
    }
}