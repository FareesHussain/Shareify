package farees.hussain.shareify.ui.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import farees.hussain.shareify.R
import farees.hussain.shareify.databinding.FragmentUploadBinding
import farees.hussain.shareify.ui.SELECT_FILE_CODE
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
        binding.buNext.hide()
        binding.buUpload.show()

        binding.buUpload.setOnClickListener {
            //todo -> check if storage permission is accepted

            if(checkPermission()) {
                uploadFile()
                //todo -> before navigation check if succesful request and show a toast msg with a button to share
                //todo -> if there is a error in request then the snackbar with retry which shows the dialog box to upload again
            } else {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Accept The Storage Permission")
                    .setMessage("Select any one file to upload")
                    .setNegativeButton("Cancel"){ _, _->
                        Toast.makeText(context, "Can't Uplaod without storage permissions", Toast.LENGTH_SHORT).show()
                    }
                    .setPositiveButton("Accept"){ _, _->
                        askPermission()
                    }
                    .show()
            }
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

    fun checkPermission() : Boolean{
        val permission = ContextCompat.checkSelfPermission(requireContext(),
            Manifest.permission.READ_EXTERNAL_STORAGE)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    fun askPermission(){
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 7)
    }

    fun uploadFile(){
        binding.pbUploading.show()
        binding.pbUploading.show()
        viewModel.uploadFile()
        binding.buselectSomeOtherFile.hide()
        binding.buUpload.visibility = View.INVISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 7){
            if(grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context, "Can't Uplaod without storage permissions", Toast.LENGTH_SHORT).show()
            } else {
                uploadFile()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun Button.hide(){
        this.visibility = View.GONE
    }
    fun Button.show(){
        this.visibility = View.VISIBLE
    }

}