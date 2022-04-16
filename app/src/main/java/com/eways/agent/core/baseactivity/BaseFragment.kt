package com.eways.agent.core.baseactivity

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import cn.pedant.SweetAlert.SweetAlertDialog

abstract class BaseFragment: Fragment() {
    private var pDialog: SweetAlertDialog? = null

    open fun showProgress() {
        pDialog = SweetAlertDialog(requireContext(), SweetAlertDialog.PROGRESS_TYPE)
        pDialog!!.progressHelper.barColor = Color.parseColor("#A5DC86")
        pDialog!!.titleText = "Memuat"
        pDialog!!.setCancelable(false)
        pDialog!!.show()
    }

    open fun dismissProgress() {
        if (pDialog != null) pDialog!!.dismiss()
    }

    open fun showSuccess(message: String?) {
        val swal = SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
        swal.titleText = "Sukses"
        swal.contentText = message
        swal.show()
    }

    open fun showError(text: String) {
        val swal = SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
        swal.titleText = "Error"
        swal.contentText = text
        swal.show()
    }

}