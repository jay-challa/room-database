package com.example.roomdemo

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.webkit.RenderProcessGoneDetail
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.adapter.UserAdapter
import com.example.roomdemo.database.UserDataBase
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.entity.UserName
import com.example.roomdemo.view_model.UserViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),View.OnClickListener,OnValueMatch {



    var adapter:UserAdapter?=null
    var userList:ArrayList<UserName>?=null
    var viewModel:UserViewModel?=null
    val REQ_CODE_SPEECH_INPUT = 100;


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView(this,R.layout.activity_main) as ActivityMainBinding
        initView()
    }

    private fun initView()
    {

        viewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        recycle_user.layoutManager = LinearLayoutManager(this,RecyclerView.VERTICAL,false)





        /**
         * listener
         */
        imageView.setOnClickListener(this)
        imageView_search.setOnClickListener(this)


        setValueToAdapter()

        /**
         * UserName Search
         */
        editText.addTextChangedListener(object :TextWatcher
        {
            override fun afterTextChanged(s: Editable?)
            {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
            {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
            {
                if(s.isNullOrEmpty())
                {
                    hasSearchData(false)
                }else
                {
                    hasSearchData(true)
                }
            }

        })




    }



    /**
     * Has Search Data
     */
    private fun hasSearchData(b: Boolean)
    {
        if(b)
        {
            imageView_search.visibility = VISIBLE
            imageView.setImageResource(R.mipmap.cross)
            imageView.visibility = GONE
        }else
        {
            imageView_search.visibility = GONE
            imageView.visibility = VISIBLE
            imageView.setImageResource(R.mipmap.mic)
            searchValue("")
        }
    }

    override fun onStart()
    {
        super.onStart()

        viewModel?.getAllData()?.observe(this,object :Observer<List<UserName>>
        {
            override fun onChanged(t: List<UserName>?)
            {
                Log.e("DataChange","Change")
                setUsers(t)
            }

        })


    }


    override fun onClick(v: View?)
    {
        when(v?.id)
        {
            R.id.imageView_search ->
            {
                searchValue(editText.text.toString())
            }

            R.id.imageView ->
            {

                if(editText.text.toString().isNullOrBlank())
                {
                    promptSpeechInput()
                }else
                {
                    editText.text = null


                }
            }
        }
    }




    /**
     *
     */
    public fun setUsers(newList:List<UserName>?)
    {

        if(userList==null)
        {
            userList = ArrayList<UserName>()
        }

        userList!!.clear()

        if(newList!=null)
        {
            userList!!.addAll(newList)
        }

        Log.e("DataList",userList.toString());

        setValueToAdapter()


    }


    /**
     *
     */
    fun  setValueToAdapter()
    {
        if(userList==null)
        {
            userList = ArrayList<UserName>()
        }

        if(userList?.size == 0)
        {
            userList!!.add(UserName(0,"","",false))
        }
        adapter = UserAdapter(this,userList!!,this)
        recycle_user.adapter = adapter
        adapter!!.notifyDataSetChanged()
    }




    /**
     *
     */
    private fun searchValue(text: String?)
    {

        adapter?.searchUser(text?:"")

       /* var isExist =viewModel?.isUserExist(text?:"")

        if(!text.isNullOrEmpty())
        {
            var isExist =viewModel?.isUserExist(text?:"")

            Log.e("IsExist",isExist.toString())

            if(!(isExist?:false))
            {

                var msg = "<b>"+text+"</b> name user not found. Please try other name"
                AlertDialog.Builder(this)
                    .setTitle("Not, Found")
                    .setMessage(Html.fromHtml(msg))
                    .setIcon(R.mipmap.search)
                    .setCancelable(false)
                    .setPositiveButton("Ok") { dialog, whichButton ->
                        viewModel?.insertUser(UserName(0,editText.text.toString(),"",true))
                        editText.text = null
                    }.show()
            }
        }*/
    }


    /**
     * Showing google speech input dialog
     */
    private fun promptSpeechInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(
            RecognizerIntent.EXTRA_PROMPT,
            getString(R.string.speech_prompt)
        )
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
            Toast.makeText(
                applicationContext,
                getString(R.string.speech_not_supported),
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode)
        {
            REQ_CODE_SPEECH_INPUT ->
            {
                if(resultCode == Activity.RESULT_OK && data!=null)
                {
                    var value = data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    editText.setText(value.get(0))

                }
            }
        }


    }

    override fun isValueMatch(match: Boolean) {

        if(!match)
        {

            if(editText.text.isNullOrEmpty())
            {
             return
            }

            var msg = "<b>"+editText.text.toString()+"</b> name user not found. Please try other name"
            AlertDialog.Builder(this)
                .setTitle("Not, Found")
                .setMessage(Html.fromHtml(msg))
                .setIcon(R.mipmap.search)
                .setCancelable(false)
                .setPositiveButton("Ok") { dialog, whichButton ->
                    viewModel?.insertUser(UserName(0,editText.text.toString(),"",true))
                    editText.text = null
                }.show()
        }
    }


}
