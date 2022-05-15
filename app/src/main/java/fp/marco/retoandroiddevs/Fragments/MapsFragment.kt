package fp.marco.retoandroiddevs.Fragments

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fp.marco.retoandroiddevs.Model.Place
import fp.marco.retoandroiddevs.MyApp
import fp.marco.retoandroiddevs.R
import fp.marco.retoandroiddevs.databinding.FragmentMapsBinding
import kotlinx.coroutines.launch


class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var place : Place? = null

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }


    private val callback = OnMapReadyCallback { googleMap ->
        val location = LatLng(place!!.location!!.latitude,place!!.location!!.longitude!!)
        val marker = MarkerOptions().position(location).title(place!!.streetName).
        icon(bitmapDescriptorFromVector(requireContext(), when (place!!.visited) { true -> R.drawable.ic_visited_marker false-> R.drawable.ic_marker} ))
        googleMap.addMarker(marker)
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 12.0f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMapsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        val args = arguments
        if (args != null) {
            place = args.getParcelable<Place>("place")

            binding.tvStreetName.setText(place!!.streetName)
            binding.tvSuburb.setText(place!!.suburb)
            if (place!!.visited) {
                binding.cvDetail.setCardBackgroundColor(requireActivity().getColor(R.color.text_highlight))
                binding.tvStatus.setText(R.string.label_visited)
                binding.tvStatus.setTextColor(requireActivity().getColor(R.color.text_highlight))
            }
            else {
                binding.tvStatus.setText(R.string.label_no_visited)
            }

            binding.btnVisit.setOnClickListener{
                viewLifecycleOwner.lifecycleScope.launch {
                    MyApp.instance.room.placeDao().setAsVisited(place!!.id)
                }
                findNavController(this).popBackStack()

            }
            if (place!!.visited)
                binding.viewMenuBtn.visibility = View.GONE

            binding.btnNavigate.setOnClickListener {
                val uri: Uri = Uri.parse("geo:" + place!!.location!!.latitude.toString() + "," + place!!.location!!.longitude.toString())
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)

            }
        }
    }
}