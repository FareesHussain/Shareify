package farees.hussain.shareify

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import farees.hussain.shareify.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavView.apply {
            background = null
            menu.getItem(1).isEnabled = false
        }
    }
}