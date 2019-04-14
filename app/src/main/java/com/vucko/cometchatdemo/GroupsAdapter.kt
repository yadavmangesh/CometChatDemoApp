package com.vucko.cometchatdemo

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Group
import kotlinx.android.synthetic.main.group_layout.view.*

class GroupsAdapter(val groups: List<Group>?, val context: Context) : RecyclerView.Adapter<GroupViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.group_layout, parent, false))
    }

    override fun getItemCount(): Int {
        return groups!!.size
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.groupNameTextView.text = groups!!.get(position).name
        val group = groups[position]
        if (group.isJoined) {
            holder.joinedTextView.visibility = View.VISIBLE
        } else {
            holder.joinedTextView.visibility = View.INVISIBLE
        }
        holder.container.setOnClickListener {
            if (group.isJoined) {
                goToGroupScreen(group)
            } else {
                attemptJoinGroup(group)
            }
        }
    }

    private fun attemptJoinGroup(group: Group) {
        CometChat.joinGroup(group.guid, group.groupType, group.password, object : CometChat.CallbackListener<Group>() {
            override fun onSuccess(p0: Group?) {
                groups!!.forEach {
                    if (it.guid == group.guid) {
                        it.setHasJoined(true)
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onError(p0: CometChatException?) {
                Toast.makeText(context, p0?.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun goToGroupScreen(group: Group) {
        val intent = Intent(context, GroupActivity::class.java)
        intent.putExtra("group_id", group.guid)
        context.startActivity(intent)
    }

}

class GroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val groupNameTextView: TextView = itemView.groupNameTextView
    val container: ConstraintLayout = itemView.container
    val joinedTextView: TextView = itemView.joinedTextView
}