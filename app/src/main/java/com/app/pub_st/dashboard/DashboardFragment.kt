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
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.pub_st.BottomNavActivity
import com.app.pub_st.databinding.FragmentDashboardBinding
import com.app.pub_st.dialogue.SelectBatchDialogue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject

class DashboardFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            requireActivity().getSharedPreferences("authToken", Context.MODE_PRIVATE)
        //fetchRoutineData()
        CoroutineScope(Dispatchers.IO).launch {
            fetchStudentData()
            getAllDepartment()
        }
        arguments?.let {
        }
    }

    lateinit var binding: FragmentDashboardBinding
    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var selectBatchDialogue: SelectBatchDialogue
    private var selectBatchBtnLoading = false
    val bottomNavActivity:BottomNavActivity = BottomNavActivity()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
//        getRoutineData()
        selectBatchDialogue = SelectBatchDialogue(requireActivity(), this@DashboardFragment);
        editor = sharedPreferences.edit()
        with(binding) {
            batchName.text = sharedPreferences.getString("batch", "")
            selectBatchBtn.setOnClickListener {
                selectBatchDialogue.startLoadingDialogue()
            }
        }
        return binding.root

    }

    private fun getRoutineData() {
        val data = sharedPreferences.getString("routine","").toString()
        Log.d(TAG, "getRoutineData: *****  $data")

        val routineData = JSONObject(data)
        val nineAm = routineData.getJSONObject("nine_am")
        val TeacherNineAm = nineAm.getJSONObject("teacher")
        val tenAm = routineData.getJSONObject("ten_am")
        val TeacherTenAm = tenAm.getJSONObject("teacher")
        val twelvePm = routineData.getJSONObject("twelve_pm")
        val TeacherTwelvePm = twelvePm.getJSONObject("teacher")
        with(binding) {
            nineAmCourseCode.text = nineAm.getString("course_code")
            nineAmCourseName.text = nineAm.getString("course_title")
            nineAmTeacher.text = TeacherNineAm.getString("name")

            tenAmCourseCode.text = tenAm.getString("course_code")
            tenAmCourseName.text = tenAm.getString("course_title")
            tenAmTeacher.text = TeacherTenAm.getString("name")

            twelvePmCourseCode.text = twelvePm.getString("course_code")
            twelvePmCourseName.text = twelvePm.getString("course_title")
            twelvePmTeacher.text = TeacherTwelvePm.getString("name")
        }
        if (selectBatchBtnLoading) {
            selectBatchDialogue.dismissDialogue()
            selectBatchBtnLoading = false
        }

    }

    private suspend fun fetchStudentData() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("authToken", Context.MODE_PRIVATE)
        val batchID = sharedPreferences.getString("batch_id", "")
        val batch = sharedPreferences.getString("batch", "")
        val course = sharedPreferences.getString("course", "")
        val token = sharedPreferences.getString("token", "")

        val queue = Volley.newRequestQueue(context)
        val url = "https://pub-backend.dreamitdevlopment.com/public/api/student/all?token=$token"

// Request a string response from the provided URL.
        val stringRequest: StringRequest = object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val student = jsonObject.getString("attendanceTaken")
                    editor.putString("students", jsonObject.getString("student"))
                    editor.putString("attendance_taken", jsonObject.getString("attendanceTaken"))
                    editor.apply()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }, Response.ErrorListener { error -> Log.d(ContentValues.TAG, "onErrorResponse:    $error") }) {
            override fun getParams(): Map<String, String>? {

                val params: MutableMap<String, String> = HashMap()
                params["batch_id"] = batchID.toString()
                params["batch"] = batch.toString()
                params["course_name"] = course.toString()
                return params
            }
        }
        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private suspend fun getAllDepartment() {

        val queue = Volley.newRequestQueue(activity)
        val url = "https://pub-backend.dreamitdevlopment.com/public/api/department/all"
// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // Display the first 500 characters of the response string.
                try {
                    editor.putString("all_department", response)
                    editor.apply()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> Log.d(ContentValues.TAG, "onErrorResponse:    $error") }
        queue.add(stringRequest)
    }

    fun selectBatch(department: String, batch: String, term: String) {
        bottomNavActivity.fetchRoutineData(activity!!.applicationContext)
        selectBatchBtnLoading = true
        with(binding) {
            batchName.text = batch
            editor.putString("department", department)
            editor.putString("batch", batch)
            editor.putString("term", term)
            editor.apply()

            CoroutineScope(Dispatchers.IO).launch {
                getRoutineData()
                fetchStudentData()
            }


        }
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