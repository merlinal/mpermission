package com.merlin.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View


/**
 * 功能描述：
 *
 * @author merlin
 * @date 2019/2/26
 */
class PermissionRequestActivity : AppCompatActivity() {

    private var isFromSetting = false
    private var permissions: Array<out String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissions = intent.getStringArrayExtra("permissions")
        //已授权的权限在设置中关闭会引起app进程重建，此时savedInstanceState!=null
        if (savedInstanceState == null) {
            if (permissions != null && permissions!!.isNotEmpty()) {
                ActivityCompat.requestPermissions(this, permissions!!, 1000)
            } else {
                finish()
            }
        } else {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isFromSetting) {
            if (PermissionHelper.inst().checkPermissions(this, *permissions!!)) {
                PermissionHelper.inst().getPermissionResultListener()?.onPermissionGranted()
            } else {
                PermissionHelper.inst().getPermissionResultListener()?.onPermissionDenied(
                        PermissionHelper.inst().checkPermissionsForResult(this, *permissions!!))
            }
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        overridePendingTransition(0, 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1000) {
            var isGranted = true
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    isGranted = false
                    break
                }
            }
            if (isGranted) {
                PermissionHelper.inst().getPermissionResultListener()?.onPermissionGranted()
                finish()
            } else {
                if (PermissionHelper.inst().isShowGuide()) {
                    val dialog = PermissionGuideDialog()
                    dialog.btnLeftListener = View.OnClickListener {
                        finish()
                    }
                    dialog.btnRightListener = View.OnClickListener {
                        isFromSetting = true
                    }
                    dialog.show(supportFragmentManager)
                } else {
                    val resultMap = HashMap<String, Boolean>()
                    for ((index, permission) in permissions.withIndex()) {
                        resultMap[permission] = (grantResults[index] == PackageManager.PERMISSION_GRANTED)
                    }
                    PermissionHelper.inst().getPermissionResultListener()?.onPermissionDenied(resultMap)
                    finish()
                }
            }
        }
    }

}
