package com.eways.agent.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

class Utils {
    companion object{
        @FunctionalInterface
        interface OnTextChanged {
            fun onChange(text: String)
        }

        fun setOnTextChanged(textView: TextView, onTextChangedCallback: OnTextChanged, bar:() ->Unit) {
            if(textView is EditText) textView
            textView.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    onTextChangedCallback.onChange(s.toString())
                    bar()
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }

        /*fun toRequestBody(`val`: String?): RequestBody? {
            return if (`val` == null) null else RequestBody.create(MediaType.parse("text/plain"), `val`)
        }

        fun isNumeric(number: String): Boolean {
            return number.contains("-?\\d+(\\.\\d+)?")
        }*/

        private fun isNullOrEmpty(str: String?): Boolean {
            return str == null || str.isEmpty()
        }

        fun isNotNullOrEmpty(str: String?): Boolean {
            return !isNullOrEmpty(str)
        }

/*        fun isNullOrEmptyEditable(editable: Editable?): Boolean {
            return editable == null || isNullOrEmpty(editable.toString())
        }

        fun getEmptyIfNull(str: Editable?): String? {
            return str?.toString() ?: ""
        }

        fun intentToGoogleMap(context: Context, x: String, y: String) {
            val gmmIntentUri: Uri = Uri.parse("geo:0,0?q=$y,$x")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            context.startActivity(mapIntent)
        }*/
    }

}