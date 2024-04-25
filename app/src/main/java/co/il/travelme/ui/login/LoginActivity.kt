package co.il.travelme.ui.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import co.il.travelme.databinding.ActivityLoginBinding

import co.il.travelme.R
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var route: String = "Login"
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)
            val view = binding.root


            binding.signupPromptTextView.setOnClickListener {
                Log.i("Login","Login")
                if (route != "Login") {
                    binding.nameEditText.visibility = View.VISIBLE
                    binding.signupPromptTextView.text = this.getString(R.string.already_have_account)
                    route = "Login"
                } else {
                    binding.nameEditText.visibility = View.GONE
                    binding.signupPromptTextView.text = this.getString(R.string.dont_have_account)
                    route = "signup"
                }
            }

            binding.loginButton.setOnClickListener {
               val userEmail = binding.emailEditText.text.toString()
               val userPassword = binding.passwordEditText.text.toString()

                signupWithFirebase(userEmail,userPassword)
            }

            setContentView(view)
        }

        fun signupWithFirebase(userEmail:String,userPassword:String)
        {
            auth.createUserWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Toast.makeText(this,"Your account has been created",Toast.LENGTH_LONG).show()
                    finish()
                }
                else{
                    Toast.makeText(this,task.exception.toString(),Toast.LENGTH_LONG).show()
                }
            }
        }
    }
