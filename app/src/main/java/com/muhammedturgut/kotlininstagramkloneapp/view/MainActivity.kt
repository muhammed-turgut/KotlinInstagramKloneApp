package com.muhammedturgut.kotlininstagramkloneapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.muhammedturgut.kotlininstagramkloneapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    auth= Firebase.auth

        //güncel kullanıcı
        val currentUser=auth.currentUser
        if(currentUser!= null){
            val intent=Intent(this@MainActivity, FeedActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    fun signinClick(view: View){
   val email =binding.emailText.text.toString()
   val passwor=binding.paswordText.text.toString()

        if(email.isNotEmpty() && passwor.isNotEmpty()){
           auth.signInWithEmailAndPassword(email,passwor).addOnSuccessListener{

           val intent=Intent(this@MainActivity, FeedActivity::class.java)
               startActivity(intent)
               finish()
           }.addOnFailureListener{
    Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_SHORT).show()
           }
        }

    }

    fun signupClick(view: View){
        val email=binding.emailText.text.toString()
        val passwor=binding.paswordText.text.toString()

        if(email.isNotEmpty() && passwor.isNotEmpty()){
       auth.createUserWithEmailAndPassword(email,passwor)
           .addOnCompleteListener{
              val intent=Intent(this@MainActivity, FeedActivity::class.java )
               startActivity(intent)
               finish()

           }.addOnFailureListener{
               Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
           }
        }
        else{
            Toast.makeText(this,"Enter email and Pasword!", Toast.LENGTH_LONG).show()
        }

    }
}