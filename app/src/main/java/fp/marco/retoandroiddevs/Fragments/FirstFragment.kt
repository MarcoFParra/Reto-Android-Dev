package fp.marco.retoandroiddevs.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fp.marco.retoandroiddevs.Model.Place
import fp.marco.retoandroiddevs.MyApp
import fp.marco.retoandroiddevs.PlacesAdapter
import fp.marco.retoandroiddevs.R
import fp.marco.retoandroiddevs.databinding.FragmentFirstBinding
import fp.marco.retoandroiddevs.databinding.FragmentMapsBinding
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding ? = null
    private val binding get() = _binding!!
    private lateinit var adapter: PlacesAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFirstBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {


            MyApp.instance.room.placeDao().getAll()
                .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            binding.viewSwitch.visibility = View.VISIBLE
                            binding.etSearch.isEnabled = true
                            adapter = PlacesAdapter(it)
                            binding.rvPlaces.layoutManager = LinearLayoutManager(context)
                            binding.rvPlaces.adapter = adapter

                            val placesLeftforVisit = it.count { !it.visited } //MyApp.instance.room.placeDao().getCountByStatus(false)
                            if  (placesLeftforVisit == 0)
                                binding.viewSwitch.showNext()
                                binding.tvVisitsLeft.setText("Tienes $placesLeftforVisit visitas en el dia")
                        }
                    }
                })
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                adapter.filter.filter(p0.toString())

            }

        })


    }

}