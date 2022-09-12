package com.app.pub_st

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import org.json.JSONException

class BottomNavActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_nav)
        sharedPreferences =getSharedPreferences("authToken", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        fetchRoutineData(applicationContext)

        val bottomNavigationView = findViewById<BottomNavigationView
                >(R.id.bottom_navigation)
        val navController = findNavController(R.id.bottom_nav_fragment)
        bottomNavigationView.setupWithNavController(navController)

        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.dashboard -> {
                    // Respond to navigation item 1 click
                    true
                }
                R.id.classroom -> {
                    // Respond to navigation item 2 click
                    true
                }
                R.id.notice -> {
                    // Respond to navigation item 2 click
                    true
                }
                else -> false
            }
        }
    }

    fun fetchRoutineData(applicationContext: Context) {
        val sharedPreferences: SharedPreferences =
            applicationContext.getSharedPreferences("authToken", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val batch = sharedPreferences.getString("batch", "")
        val term = sharedPreferences.getString("term", "")
        val department = sharedPreferences.getString("department", "")

        val queue = Volley.newRequestQueue(applicationContext)
        val url = "https://pub-backend.dreamitdevlopment.com/public/api/routine/create"

// Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(
            Method.POST,
            url,
            Response.Listener { response ->
                try {
                    Log.d(TAG, "fetchRoutineData: @@@@@@@@@@@ $response")
                    editor.putString("routine",response)
                    editor.apply()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            Response.ErrorListener { error ->
                Log.d(
                    ContentValues.TAG,
                    "onErrorResponse:    $error"
                )
            }) {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()
                params["department"] = department.toString()
                params["batch"] = batch.toString()
                return params
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}