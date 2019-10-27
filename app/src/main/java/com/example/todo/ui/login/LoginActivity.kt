package com.example.todo.ui.login
import android.content.Intent
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
import com.example.todo.databinding.ActivityLoginBinding
import com.example.todo.ui.list.EventListActivity
import com.example.todo.ui.register.RegisterActivity

//登录界面
class LoginActivity : AppCompatActivity() {
    private lateinit var binding:ActivityLoginBinding
    lateinit var loginViewModel:LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //充满状态栏
        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        loginViewModel = ViewModelProviders.of(this,LoginViewModelFactory(LoginRegisterRepository.getInstance(ToDoNetwork.getInstance()))).get(LoginViewModel::class.java)
        init() //初始化
        binding.loginViewModel = loginViewModel

        //监听登录返回信息
        loginViewModel.info.observe(this, Observer {
            val errorCode = it.errorCode
            if(errorCode == 0){
                setResult(1)
                val intent = Intent(this,EventListActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Log.d("loginActivity","fail")
                Toast.makeText(this,it.errorMsg,Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun init(){
        //设置初值
        loginViewModel.isPasswordFocus.set(0)
        loginViewModel.isUsernameFocus.set(0)
        loginViewModel.isPasswordNotNull.set(0)
        loginViewModel.isUsernameNotNull.set(0)




        //监听editText焦点和输入改变UI
        binding.loginUsername.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                loginViewModel.isUsernameFocus.set(1)
            }else{
                loginViewModel.isUsernameFocus.set(0)
            }

        }
        binding.loginPassword.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                loginViewModel.isPasswordFocus.set(1)
            }else{
                loginViewModel.isPasswordFocus.set(0)
            }
        }
        binding.loginUsername.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            @Suppress("UNREACHABLE_CODE")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s == null){
                    loginViewModel.isUsernameNotNull.set(0)
                }else{
                    if(s.isNotEmpty()){
                        loginViewModel.isUsernameNotNull.set(1)
                    }else{
                        loginViewModel.isUsernameNotNull.set(0)
                    }
                }
            }
        })
        binding.loginPassword.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            @Suppress("UNREACHABLE_CODE")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s == null){
                    loginViewModel.isPasswordNotNull.set(0)
                }else{
                    if(s.isNotEmpty()){
                        loginViewModel.isPasswordNotNull.set(1)
                    }else{
                        loginViewModel.isPasswordNotNull.set(0)
                    }
                }
            }

        })

        //设置监听
        binding.usernameClearButton.setOnClickListener { binding.loginUsername.setText("")}
        binding.passwordClearButton.setOnClickListener { binding.loginPassword.setText("") }
        binding.loginToRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent) }
        binding.loginButton.setOnClickListener { loginViewModel.login(binding.loginUsername.text.toString(),binding.loginPassword.text.toString())}
        binding.loginToRegister.setOnClickListener { intent = Intent(this,RegisterActivity::class.java)
        startActivity(intent)}
    }
}
