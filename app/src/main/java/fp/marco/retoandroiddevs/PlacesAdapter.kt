package fp.marco.retoandroiddevs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import fp.marco.retoandroiddevs.Model.Place
import fp.marco.retoandroiddevs.databinding.LayoutItemPlaceBinding
import java.util.*


class PlacesAdapter(private val places: List<Place>): RecyclerView.Adapter<PlaceViewHolder>(),
    Filterable {

    private var placeFilterList: List<Place>
    init {
        placeFilterList = places
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PlaceViewHolder(parent.context,layoutInflater.inflate(R.layout.layout_item_place,parent,false))
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val item = placeFilterList[position]
        holder.bind(item);
    }

    override fun getItemCount(): Int = placeFilterList.size

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    placeFilterList = places
                } else {
                    val resultList = ArrayList<Place>()
                    for (row in places) {
                        if (row.streetName?.lowercase(Locale.ROOT)?.contains(charSearch.lowercase(Locale.ROOT)) == true) {
                            resultList.add(row)
                        }
                    }
                    placeFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = placeFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                placeFilterList = results?.values as ArrayList<Place>
                notifyDataSetChanged()
            }

        }
    }

}

class PlaceViewHolder(private var context: Context, view: View):RecyclerView.ViewHolder(view){

    private var binding =  LayoutItemPlaceBinding.bind(view)
    fun bind (place: Place)
   {
       binding.tvStreetName.setText(place.streetName)
       binding.tvSuburb.setText(place.suburb)
       if (place.visited) {
           binding.cvDetail.setCardBackgroundColor(context.getColor(R.color.text_highlight))
           binding.tvStatus.setText(R.string.label_visited)
           binding.tvStatus.setTextColor(context.getColor(R.color.text_highlight))
       }
       else {
           binding.cvDetail.setCardBackgroundColor(context.getColor(R.color.text_extra_light_gray))
           binding.tvStatus.setTextColor(context.getColor(R.color.text_extra_light_gray))
           binding.tvStatus.setText(R.string.label_no_visited)
       }

       binding.root.setOnClickListener {
           val bundle = Bundle()
           bundle.putParcelable("place",place)
           Navigation.findNavController(binding.root).navigate(R.id.action_FirstFragment_to_SecondFragment,bundle)

       }
   }
}