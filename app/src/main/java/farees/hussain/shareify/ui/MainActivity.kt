package farees.hussain.shareify.ui

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import farees.hussain.shareify.databinding.ActivityMainBinding
import timber.log.Timber
import java.util.*

const val SELECT_FILE_CODE = 1
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: ShareifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ShareifyViewModel::class.java)


        binding.bottomNavView.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }
        Log.d("darkmode", "${resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK}")
        if(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == 16){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        Timber.d("bhai dekh le yaar")
        binding.fab.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle("Upload File")
                .setMessage("Select any one file to upload")
                .setNegativeButton("Cancel"){ _, _->
                    Toast.makeText(this, "cancel", Toast.LENGTH_SHORT).show()
                }
                .setPositiveButton("Upload"){ _, _->
                    val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        setType("*/*")
                    }
//                    startActivityForResult(intent, SELECT_FILE_CODE)


                }
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode ==  SELECT_FILE_CODE && resultCode == Activity.RESULT_OK) {
            data?.data?.let{
                var result = ""
                if (it.scheme.equals("content")){
                    val cursor = contentResolver.query(it, null, null, null, null)
                    try {
                        if(cursor!=null && cursor.moveToFirst()){
                            result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        }
                    } finally {
                        cursor?.close()
                    }
                }
                if(result==""){
                    result = it.path?:"nothing"
                    var cut = result.lastIndexOf("/")
                    if(cut!=-1){
                        result = result.substring(cut+1)
                    }
                }
                viewModel.setCurFileUri(it)
//                Log.d("file", result)
            }
        }
    }
}