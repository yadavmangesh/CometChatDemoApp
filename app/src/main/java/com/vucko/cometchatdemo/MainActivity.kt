package com.vucko.cometchatdemo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.core.GroupsRequest
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.User

class MainActivity : AppCompatActivity() {

    private val CREATE_GROUP = 1

    val TAG = "MainActivity"
    val apiKey = "40699368e14447dfe1f45d5168fae2850473ce7e"
    val appID = "18299f79cc590e"

    lateinit var createGroupButton: Button
    lateinit var groupsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createGroupButton = findViewById(R.id.createGroupButton)
        groupsRecyclerView = findViewById(R.id.groupsRecyclerView)
        createGroupButton.setOnClickListener { openCreateGroupScreen() }
        initCometChat()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CREATE_GROUP) {
            refreshGroupList()
        }
    }

    private fun refreshGroupList() {
        var groupRequest: GroupsRequest? = GroupsRequest.GroupsRequestBuilder().build()

        groupRequest?.fetchNext(object : CometChat.CallbackListener<List<Group>>() {
            override fun onSuccess(p0: List<Group>?) {
                updateUI(p0)
            }

            override fun onError(p0: CometChatException?) {
                Toast.makeText(this@MainActivity, p0?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateUI(groups: List<Group>?) {
        val adapter = GroupsAdapter(groups, this)
        groupsRecyclerView.adapter = adapter
    }

    private fun openCreateGroupScreen() {
        val intent = Intent(this, CreateGroupActivity::class.java)
        startActivityForResult(intent, CREATE_GROUP)
    }

    private fun initCometChat() {
        CometChat.init(this, appID, object : CometChat.CallbackListener<String>() {
            override fun onSuccess(p0: String?) {
                Log.d(TAG, "Initialization completed successfully")
                loginUser()
            }

            override fun onError(p0: CometChatException?) {
                Log.d(TAG, "Initialization failed with exception: " + p0?.message)
            }
        })
    }

    private fun loginUser() {
        val UID = "SUPERHERO5"
        CometChat.login(UID, apiKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User?) {
                saveUser(user)
                refreshGroupList()
            }

            override fun onError(p0: CometChatException?) {
                Log.d(TAG, "Login failed with exception: " + p0?.message)
            }

        })
    }

    private fun saveUser(user: User?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences
            .edit()
            .putString("user_id", user?.uid)
            .apply()
    }
}
