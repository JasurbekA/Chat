package uz.fizmasoft.dagger2.ui

import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.obsez.android.lib.filechooser.ChooserDialog
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import uz.fizmasoft.dagger2.R
import uz.fizmasoft.dagger2.data.model.Message
import uz.fizmasoft.dagger2.util.ChatRvAdapter
import uz.fizmasoft.dagger2.util.extentions.toEditable
import uz.fizmasoft.dagger2.util.extentions.toast
import java.io.File
import javax.inject.Inject

class ChatActivity : DaggerAppCompatActivity() {

    private lateinit var chatViewModel: ChatViewModel

    @Inject
    lateinit var providerFactory: ViewModelProvider.Factory

    private lateinit var chatRvAdapter: ChatRvAdapter

    @Inject
    lateinit var chooserDialog: ChooserDialog
    private var filePath: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        init()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_activity_chat, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAllMessages){
            chatViewModel.deleteAllMessages()
            observeMessagesDeleteState()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeMessagesDeleteState() {
        chatViewModel.deletingAllMessages.observe(
            this,
            Observer {
                when (it) {
                    is ChatViewModel.DeletingAllMessagesState.OnSuccess -> {
                        toast("All messages have been deleted")
                    }
                    is ChatViewModel.DeletingAllMessagesState.OnError -> {
                        toast("Error while deleting messages ${it.err.localizedMessage}")
                    }
                }
            }
        )
    }

    private fun init() {
        initVM()
        initRV()
        setClickListener()
        loadMessages()
    }

    private fun initVM() {
        chatViewModel = ViewModelProvider(
            this,
            providerFactory
        )[ChatViewModel::class.java]
    }

    private fun initRV() {
        chatRvAdapter = ChatRvAdapter()
        chatRecyclerView.adapter = chatRvAdapter
    }

    private fun setClickListener() {
        btnSend.setOnClickListener { sendBtnClick() }
        fileChooser.setOnClickListener { openFileChooser() }
    }

    private fun sendBtnClick() {
        if (!TextUtils.isEmpty(sendMessageContent.text)) {
            chatViewModel.insertMessage(getMessage())
            observeInsertingState()
        }
    }


    private fun openFileChooser() = chooserDialog.apply {
        withChosenListener { path, file -> onFileChosen(path, file) }
        withOnCancelListener { it.cancel() }
        build()
        show()
    }

    private fun onFileChosen(path: String, file: File) {
        filePath = path
        chosen_file_container.visibility = View.VISIBLE
        val fileName = "${file.name}.${file.extension}"
        chosenFileName.text = fileName
        removeChosenFile.setOnClickListener {
            chosen_file_container.visibility = View.GONE
            filePath = null
        }
    }


    private fun loadMessages() {
        chatViewModel.loadMessages()
        observeMessages()
    }

    private fun observeMessages() {
        chatViewModel.loadMessage.observe(
            this,
            Observer {
                when (it) {
                    is ChatViewModel.LoadingMessagesState.OnSuccess -> {
                        chatRvAdapter.submitList(it.messages)
                    }
                    is ChatViewModel.LoadingMessagesState.OnError -> {
                        toast("Loading messages error ${it.err.localizedMessage}")
                    }
                }
            }
        )
    }

    private fun observeInsertingState() {
        chatViewModel.insertMessage.observe(
            this,
            Observer {
                when (it) {
                    is ChatViewModel.InsertingMessagesState.OnSuccess -> {
                        onInsertSuccess()
                    }
                    is ChatViewModel.InsertingMessagesState.OnError -> {
                        toast("Error while inserting DB ${it.err.localizedMessage}")
                    }
                }
            })
    }

    private fun onInsertSuccess() {
        //Clear current message state elements
        sendMessageContent.text = "".toEditable()
        filePath?.let { chosen_file_container.visibility = View.GONE }
        filePath = null
        //scroll to the last message
        println("checking the item count ${chatRvAdapter.itemCount}")
        chatRecyclerView.scrollToPosition(chatRvAdapter.itemCount)
    }


    private fun getMessage(): Message {
        val senderId: Long = 1
        val recipientId: Long = 2
        val messageContent = sendMessageContent.text.toString()
        return Message(
            senderID = senderId,
            recipientID = recipientId,
            messageContent = messageContent,
            filePath = filePath
        )
    }
}
