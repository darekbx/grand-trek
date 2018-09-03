package com.grandtrek.ui.route

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.grandtrek.GrandTrekApplication
import com.grandtrek.R
import com.grandtrek.ui.routes.RoutesAdapter
import com.grandtrek.usecases.TripMap
import kotlinx.android.synthetic.main.fragment_trip.*
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import java.io.File
import javax.inject.Inject

class RouteFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    internal lateinit var viewModel: RouteViewModel

    var routeId = 0L
    val currentLocationOverlay = CustomPointsOverlay()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity?.application as GrandTrekApplication)?.appComponent.inject(this)

        initializeMap()

        context?.run {
            viewModel = ViewModelProviders.of(this@RouteFragment, viewModelFactory)[RouteViewModel::class.java]
            viewModel.fetchRoutePoints(routeId).observe(this@RouteFragment, Observer { points ->

                currentLocationOverlay.currentPosition = points?.first()
                currentLocationOverlay.points = points?.toMutableList()
                map_view.overlays.add(currentLocationOverlay)
                map_view.invalidate()

                points?.first()?.let { point ->

                    with(map_view.controller) {
                        setZoom(18.0)
                        setCenter(GeoPoint(point.latitudePoint, point.longitudePoint))
                    }
                }
            })
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
            overlays.add(currentLocationOverlay)
        }
    }
}