package com.app.pub_st.dashboard

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.pub.attendance.AttendanceAdapter
import com.app.pub.attendance.AttendanceItems
import com.app.pub_st.R
import com.app.pub_st.databinding.FragmentDashboardBinding
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("authToken", Context.MODE_PRIVATE)
       // getAttendanceData()
        arguments?.let {
        }
    }

    lateinit var binding: FragmentDashboardBinding
    private val attendanceItems = mutableListOf<AttendanceItems>()
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        fetchAttendanceData(activity,context)
        with(binding){
            routineRecycler.layoutManager = LinearLayoutManager(activity)
            routineRecycler.adapter =
                AttendanceAdapter(attendanceItems)
        }
        return binding.root
    }

    private fun getAttendanceData() {
        attendanceItems.clear()
        val attendance = sharedPreferences.getString("attendance","")
        val jsonArray = JSONArray(attendance)
        for (i in 0 until jsonArray.length()) {
            val attendanceData: JSONObject = jsonArray.getJSONObject(i)
            attendanceItems.add( AttendanceItems(
                attendanceData.getString("created_at"),
            )
            )
        }
    }

    fun fetchAttendanceData(act: FragmentActivity?, context: Context?) {
        val sharedPreferences: SharedPreferences =
            act!!.getSharedPreferences("authToken", Context.MODE_PRIVATE)
        val batch = sharedPreferences.getString("batch", "")
        val term = sharedPreferences.getString("term", "")
        val course = sharedPreferences.getString("course", "")
        val token = sharedPreferences.getString("token", "")

        val queue = Volley.newRequestQueue(context)
        val url = "https://pub-backend.dreamitdevlopment.com/public/api/routine/create"

// Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    Log.d(TAG, "fetchAttendanceData:  $response")
//                    editor = sharedPreferences.edit()
//                    editor.putString("attendance",response)
//                    editor.apply()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.d(ContentValues.TAG, "onErrorResponse:    $error") }) {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()
                params["department"] = "CSE"
                params["batch"] = "12th"
                return params
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DashboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DashboardFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}