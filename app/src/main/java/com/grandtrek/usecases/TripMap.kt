package com.grandtrek.usecases

import org.osmdroid.tileprovider.tilesource.XYTileSource

class TripMap {

    companion object {
        val OSM_BASE_PATH = "/storage/emulated/0/osmdroid"
        val DEFAULT_ZOOM = 17.0
        val MAX_ZOOM = 17.0
    }

    fun offlineMapSource() = XYTileSource("Mapnik", 1, 17, 256, ".jpg", arrayOf())
}