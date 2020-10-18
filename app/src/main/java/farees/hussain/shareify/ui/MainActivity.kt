package farees.hussain.shareify.ui

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import farees.hussain.shareify.R
import farees.hussain.shareify.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

const val SELECT_FILE_CODE = 1

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    lateinit var viewModel: ShareifyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(ShareifyViewModel::class.java)
        binding.bottomNavView.setupWithNavController(navHostFragment.findNavController())

        navHostFragment.findNavController().addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){
                R.id.uploadFragment -> {
                    binding.bottomAppBar.visibility = View.INVISIBLE
                    binding.fab.visibility = View.INVISIBLE
                }
                else -> {
                    binding.bottomAppBar.visibility = View.VISIBLE
                    binding.fab.visibility = View.VISIBLE
                }
            }
        }


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
                    startActivityForResult(intent, SELECT_FILE_CODE)
                    //todo -> non-deprecated version for startActivityForResult
                }
                .show()
        }
        viewModel.curFileUrl.observe(this, Observer {
            val intent = Intent(Intent.ACTION_SEND).apply {
                setType("text/plain")
                putExtra(android.content.Intent.EXTRA_SUBJECT, "Shareify link share")
                putExtra(android.content.Intent.EXTRA_TEXT, "Shareify link share $it")
            }
            if (it.isNotEmpty()){
                val clipboard = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("text", it)
                clipboard.setPrimaryClip(clip)
            }
            val snackbar = Snackbar.make(
                findViewById(android.R.id.content),
                "File uploaded Successfully and Copied to Clipboard",
                Snackbar.LENGTH_LONG
            )
                .setAction("SHARE", View.OnClickListener {
                    startActivity(Intent.createChooser(intent, "Shareify link share"))
                })
            if (it.isNotEmpty()) {
                snackbar.show()
            }
            viewModel.setCurFileUrl("")
        })
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
                        result = result.substring(cut + 1)
                    }
                }
                viewModel.setCurFileUri(it)
                navHostFragment.findNavController().navigate(R.id.action_global_uploadingfragment)
//                Log.d("file", result)
            }
        }
    }
}