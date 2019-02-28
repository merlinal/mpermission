package com.merlin.permission

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.permission_guide_dialog.*


/**
 * @author merlin.
 */

class PermissionGuideDialog : DialogFragment() {

    var btnLeftListener: View.OnClickListener? = null
    var btnRightListener: View.OnClickListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //点击空白处能否取消
        isCancelable = false
        //在此处设置 无标题 对话框背景色
        val window = dialog.window
        window!!.requestFeature(Window.FEATURE_NO_TITLE)
        //对话框背景色
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        //背景黑暗度
//        window.setDimAmount(0.35f)
        //设置dialog的 进出 动画
        window.setWindowAnimations(R.style.permission_anim_dialog)

        return inflater.inflate(R.layout.permission_guide_dialog, container, false)
    }

    override fun onResume() {
        super.onResume()

        mGuideIcon.setImageResource(PermissionHelper.inst().getGuideIcon())
        mGuideExplain.text = PermissionHelper.inst().getGuideExplain()
        mGuidePermission.text = PermissionHelper.inst().getGuidePermission()
        mGuideBtnLeft.text = PermissionHelper.inst().getGuideBtnLeft()
        mGuideBtnLeft.setOnClickListener {
            dismissAllowingStateLoss()
            btnLeftListener?.onClick(mGuideBtnLeft)
        }
        mGuideBtnRight.text = PermissionHelper.inst().getGuideBtnRight()
        mGuideBtnRight.setOnClickListener {
            PermissionHelper.inst().goSettingDetail(mGuideBtnRight.context)
            dismissAllowingStateLoss()
            btnRightListener?.onClick(mGuideBtnRight)
        }
    }

    fun show(manager: FragmentManager?) {
        super.show(manager, tag)
    }

    override fun dismiss() {
        super.dismissAllowingStateLoss()
    }


}
