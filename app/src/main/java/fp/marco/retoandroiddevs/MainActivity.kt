package fp.marco.retoandroiddevs

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

            MyApp.instance.room.placeDao().getAll().observe(this@MainActivity, androidx.lifecycle.Observer {
                if (it != null) {
                    if (it.isEmpty()) {
                        mainViewModel.getPlaces()
                    }
                }
            })
        }
        mainViewModel.places.observe(this, androidx.lifecycle.Observer {
            if (it != null) {
                if (it.isNotEmpty())
                {
                    lifecycleScope.launch {
                        MyApp.instance.room.placeDao().insert(it)
                    }
                }

            }
        })

        mainViewModel.connectionError.observe(this, Observer {
            if (it)
            {
                 AlertDialog.Builder(this)
                    .setTitle("Error de conexion")
                    .setMessage("Ocurrio un error en la conexión, verifica tu conexion e intentalo mas tarde") // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.ok,
                        DialogInterface.OnClickListener { dialog, which ->
                            dialog.dismiss()
                        }) // A null listener allows the button to dismiss the dialog and take no further action.
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show()

            }
        })

        setContentView(R.layout.activity_main)

    }

}