package farees.hussain.shareify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import farees.hussain.shareify.databinding.FragmentHistoryBinding
import farees.hussain.shareify.databinding.FragmentSettingsBinding


class FragmentHistory : Fragment(){
    private lateinit var binding : FragmentHistoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)




        return binding.root
    }
}
