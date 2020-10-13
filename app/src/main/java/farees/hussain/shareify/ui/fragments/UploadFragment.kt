package farees.hussain.shareify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import farees.hussain.shareify.databinding.FragmentUploadBinding
import farees.hussain.shareify.ui.ShareifyViewModel

class UploadFragment : Fragment(){

    private lateinit var binding : FragmentUploadBinding
    lateinit var viewModel : ShareifyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(ShareifyViewModel::class.java)




        return binding.root
    }
}