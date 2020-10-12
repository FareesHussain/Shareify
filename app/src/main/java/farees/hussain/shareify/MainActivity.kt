package farees.hussain.shareify

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import farees.hussain.shareify.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavView.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }
        Log.d("darkmode", "${resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK}")
        if(resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == 16){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}