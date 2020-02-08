package uz.fizmasoft.dagger2.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Completable
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

    private val _deletingAllMessages = MutableLiveData<DeletingAllMessagesState>()
    val deletingAllMessages: LiveData<DeletingAllMessagesState>
        get() = _deletingAllMessages


    fun loadMessages() {
        val disposable = repository.fetchAllMessage()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { _loadMessages.value = LoadingMessagesState.OnIdle }
            .subscribe(
                { _loadMessages.value = LoadingMessagesState.OnSuccess(it) },
                { _loadMessages.value = LoadingMessagesState.OnError(it) }
            )
        compositeDisposable.add(disposable)
    }

    fun insertMessage(message: Message) {
        repository.insertMessage(message)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { compositeDisposable.add(it) }
            .doOnSuccess { _insertMessage.value = InsertingMessagesState.OnSuccess(it) }
            .doOnError { _insertMessage.value = InsertingMessagesState.OnError(it) }
            .doFinally { _insertMessage.value = InsertingMessagesState.OnIdle  }
            .subscribe()

    }

    fun deleteAllMessages() {

        val disposable = Completable.fromAction { repository.deleteAllMessage() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { _deletingAllMessages.value = DeletingAllMessagesState.OnIdle  }
            .subscribe(
                { _deletingAllMessages.value = DeletingAllMessagesState.OnSuccess },
                { _deletingAllMessages.value = DeletingAllMessagesState.OnError(it) }
            )

        compositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    sealed class LoadingMessagesState {
        data class OnSuccess(val messages: List<Message>) : LoadingMessagesState()
        data class OnError(val err: Throwable) : LoadingMessagesState()
        object OnIdle : LoadingMessagesState()
    }

    sealed class InsertingMessagesState {
        data class OnSuccess(val id: Long) : InsertingMessagesState()
        data class OnError(val err: Throwable) : InsertingMessagesState()
        object OnIdle : InsertingMessagesState()
    }

    sealed class DeletingAllMessagesState {
        object OnSuccess : DeletingAllMessagesState()
        data class OnError(val err: Throwable) : DeletingAllMessagesState()
        object OnIdle : DeletingAllMessagesState()
    }

}
