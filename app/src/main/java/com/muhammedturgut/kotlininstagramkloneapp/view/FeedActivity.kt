package com.muhammedturgut.kotlininstagramkloneapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.muhammedturgut.kotlininstagramkloneapp.R
import com.muhammedturgut.kotlininstagramkloneapp.adapter.FeedAdapter
import com.muhammedturgut.kotlininstagramkloneapp.databinding.ActivityFeedBinding
import com.muhammedturgut.kotlininstagramkloneapp.model.Post

class FeedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFeedBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var postArrayList : ArrayList<Post>
    private lateinit var feedAdapter: FeedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityFeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth
        db=Firebase.firestore

         postArrayList=ArrayList<Post>()
        getData()

        binding.recylerView.layoutManager=LinearLayoutManager(this)
        feedAdapter=FeedAdapter(postArrayList)
        binding.recylerView.adapter=feedAdapter
    }

    private fun getData() {
         db.collection("Posts").orderBy("date",Query.Direction.DESCENDING).addSnapshotListener { value, error ->

             if(error != null){
                 Toast.makeText(this@FeedActivity,error.localizedMessage,Toast.LENGTH_LONG).show()
             }
             else{
                 if (value != null){
                     if(!value.isEmpty){

                         val documents = value.documents

                         postArrayList.clear()
                         for(documents in documents){

                             //casting
                             val comment= documents.get("comment") as String
                             val userEmail = documents.get("userEmail") as String
                             val downloadUrl= documents.get("downloadUrl") as String

                              val post= Post(userEmail,comment,downloadUrl)
                             postArrayList.add(post)
                         }

                       //Günceleme olursa güncelşemeyi yapması için
                         feedAdapter.notifyDataSetChanged()

                     }
                 }
             }

         }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.insta_mneu,menu)
        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId== R.id.add_post){
            val intent=Intent(this@FeedActivity, UplodActivity::class.java)
            startActivity(intent)

        }
        else if(item.itemId == R.id.sign_out){

            //Kullanıcıdan çıkış yapıldı ve sunucuya bildirildi.
            auth.signOut()

            val intent=Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    fun postAdd (view: View){
        val intent=Intent(this@FeedActivity, UplodActivity::class.java)
        startActivity(intent)
    }
}