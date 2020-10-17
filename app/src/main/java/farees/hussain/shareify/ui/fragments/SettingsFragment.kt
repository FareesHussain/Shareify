package farees.hussain.shareify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import farees.hussain.shareify.databinding.FragmentSettingsBinding
import farees.hussain.shareify.ui.ShareifyViewModel


class SettingsFragment : Fragment(){
    private lateinit var binding : FragmentSettingsBinding
    lateinit var viewModel : ShareifyViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(ShareifyViewModel::class.java)


        binding.buDeleteHistory.setOnClickListener {
            //todo -> dialog box with conformation to delelte or not
        }



        return binding.root
    }
}
