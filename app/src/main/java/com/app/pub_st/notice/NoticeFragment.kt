package com.app.pub_st.notice

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.app.pub_st.R
import com.app.pub_st.databinding.FragmentNoticeBinding
import org.json.JSONArray
import org.json.JSONException


class NoticeFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getAllDepartment()
        arguments?.let {
        }
    }
    lateinit var binding: FragmentNoticeBinding
    private val noticeItems = mutableListOf<NoticeItems>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoticeBinding.inflate(inflater, container, false)
        with(binding){
            noticeRecycler.layoutManager = LinearLayoutManager(activity)
            noticeRecycler.adapter = NoticeAdapter(noticeItems)
        }
        return binding.root
    }

    private  fun getAllDepartment() {

        val queue = Volley.newRequestQueue(activity)
        val url = "https://pub-backend.dreamitdevlopment.com/public/api/notice/all"
// Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response -> // Display the first 500 characters of the response string.
                try {
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        noticeItems.add(
                            NoticeItems(
                                jsonObject.getString("id"),
                                jsonObject.getString("title"),
                                jsonObject.getString("details"),
                                jsonObject.getString("created_at"),
                            )
                        )
                    }
                    binding.noticeRecycler.adapter!!.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }) { error -> Log.d(ContentValues.TAG, "onErrorResponse:    $error") }
        queue.add(stringRequest)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            NoticeFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}