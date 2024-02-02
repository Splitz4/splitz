package com.example.splitz

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object permissionTracker {

    fun hasContactPermission(context: Context):Boolean =
        EasyPermissions.hasPermissions(
            context,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.WRITE_CONTACTS,
        )

}