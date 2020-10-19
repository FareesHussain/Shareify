package farees.hussain.shareify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import farees.hussain.shareify.adapter.ShareifyAdapter
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.databinding.FragmentHistoryBinding
import farees.hussain.shareify.databinding.FragmentSettingsBinding
import farees.hussain.shareify.ui.ShareifyViewModel
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

        val listofShareifyItems = mutableListOf<ShareifyItem>()
        listofShareifyItems.add(
            ShareifyItem(
            "filename.txt",
            "thisisfileurl",
            3,
            Calendar.getInstance().time,
            false
            )
        )
        listofShareifyItems.add(
            ShareifyItem(
            "filename.txt",
            "thisisfileurl",
            3,
            Calendar.getInstance().time,
            false
            )
        )
        listofShareifyItems.add(
            ShareifyItem(
            "filename.txt",
            "thisisfileurl",
            3,
            Calendar.getInstance().time,
            false
            )
        )



        val adapter = ShareifyAdapter(
            ShareifyAdapter.ShareifyItemClickListner {
                // onClick show copied to clipboard
                Toast.makeText(context, "clicked", Toast.LENGTH_LONG).show()
            }
        )
        viewModel.shareifyItems.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            binding.rvPreviousUploads.show()
            binding.tvNoFilesUploaded.hide()
        })
        adapter.submitList(listofShareifyItems)
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
