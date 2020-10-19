package farees.hussain.shareify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import farees.hussain.shareify.adapter.ShareifyAdapter
import farees.hussain.shareify.databinding.FragmentHistoryBinding
import farees.hussain.shareify.databinding.FragmentSettingsBinding
import farees.hussain.shareify.ui.ShareifyViewModel


class HistoryFragment : Fragment(){
    private lateinit var binding : FragmentHistoryBinding
    lateinit var viewModel : ShareifyViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(ShareifyViewModel::class.java)

        val adapter = ShareifyAdapter(
            ShareifyAdapter.ShareifyItemClickListner {
                // onClick show copied to clipboard
            }
        )
        binding.rvPreviousUploads.adapter = adapter



        return binding.root
    }
}
