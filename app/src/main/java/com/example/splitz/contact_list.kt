package com.example.splitz

import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [contact_list.newInstance] factory method to
 * create an instance of this fragment.
 */
class contact_list : Fragment(), EasyPermissions.PermissionCallbacks {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
   // private lateinit var binding : ActivityContactListBinding

    //private lateinit var binding: ActivityContactListBinding
    private lateinit var rcvAdapter: RCVAdapter
    private val arrayList: ArrayList<ContactModel> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_contact_list, container, false)

        val backTrace = view.findViewById<ImageView>(R.id.backTrace)

        backTrace.setOnClickListener {
            if (childFragmentManager.backStackEntryCount > 0) {
                childFragmentManager.popBackStack()
            } else {
                activity?.onBackPressed()
            }
        }
        return view


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView(view)
        if (checkContactPermissions()) {
            getContacts()
        }
    }
    private fun setupRecyclerView(view: View) {
        rcvAdapter = RCVAdapter(arrayList, requireContext())
        val rcvContact = view.findViewById<RecyclerView>(R.id.rcvContact)
        rcvContact.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = rcvAdapter
        }
    }

    private fun getContacts() {
        arrayList.clear()
        val cursor = requireActivity().contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            arrayOf(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
            ),
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
        )
        while (cursor?.moveToNext() == true) {
            val contactName =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val contactNumber =
                cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

            val contactModel = ContactModel(contactName, contactNumber)
            arrayList.add(contactModel)
        }
        rcvAdapter.notifyDataSetChanged()
        cursor?.close()
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        TODO("Not yet implemented")
    }
    private fun checkContactPermissions(): Boolean {
        return if (permissionTracker.hasContactPermission(requireContext())) {
            true
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            EasyPermissions.requestPermissions(
                this,
                "You will need to accept the permission in order to run the application",
                100,
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS
            )
            false
        } else {
            false
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(requireActivity(), perms)) {
            AppSettingsDialog.Builder(requireActivity()).build().show()
        } else {
            checkContactPermissions()
        }
    }


}


