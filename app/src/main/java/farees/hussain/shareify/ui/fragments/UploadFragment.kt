package farees.hussain.shareify.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import farees.hussain.shareify.databinding.FragmentUploadBinding
import farees.hussain.shareify.ui.ShareifyViewModel
import kotlinx.android.synthetic.main.activity_main.*

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

        binding.buUpload.setOnClickListener {
            //todo -> insert item in database
            findNavController().navigate(UploadFragmentDirections.actionUploadFragmentToHistoryFragment())
            //todo -> before navigation check if succesful request and show a toast msg with a button to share
            //todo -> if there is a error in request then the snackbar with retry which shows the dialog box to upload again
        }



        return binding.root
    }
}