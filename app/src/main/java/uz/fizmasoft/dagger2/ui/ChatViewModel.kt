package uz.fizmasoft.dagger2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import uz.fizmasoft.dagger2.data.ChatRepository
import uz.fizmasoft.dagger2.data.model.Message
import javax.inject.Inject

class ChatViewModel @Inject constructor(private val repository: ChatRepository) :
    ViewModel() {


    private val compositeDisposable = CompositeDisposable()

    private val _loadMessages = MutableLiveData<LoadingMessagesState>()
    val loadMessage: LiveData<LoadingMessagesState>
        get() = _loadMessages

    private val _insertMessage = MutableLiveData<InsertingMessagesState>()
    val insertMessage: LiveData<InsertingMessagesState>
        get() = _insertMessage


    fun loadMessages() {
        val disposable = repository.fetchAllMessage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _loadMessages.value = LoadingMessagesState.OnSuccess(it) },
                { _loadMessages.value = LoadingMessagesState.OnError(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun insertMessage(message: Message) {
        val disposable = repository.insertMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _insertMessage.value = InsertingMessagesState.OnSuccess(it) },
                { _insertMessage.value = InsertingMessagesState.OnError(it) }
            )

        compositeDisposable.add(disposable)
    }

    fun forTesting() = "This message is coming from ChatViewModel"

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class LoadingMessagesState {
        data class OnSuccess(val messages: List<Message>) : LoadingMessagesState()
        data class OnError(val err: Throwable) : LoadingMessagesState()
    }

    sealed class InsertingMessagesState {
        data class OnSuccess(val id: Long) : InsertingMessagesState()
        data class OnError(val err: Throwable) : InsertingMessagesState()
    }

}
