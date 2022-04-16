package com.proyek.infrastructures

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.proyek.infrastructures.user.customer.usecases.GetCustomerList

class MainActivity : AppCompatActivity() {

    private lateinit var customer: GetCustomerList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        customer = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(GetCustomerList::class.java)
        customer.set(this@MainActivity)
        customer.get().observe(this, Observer {
            Log.d("result customer", it.toString())
        })
    }
}
