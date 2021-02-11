package Classes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewmanager.R
import java.util.ArrayList

class ChatRoomRecyclerAdapter (val chatRoomList: ArrayList<Chat>,var clickListener: OnChatItemClickListener):
    RecyclerView.Adapter<ChatRoomRecyclerAdapter.ViewHolderChatRoom >() {

        class ViewHolderChatRoom (item: View) : RecyclerView.ViewHolder(item){
            var nome: TextView = item.findViewById(R.id.txtNomeSala)

            fun initialize(item : Chat, action:OnChatItemClickListener){
                nome.text = item.nome

                itemView.setOnClickListener(){
                    action.onItemClick(item,adapterPosition)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderChatRoom {

            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_room,parent,false)

            return ViewHolderChatRoom(view)
        }

        override fun getItemCount(): Int {
            return chatRoomList.size
        }

        override fun onBindViewHolder(holder: ViewHolderChatRoom, position: Int) {
            holder.initialize(chatRoomList.get(position),clickListener)
        }

}
interface OnChatItemClickListener{
    fun onItemClick(item: Chat, position: Int)
}