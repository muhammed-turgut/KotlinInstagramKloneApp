package com.muhammedturgut.kotlininstagramkloneapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.muhammedturgut.kotlininstagramkloneapp.databinding.ActivityUplodBinding

class UplodActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUplodBinding
    private lateinit var acvtivtyResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    var selectedPicture : Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUplodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerLanchuer();

    }

    fun uploadButton(view: View){

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