package com.therxmv.dirolreader.ui.auth

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.databinding.FragmentAuthBinding
import com.therxmv.dirolreader.utils.AuthState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AuthFragment : Fragment() {
    private var binding: FragmentAuthBinding? = null
    private val vm: AuthViewModel by viewModels()

    private val title: TextView by lazy { binding!!.title }
    private val inputLayout: TextInputLayout by lazy { binding!!.inputLayout }
    private val confirmBtn: MaterialButton by lazy { binding!!.confirmBtn }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater)
        vm.getClient()

        lifecycleScope.launch {
            vm.authState.collectLatest {
                when (it) {
                    AuthState.PHONE -> {
                        setupScreen(
                            getString(R.string.authTitlePhone),
                            InputType.TYPE_CLASS_PHONE,
                        )
                    }
                    AuthState.CODE -> {
                        setupScreen(
                            getString(R.string.authTitleCode),
                            InputType.TYPE_CLASS_NUMBER,
                        )
                    }
                    AuthState.PASSWORD -> {
                        setupScreen(
                            getString(R.string.authTitlePassword),
                            InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
                        )
                    }
                    AuthState.READY -> {
                        // TODO add splash screen
                        findNavController().navigate(R.id.action_authFragment_to_newsFragment)
                    }
                    AuthState.ERROR -> {

                    }
                }
            }
        }

        return binding!!.root
    }

    private fun setupScreen(text: String, inputType: Int) {
        title.text = text
        inputLayout.editText?.setText("")
        inputLayout.editText?.inputType = inputType
        setupButton(text)
    }

    private fun setupButton(text: String) {
        confirmBtn.setOnClickListener {
            when(text) {
                getString(R.string.authTitlePhone) -> vm.confirmPhone(inputLayout.editText?.text.toString())
                getString(R.string.authTitleCode) -> vm.confirmCode(inputLayout.editText?.text.toString())
                getString(R.string.authTitlePassword) -> vm.confirmPassword(inputLayout.editText?.text.toString())
            }
        }
    }
}