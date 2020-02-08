package uz.fizmasoft.dagger2.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message_me.view.*
import uz.fizmasoft.dagger2.R
import uz.fizmasoft.dagger2.data.model.Message
import java.io.File

class ChatRvAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Message>() {

        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.sendDate == newItem.sendDate

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean =
            oldItem.messageContent == newItem.messageContent

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ChatItemViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_message_me,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatItemViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount() = differ.currentList.size


    fun submitList(list: List<Message>) = differ.submitList(list)


    inner class ChatItemViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Message): Unit = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            messageContent.text = item.messageContent
            send_time.text = item.sendDate

            checkAttachedFile(item, itemView)

        }

        private fun checkAttachedFile(item: Message, itemView: View)  {

            if (item.filePath != null) {
                //File attached
                itemView.attachedFileContainer.visibility = View.VISIBLE
                val file = File(item.filePath)

                val fileName = "${file.name}.${file.extension}"

                itemView.file_name.text = fileName

                itemView.file_status.text = if (file.exists())
                    "File Status: Exists"
                else
                    "File Status: Not Exists"

            }else {
                //File not attached
                itemView.attachedFileContainer.visibility = View.GONE
            }


        }


    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Message)
    }
}
