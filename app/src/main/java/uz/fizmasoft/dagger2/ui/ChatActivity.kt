package uz.fizmasoft.dagger2.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.obsez.android.lib.filechooser.ChooserDialog
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_chat.*
import uz.fizmasoft.dagger2.R
import uz.fizmasoft.dagger2.data.model.Message
import uz.fizmasoft.dagger2.util.ChatRvAdapter
import uz.fizmasoft.dagger2.util.extentions.toEditable
import uz.fizmasoft.dagger2.util.extentions.toast
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

    private fun init() {
        initVM()
        initRV()
        setClickListener()
        loadMessages()
    }

    private fun initVM() {
        chatViewModel = ViewModelProviders.of(this, providerFactory)[ChatViewModel::class.java]
        println(chatViewModel.forTesting())
    }

    private fun initRV() {
        chatRvAdapter = ChatRvAdapter()
        chatRecyclerView.adapter = chatRvAdapter
    }

    private fun setClickListener() {
        btnSend.setOnClickListener {
            if (!TextUtils.isEmpty(sendMessageContent.text)) {
                chatViewModel.insertMessage(getMessage())
                observeInsertingState()
            }
        }
        fileChooser.setOnClickListener {
            openFileChooser()
        }
    }

    private fun openFileChooser() = chooserDialog.apply {
        withChosenListener { path, _ -> filePath = path }
        withOnCancelListener { it.cancel() }
        build()
        show()
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
                        sendMessageContent.text = "".toEditable()
                    }
                    is ChatViewModel.InsertingMessagesState.OnError -> {
                        toast("Error while inserting DB ${it.err.localizedMessage}")
                    }
                }
            })
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
