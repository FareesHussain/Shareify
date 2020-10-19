package farees.hussain.shareify.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import farees.hussain.shareify.R
import farees.hussain.shareify.databinding.FragmentUploadBinding
import farees.hussain.shareify.ui.ShareifyViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
        binding.viewModel = viewModel
        binding.tvFileName.text = viewModel.fileName.toString()
        binding.pbUploading.hide()

        binding.buUpload.setOnClickListener {
            //todo -> insert item in database
            binding.pbUploading.show()
            binding.pbUploading.show()
            viewModel.uploadFile()
            binding.buselectSomeOtherFile.hide()
            binding.buUpload.visibility = View.INVISIBLE
            //todo -> before navigation check if succesful request and show a toast msg with a button to share
            //todo -> if there is a error in request then the snackbar with retry which shows the dialog box to upload again
        }
        viewModel.curFileUrl.observe(viewLifecycleOwner, Observer {
//            findNavController().popBackStack()
            binding.buUpload.show()
            binding.buNext.show()
            binding.buselectSomeOtherFile.hide()
        })
        binding.buNext.setOnClickListener { findNavController().popBackStack() }


        viewModel.uploadProgresss.observe(viewLifecycleOwner, Observer {
            if(binding.pbUploading.isIndeterminate){
                binding.pbUploading.isIndeterminate = false
            }
            if(!!binding.pbUploading.isVisible) {
                binding.pbUploading.show()
            }
            binding.pbUploading.progress = it.toInt()
        })


        return binding.root
    }

    fun Button.hide(){
        this.visibility = View.GONE
    }
    fun Button.show(){
        this.visibility = View.VISIBLE
    }

}