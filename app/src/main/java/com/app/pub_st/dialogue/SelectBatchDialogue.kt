package com.app.pub_st.dialogue

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.pub_st.R
import com.app.pub_st.dashboard.DashboardFragment
import com.google.android.material.card.MaterialCardView
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SelectBatchDialogue(private val activity: Activity, private val fragment: DashboardFragment) {
    private lateinit var batchArray: JSONArray
    private var batch_id: String = ""
    private var departmentID: String = ""
    private var alertDialog: AlertDialog? = null
    private val department = mutableListOf<String>()
    private val batch = mutableListOf<String>()
    private val sharedPreferences: SharedPreferences =
        activity.getSharedPreferences("authToken", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun startLoadingDialogue() {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val view: View = inflater.inflate(R.layout.select_batch_dialogue, null)
        val selectDepartment: AutoCompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.select_department)
        val selectBatch: AutoCompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.select_batch)
        builder.setView(view)
        builder.setCancelable(false)
        alertDialog = builder.create()
        alertDialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        alertDialog!!.window!!.attributes.windowAnimations = R.style.SlidingDialogAnimation
        alertDialog!!.show()


        /**for batch*/
        department.clear()
        val departments = sharedPreferences.getString("all_department", "")
        val jsonArray = JSONArray(departments)
        for (i in 0 until jsonArray.length()) {
            val departmentObject: JSONObject = jsonArray.getJSONObject(i)
            department.add(departmentObject.getString("name"))
        }

        /*for department select*/
        val departmentAdapter =
            ArrayAdapter(activity.applicationContext, R.layout.spiner_select_item, department)
        selectDepartment.setAdapter(departmentAdapter)

        /**for term*/
        val items = arrayOf("Mid", "Final")
        val itemAdapter = ArrayAdapter(activity, R.layout.spiner_select_item, items)
        view.findViewById<AutoCompleteTextView>(R.id.select_term).setAdapter(itemAdapter)
        val loading = view.findViewById<ProgressBar>(R.id.batch_loading)

        /**get batch data*/
        selectDepartment.onItemClickListener =
            AdapterView.OnItemClickListener { parent, View, position, id ->
                val selectedDepartment = selectDepartment.text.toString()
                loading.visibility = android.view.View.VISIBLE
                for (i in 0 until jsonArray.length()) {
                    val departmentObject: JSONObject = jsonArray.getJSONObject(i)
                    val name = departmentObject.getString("name")
                    if (selectedDepartment == name) {
                        departmentID = departmentObject.getString("id")
                    }
                }
                getBatchData(view)

            }



        /**submit button click*/
        view.findViewById<MaterialCardView>(R.id.select_batch_submit_btn).setOnClickListener {
            val loading_btn = view.findViewById<ProgressBar>(R.id.submit_btn_loading)
            loading_btn.visibility = View.VISIBLE
            val department = selectDepartment.text.toString()
            val batch = selectBatch.text.toString()
            val term = view.findViewById<TextView>(R.id.select_term).text.toString()

            fragment.selectBatch(department, batch, term)
        }
    }

    fun dismissDialogue() {
        alertDialog!!.dismiss()
    }

    private fun getBatchData(view: View) {
        val selectBatch: AutoCompleteTextView =
            view.findViewById<AutoCompleteTextView>(R.id.select_batch)
        val queue = Volley.newRequestQueue(activity)
        val url = "https://pub-backend.dreamitdevlopment.com/public/api/batch/all/$departmentID"
// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // Display the first 500 characters of the response string.
                try {
                    batch.clear()
                    batchArray = JSONArray(response)
                    for (i in 0 until batchArray.length()) {
                        val batch: JSONObject = batchArray.getJSONObject(i)
                        this.batch.add(batch.getString("name"))
                    }

                    /*for address select*/
                    val batchAdapter = ArrayAdapter(
                        activity.applicationContext,
                        R.layout.spiner_select_item,
                        batch
                    )
                    selectBatch.setAdapter(batchAdapter)
                    val loading = view.findViewById<ProgressBar>(R.id.batch_loading)
                    loading.visibility = android.view.View.GONE


                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> Log.d(ContentValues.TAG, "onErrorResponse:    $error") }
        queue.add(stringRequest)
    }

}