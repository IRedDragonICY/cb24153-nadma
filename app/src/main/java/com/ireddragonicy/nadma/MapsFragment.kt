package com.ireddragonicy.nadma

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ireddragonicy.nadma.databinding.FragmentMapsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.ScaleBarOverlay
import org.osmdroid.views.overlay.compass.CompassOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.IOException

class MapsFragment : Fragment(R.layout.fragment_maps) {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMapsBinding.bind(view)

        Configuration.getInstance().load(context, context?.getSharedPreferences("osmdroid", 0))

        val mapView = binding.mapView
        mapView.apply {
            setMultiTouchControls(true)
            controller.setZoom(15.0)
            controller.setCenter(GeoPoint(3.54306, 103.43611))

            overlays.apply {
                add(MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
                    enableMyLocation()
                })
                add(CompassOverlay(context, mapView).apply {
                    enableCompass()
                })
                add(ScaleBarOverlay(mapView).apply {
                    setAlignRight(true)
                })
            }
        }

        fetchData()

        binding.headerMaps.iconSettings.setOnClickListener {}
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchData() {
        lifecycleScope.launch(Dispatchers.IO) {
            val dataMap = fetchNadmaData()
            withContext(Dispatchers.Main) {
                updateUI(dataMap)
            }
        }
    }

    private fun fetchNadmaData(): Map<String, String> {
        val dataMap = mutableMapOf<String, String>()
        try {
            val doc = Jsoup.connect("https://portalbencana.nadma.gov.my/map/map_main.php").get()
            val boxItems = doc.select("div.box-item")

            fun getText(index: Int): String =
                boxItems.getOrNull(index)?.selectFirst("div.box-value")?.text()?.takeUnless { it.isEmpty() } ?: "0"

            dataMap["kebakaran"] = getText(0)
            dataMap["banjir"] = getText(1)

            boxItems.getOrNull(2)?.selectFirst("div.box-value")?.text()?.split("|")?.map { it.trim() }?.let {
                dataMap["negeri"] = it.getOrNull(0) ?: "0"
                dataMap["daerah"] = it.getOrNull(1) ?: "0"
            }

            dataMap["keluarga"] = getText(3)
            dataMap["mangsa"] = getText(4)
            dataMap["pps_aktif"] = getText(5)
            dataMap["kematian"] = getText(6)

        } catch (e: IOException) {
            Log.e("JsoupExample", "Error fetching data", e)
        }
        return dataMap
    }

    private fun updateUI(result: Map<String, String>) {
        binding.headerMaps.apply {
            numberFire.text = result["kebakaran"]
            numberFlood.text = result["banjir"]
            numberState.text = result["negeri"]
            numberDistrict.text = result["daerah"]
            numberFamily.text = result["keluarga"]
            numberInjuries.text = result["mangsa"]
            numberPpsActive.text = result["pps_aktif"]
            numberFatality.text = result["kematian"]
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }
}