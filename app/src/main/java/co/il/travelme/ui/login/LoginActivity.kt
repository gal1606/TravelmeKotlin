package co.il.travelme.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import co.il.travelme.AuthViewModel
import co.il.travelme.CurrentUser
import co.il.travelme.MainActivity
import co.il.travelme.databinding.ActivityLoginBinding

import co.il.travelme.R
import co.il.travelme.StoreViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var route: String = "Login"
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            val view = binding.root
            if (AuthViewModel.authViewModel.checkCurrentUser())
            {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            binding.signupPromptTextView.setOnClickListener {
                if (route == "Login") {
                    binding.nameEditText.visibility = View.VISIBLE
                    binding.signupPromptTextView.text = this.getString(R.string.already_have_account)
                    route = "signup"
                } else {
                    binding.nameEditText.visibility = View.GONE
                    binding.signupPromptTextView.text = this.getString(R.string.dont_have_account)
                    route = "Login"
                }
            }
            binding.loginButton.setOnClickListener {
               val userEmail = binding.emailEditText.text.toString()
               val userPassword = binding.passwordEditText.text.toString()
                val name = binding.nameEditText.text.toString()
                if (route != "Login") {
                    AuthViewModel.authViewModel.register(userEmail,userPassword,name,
                        onSuccess = {
                            CurrentUser.currentUser = it
                            StoreViewModel.storeViewModel.addUser(CurrentUser.currentUser!!, {}, {})
                            Toast.makeText(this,"Your account has been created",Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        },
                        onFailure = { exception ->
                            Toast.makeText(this,exception.toString(),Toast.LENGTH_LONG).show()
                        }
                    )
                }
                else
                {
                    AuthViewModel.authViewModel.login(userEmail,userPassword,
                        onSuccess = {
                            Toast.makeText(this,"Login is successful",Toast.LENGTH_LONG).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                            CurrentUser.currentUser = it
                        },
                        onFailure = { exception ->
                            Toast.makeText(this,exception.toString(),Toast.LENGTH_LONG).show()
                        })
                }
            }

            setContentView(view)
        }


    class CheckIfUserLogIn {

    }

}