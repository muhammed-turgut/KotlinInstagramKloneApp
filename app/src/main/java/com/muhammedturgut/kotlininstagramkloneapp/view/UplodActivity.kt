package com.muhammedturgut.kotlininstagramkloneapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.muhammedturgut.kotlininstagramkloneapp.databinding.ActivityUplodBinding
import java.util.UUID

class UplodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUplodBinding
    var selectedBitmap : Bitmap? = null
    private lateinit var acvtivtyResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUplodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLanchuer()

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    fun uploadButton(view: View) {
        println("Hata 1")
        //UUID -> image name
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val storage = Firebase.storage
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)

        if (selectedPicture != null) {
            println("Hata 2")
            imagesReference.putFile(selectedPicture!!).addOnSuccessListener {

                val uploadedPictureReference = storage.reference.child("images").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()


                    val postMap = hashMapOf<String,Any>()
                    postMap.put("downloadUrl",downloadUrl)
                    postMap.put("userEmail",auth.currentUser!!.email!!.toString())
                    postMap.put("comment",binding.commentText.text.toString())
                    postMap.put("date", Timestamp.now())


                    firestore.collection( "Posts").add(postMap).addOnSuccessListener{
                            //back
                        println("Hata 4")
                            finish()

                    }.addOnFailureListener{
                        println("Hata 5")
                        Toast.makeText(this@UplodActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }

                }

            }

        }
    }


    fun selectedImage(view: View){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission needed for gallerry",Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()
            }
            else{
                permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
        else{
            val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            acvtivtyResultLauncher.launch(intentToGallery)

        }
    }

    private fun registerLanchuer(){

        acvtivtyResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult=result.data
                if(intentFromResult != null){
                    selectedPicture= intentFromResult.data
                    selectedPicture.let {
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }

        permissionLauncher=registerForActivityResult(ActivityResultContracts.RequestPermission()){result ->
            if(result){
                //permission granted
                val intentToGallery=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                acvtivtyResultLauncher.launch(intentToGallery)
            }
            else{
                Toast.makeText(this@UplodActivity,"Permission needed",Toast.LENGTH_LONG).show()
            }
        }

    }
}
