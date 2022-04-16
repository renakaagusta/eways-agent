package com.eways.agent.user.activity

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.eways.agent.R
import com.eways.agent.core.baseactivity.BaseActivity
import com.eways.agent.dashboard.activity.WelcomeActivity
import com.eways.agent.utils.customsupportactionbar.CustomSupportActionBar
import com.proyek.infrastructures.user.agent.entities.UserAgent
import com.proyek.infrastructures.user.agent.usecases.GetAgentDetail
import com.proyek.infrastructures.user.agent.usecases.UploadImageProfileAgent
import com.proyek.infrastructures.user.cluster.usecases.GetClusterDetail
import com.proyek.infrastructures.utils.Authenticated
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfileActivity : BaseActivity() {
    private lateinit var user: UserAgent
    private lateinit var getClusterDetail: GetClusterDetail
    private val GALLERY = 1
    private val CAMERA = 2
    private val IMAGE_DIRECTORY = "/profile"
    private lateinit var uploadImageProfileAgent: UploadImageProfileAgent
    private lateinit var getAgentDetail: GetAgentDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        CustomSupportActionBar.setCustomActionBar(this, "Profil" )

        getClusterDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetClusterDetail::class.java)
        uploadImageProfileAgent = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(UploadImageProfileAgent::class.java)
        getAgentDetail = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetAgentDetail::class.java)

        moveToProfileUpdateFullname()
        moveToProfileUpdateNIK()
        moveToProfileUpdatePhone()
        moveToProfileUpateEmail()
        moveToProfileUpdateAddress()
        moveToProfileUpdateEmployeeID()

        logout()
    }

    override fun onStart() {
        super.onStart()

        GlobalScope.launch(Dispatchers.Main) {
            if (Authenticated.isValidCacheMember()){
                setProfileInfo()
            } else {
                this@ProfileActivity.showProgress()
                var userID = Authenticated.getUserAgent().ID
                getAgentDetail.set(userID!!, this@ProfileActivity)
                getAgentDetail.get().observe(this@ProfileActivity, Observer {
                    this@ProfileActivity.dismissProgress()
                    Authenticated.setUserAgent(it.data[0])
                    setProfileInfo()
                })
            }

            ivChangeImage.setOnClickListener {
                showPictureDialog()
            }
        }

        requestPermission()
    }

    fun setProfileInfo(){
        user = Authenticated.getUserAgent()

        if (user.imagePath != null)
            Glide.with(this@ProfileActivity)
                .load("http://13.229.200.77:8001/storage/${user.imagePath}")
                .into(civUserImage)

        Log.w("ProfileActivity", user.toString())

        tvUserName.text = user.username
        tvUserFullname.text = user.fullname
        tvEmployeeID.text = user.agent?.employee_id
        tvUserAddress.text = user.address
        tvUserCluster.text = user.agent?.cluster_id
        tvUserEmail.text = user.email
        tvUserPhone.text = user.phoneNumber
        tvUserNIK.text = user.agent?.nik

        tvUserCluster.text = user.cluster?.name
    }

    fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) { // Permission is not granted
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.CAMERA
                )
            ) {
            } else { // No explanation needed; request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.CAMERA
                    ),
                    1
                )
            }
        }

    }

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Pilih foto dari")
        val pictureDialogItems = arrayOf("Galeri", "Kamera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> choosePhotoFromGallary()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun choosePhotoFromGallary() {
        val galleryIntent = Intent(Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA)
    }

    public override fun onActivityResult(requestCode:Int, resultCode:Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                val contentURI = data.data
                try
                {
                    val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, contentURI)
                    Glide.with(this)
                        .load(bitmap)
                        .into(civUserImage)
                    val file = saveImage(bitmap)
                    uploadImage(file)
                }
                catch (e: IOException) {
                    e.printStackTrace()
                   Log.d("error", e.message)
                }
                Log.d("URI", contentURI.toString())
            }

        }
        else if (requestCode == CAMERA)
        {
            val bitmap = data!!.extras!!.get("data") as Bitmap
            Glide.with(this)
                .load(bitmap)
                .into(civUserImage)
            val file = saveImage(bitmap)
            uploadImage(file)
        }
    }

    fun uploadImage(file: File) {
        val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val body: MultipartBody.Part = MultipartBody.Part.createFormData("image", file.name, requestFile)
        uploadImageProfileAgent.set(user.ID!!, body, this@ProfileActivity)
        showProgress()
        uploadImageProfileAgent.get().observe(this, Observer {
            dismissProgress()
            getAgentDetail.set(Authenticated.getUserAgent().ID!!, this)
            getAgentDetail.get().observe(this@ProfileActivity, Observer {
                Authenticated.setUserAgent(it.data[0])
                setProfileInfo()
            })
        })
    }

    fun saveImage(bitmap: Bitmap): File {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "profile.jpg")
        val bos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 30, bos)
        val bitmapdata = bos.toByteArray()

        try {
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }


    private fun  moveToProfileUpdateFullname(){
        llUserFullname.setOnClickListener {
            val intent = Intent(this, ProfileUpdateFullnameActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToProfileUpdateNIK(){
        llUserNIK.setOnClickListener {
            val intent = Intent(this, ProfileUpdateNIKActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToProfileUpdatePhone(){
        llUserPhone.setOnClickListener {
            val intent = Intent(this, ProfileUpdatePhoneActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToProfileUpateEmail(){
        llUserEmail.setOnClickListener {
            val intent = Intent(this, ProfilUpdateEmailActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToProfileUpdateAddress(){
        llUserAddress.setOnClickListener {
            val intent = Intent(this, ProfileUpdateAddressActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun moveToProfileUpdateEmployeeID(){
        llEmployeeID.setOnClickListener {
            val intent = Intent(this, ProfileUpdateEmployeeIDActivity::class.java)
            intent.putExtra("user", user)
            startActivity(intent)
        }
    }

    private fun logout(){
        tvLogout.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            Authenticated.destroySession()
            startActivity(intent)
            finish()
        }
    }
}