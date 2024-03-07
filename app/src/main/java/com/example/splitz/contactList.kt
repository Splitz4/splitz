package com.example.splitz

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.splitz.databinding.ActivityContactListBinding
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList


class contactList : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding : ActivityContactListBinding

    //private lateinit var binding: ActivityContactListBinding
    private lateinit var rcvAdapter: RCVAdapter
    private val arrayList: ArrayList<ContactModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if(checkContactPermissions()){
            binding.apply {
                rcvContact.apply {
                    layoutManager = LinearLayoutManager(this@contactList)
                    adapter = RCVAdapter(arrayList,context)

                }
            }
            getContactc()
        }

    }

    private fun getContactc() {
        arrayList.clear()
        val cursor = this.contentResolver
            .query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,

                    ),null,null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
        while (cursor!!.moveToNext()){
            val contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel(contactName, contactNumber)
            arrayList.add(contactModel)
        }
        rcvAdapter.notifyDataSetChanged()
        cursor.close()
    }


    private fun checkContactPermissions():Boolean{
        if (permissionTracker.hasContactPermission(this)){
            return true
        }else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            EasyPermissions.requestPermissions(
                this,
                "You will need to accept the permission in order to run the application",
                100,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
            )
            return true
        }else{
            return false
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        TODO("Not yet implemented")
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }else{
            checkContactPermissions()
        }
    }
}