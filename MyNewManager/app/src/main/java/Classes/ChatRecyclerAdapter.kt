package Classes

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynewmanager.R
import kotlinx.serialization.json.Json.Default.context

class ChatRecyclerAdapter( val smsList :ArrayList<textMessage>, var userOpen:String):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    internal val VIEW_TYPE_Send = 1
    internal val VIEW_TYPE_Receive = 2

    private inner class ViewHolderSend internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var texto: TextView

        init {
            texto = itemView.findViewById(R.id.SendText)
        }

        internal fun bind(position: Int) {

            texto.text = smsList[position].texto
        }
    }

    private inner class ViewHolderReceive internal constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        internal var texto: TextView
        internal var nomeSend: TextView

        init {
            texto = itemView.findViewById(R.id.receiveText)
            nomeSend = itemView.findViewById(R.id.namePersonSend)
        }

        internal fun bind(position: Int) {

            texto.text = smsList[position].texto
            nomeSend.text = smsList[position].sentFrom
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_Send) {
            ViewHolderSend(
                LayoutInflater.from(parent.context).inflate(R.layout.item_sms_send, parent, false)
            )
        } else ViewHolderReceive(
            LayoutInflater.from(parent.context).inflate(R.layout.item_sms_receive, parent, false)
        ) //if it's not VIEW_TYPE_ONE then its VIEW_TYPE_TWO
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (smsList[position].sentFrom == userOpen) { // put your condition, according to your requirements
            (holder as ViewHolderSend).bind(position)
        } else {
            (holder as ViewHolderReceive).bind(position)
        }
    }

    override fun getItemViewType(position: Int): Int {

        return if (smsList[position].sentFrom == userOpen) { // put your condition, according to your requirements
            VIEW_TYPE_Send
        } else VIEW_TYPE_Receive
    }

    override fun getItemCount(): Int {
        return smsList.size
    }
}


