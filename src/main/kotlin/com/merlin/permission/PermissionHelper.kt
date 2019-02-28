package com.merlin.permission

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.content.ContextCompat

/**
 * 功能描述：
 *
 * @author merlin
 * @date 2019/2/26
 */
open class PermissionHelper private constructor() {

    companion object {
        fun inst(): PermissionHelper {
            return InstHolder.permissionHelper
        }
    }

    private object InstHolder {
        val permissionHelper = PermissionHelper()
    }

    private var permissionResult: OnPermissionResultListener? = null
    private var isShowGuide = true
    private var guideIconRes = R.drawable.permission_dialog_setting
    private var guideExplain = "为了能完整的展示相关功能，需要您允许获取部分权限。请前往“权限管理”中进行设置："
    private var guidePermission = ""
    private var guideBtnLeft = "取消"
    private var guideBtnRight: String = "去设置"

    fun getPermissionResultListener(): OnPermissionResultListener? {
        return permissionResult
    }

    fun checkPermissionsForResult(context: Context, vararg permissions: String): Map<String, Boolean> {
        val map = HashMap<String, Boolean>()
        for (permission in permissions) {
            map[permission] = (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_DENIED)
        }
        return map
    }

    fun checkPermissions(context: Context, vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    fun requestPermissions(activity: Activity, onPermissionResultListener: OnPermissionResultListener?,
                           vararg permissions: String) {
        if (checkPermissions(activity, *permissions)) {
            onPermissionResultListener?.onPermissionGranted()
        } else {
            this.permissionResult = onPermissionResultListener
            startPermissionRequestActivity(activity, *permissions)
        }
    }

    fun goSettingDetail(context: Context) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.data = Uri.parse("package:${context?.packageName}")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        context.startActivity(intent)
    }

    private fun startPermissionRequestActivity(activity: Activity, vararg permissions: String) {
        val it = Intent(activity, PermissionRequestActivity::class.java)
        it.putExtra("permissions", permissions)
        activity.startActivity(it)
        activity.overridePendingTransition(0, 0)
    }

    interface OnPermissionResultListener {
        fun onPermissionGranted()
        fun onPermissionDenied(resultMap: Map<String, Boolean>?)
    }

    fun setShowGuide(isShowGuide: Boolean): PermissionHelper {
        this.isShowGuide = isShowGuide
        return InstHolder.permissionHelper
    }

    fun setGuideIcon(iconRes: Int): PermissionHelper {
        this.guideIconRes = iconRes
        return InstHolder.permissionHelper
    }

    fun setGuideExpain(explain: String): PermissionHelper {
        this.guideExplain = explain
        return InstHolder.permissionHelper
    }

    fun setGuidePermission(permission: String): PermissionHelper {
        this.guidePermission = permission
        return InstHolder.permissionHelper
    }

    fun setGuideBtnLeft(btnLeft: String): PermissionHelper {
        this.guideBtnLeft = btnLeft
        return InstHolder.permissionHelper
    }

    fun setGuideBtnRight(btnRight: String): PermissionHelper {
        this.guideBtnRight = btnRight
        return InstHolder.permissionHelper
    }

    fun isShowGuide() = isShowGuide

    fun getGuideIcon() = guideIconRes

    fun getGuideExplain() = guideExplain

    fun getGuidePermission() = guidePermission

    fun getGuideBtnLeft() = guideBtnLeft

    fun getGuideBtnRight() = guideBtnRight

}
