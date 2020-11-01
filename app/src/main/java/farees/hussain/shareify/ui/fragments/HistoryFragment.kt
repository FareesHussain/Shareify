package farees.hussain.shareify.ui.fragments

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import farees.hussain.shareify.adapter.ShareifyAdapter
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.databinding.FragmentHistoryBinding
import farees.hussain.shareify.databinding.FragmentSettingsBinding
import farees.hussain.shareify.ui.ShareifyViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


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
                val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip: ClipData = ClipData.newPlainText("text", it.fileUrl)
                clipboard.setPrimaryClip(clip)
                Snackbar.make(binding.root, "Copied to Clipboard", Snackbar.LENGTH_LONG)
                    .show()
            }
        )
        viewModel.shareifyItems.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            binding.rvPreviousUploads.show()
            binding.tvNoFilesUploaded.hide()
        })
        binding.rvPreviousUploads.adapter = adapter


        return binding.root
    }

    fun View.hide(){
        this.visibility = View.GONE
    }
    fun View.show(){
        this.visibility = View.VISIBLE
    }
}
