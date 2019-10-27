package com.example.todo.ui.register


import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.todo.R
import com.example.todo.data.LoginRegisterRepository
import com.example.todo.data.network.ToDoNetwork
import com.example.todo.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegisterBinding
    lateinit var registerViewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_register)
        registerViewModel = ViewModelProviders.of(this,RegisterViewModelFactory(LoginRegisterRepository.getInstance(
            ToDoNetwork.getInstance()
        ))).get(RegisterViewModel::class.java)
        binding.registerViewModel = registerViewModel

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        supportActionBar?.hide()

        init()

        registerViewModel.info.observe(this, Observer {
            val errorCode = it.errorCode
            if(errorCode == 0){
                Toast.makeText(this,"注册成功！", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Log.d("loginActivity","fail")
                Toast.makeText(this,it.errorMsg, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init(){
        registerViewModel.isUsernameFocus.set(0)
        registerViewModel.isUsernameNotNull.set(0)
        registerViewModel.isPasswordFocus.set(0)
        registerViewModel.isPasswordNotNull.set(0)
        registerViewModel.isRePasswordFocus.set(0)
        registerViewModel.isRePasswordNotNull.set(0)

        binding.registerUsername.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                registerViewModel.isUsernameFocus.set(1)
            }else{
                registerViewModel.isUsernameFocus.set(0)
            }
        }
        binding.registerPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                registerViewModel.isPasswordFocus.set(1)
            }else{
                registerViewModel.isPasswordFocus.set(0)
            }
        }
        binding.registerRePassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                registerViewModel.isRePasswordFocus.set(1)
            }else{
                registerViewModel.isRePasswordFocus.set(0)
            }
        }

        binding.registerUsername.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s == null){
                    registerViewModel.isUsernameNotNull.set(0)
                }else{
                    if(s.isNotEmpty()){
                        registerViewModel.isUsernameNotNull.set(1)
                    }else{
                        registerViewModel.isUsernameNotNull.set(0)
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
        binding.registerPassword.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s == null){
                    registerViewModel.isPasswordNotNull.set(0)
                }else{
                    if(s.isNotEmpty()){
                        registerViewModel.isPasswordNotNull.set(1)
                    }else{
                        registerViewModel.isPasswordNotNull.set(0)
                    }
                }
            }

        })
        binding.registerRePassword.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s == null){
                    registerViewModel.isRePasswordNotNull.set(0)
                }else{
                    if(s.isNotEmpty()){
                        registerViewModel.isRePasswordNotNull.set(1)
                    }else{
                        registerViewModel.isRePasswordNotNull.set(0)
                    }
                }
            }
        })

        binding.registerUsernameClearButton.setOnClickListener { binding.registerUsername.setText("") }
        binding.rePasswordClearButton.setOnClickListener { binding.registerRePassword.setText("") }
        binding.registerPasswordClearButton.setOnClickListener { binding.registerPassword.setText("") }

        binding.registerButton.setOnClickListener { registerViewModel.register(binding.registerUsername.text.toString(),binding.registerPassword.text.toString(),binding.registerRePassword.text.toString()) }
    }
}
