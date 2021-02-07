package com.kehnestudio.procrastinator_proccy.ui.backdrop.myaccount

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.FragmentMyaccountBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_mainactivity.*

@AndroidEntryPoint
class MyAccountFragment : Fragment(R.layout.fragment_myaccount) {

    private lateinit var mAuth: FirebaseAuth
    private var _binding: FragmentMyaccountBinding? = null

    private val binding get()= _binding!!

    private val viewModel: MyAccountViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyaccountBinding.inflate(inflater, container, false)

        //binding.root is property on all automatic generated viewbinding classes. Root return whole layout
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        viewModel.saveDataIntoFirestore()

        binding.btnLogout.setOnClickListener {
            logout()

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun logout(){

        viewModel.deleteAll()
        val mGoogleSignInClient: GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity())
        {
            FirebaseAuth.getInstance().signOut() //signout firebase
            nav_host_fragment.findNavController().navigate(R.id.fragment_login)
            Toast.makeText(requireActivity(), "Logged Out", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //clears reference to binding, view is cleaned up in memory
        _binding = null
    }

}