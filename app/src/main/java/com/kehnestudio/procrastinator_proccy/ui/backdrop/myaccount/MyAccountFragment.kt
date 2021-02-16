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
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.kehnestudio.procrastinator_proccy.Constants.PERIODIC_WORK_UPLOAD_DATA
import com.kehnestudio.procrastinator_proccy.R
import com.kehnestudio.procrastinator_proccy.databinding.FragmentMyaccountBinding
import com.kehnestudio.procrastinator_proccy.utilities.Variables
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_mainactivity.*
import timber.log.Timber


@AndroidEntryPoint
class MyAccountFragment : Fragment(R.layout.fragment_myaccount) {
    
    private var _binding: FragmentMyaccountBinding? = null

    private val binding get() = _binding!!

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

        readLastSynchronisedDateFromDataStore()

        binding.btnDeleteAccount.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Account?")
                .setMessage("Deleting your account can not be reverted. Are you sure?")
                .setNegativeButton("Cancel"){ _, _ -> Timber.d("Alert - Hide")
                }
                .setPositiveButton("Delete"){ _, _ -> deleteAccount()
                }.show()
        }

        binding.btnLogout.setOnClickListener {

            if(Variables.isNetworkConnected){
                viewModel.saveDataOnLogout(requireContext())
                viewModel.result.observe(viewLifecycleOwner, Observer {
                    when (it) {
                        true -> {
                            logout()
                            viewModel.resetResult()
                        }
                        false -> Toast.makeText(
                            requireContext(),
                            "Failed to logout",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
            } else {
                Toast.makeText(
                    requireContext(),
                    "Failed to logout. No Internet connection. Please, try again later.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteAccount(){

        if (Variables.isNetworkConnected){
            cancelPeriodicWorkRequestUploadData()
            viewModel.deleteAccount(requireContext())
            navigateToLogin("Deleted Account")
        }
        else {
            Toast.makeText(
                requireContext(),
                "Failed to delete account. No Internet connection. Please, try again later",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun logout() {
        val mGoogleSignInClient: GoogleSignInClient
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity()){
            FirebaseAuth.getInstance().signOut()
            cancelPeriodicWorkRequestUploadData()
            navigateToLogin("Logged out")
        }
    }

    private fun cancelPeriodicWorkRequestUploadData(){
        WorkManager.getInstance(requireContext()).cancelUniqueWork(PERIODIC_WORK_UPLOAD_DATA)
    }

    private fun navigateToLogin(message: String){
        nav_host_fragment.findNavController().navigate(R.id.fragment_login)
        Toast.makeText(requireActivity(), message, Toast.LENGTH_LONG).show()
    }


    private fun readLastSynchronisedDateFromDataStore() {
        viewModel.readFromDataStore.observe(viewLifecycleOwner, Observer {
            var text = it
            if (text.equals("none")) {
                text = getString(R.string.unknown)
            }
            binding.textViewLastSync.text = getString(R.string.last_synchronisation_attempt, text)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //clears reference to binding, view is cleaned up in memory
        _binding = null
    }

}