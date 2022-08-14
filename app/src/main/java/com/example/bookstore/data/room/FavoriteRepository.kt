package com.example.bookstore.data.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookstore.VolumesApplication
import com.example.bookstore.utils.subscribeOnBackground
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class FavoriteRepository {

    private val _data by lazy { MutableLiveData<List<FavoriteEntity>>() }
    val data: LiveData<List<FavoriteEntity>>
        get() = _data

    private val _isInProgress by lazy { MutableLiveData<Boolean>() }
    val isInProgress: LiveData<Boolean>
        get() = _isInProgress

    private val _isError by lazy { MutableLiveData<Boolean>() }
    val isError: LiveData<Boolean>
        get() = _isError


    fun insert(volume: FavoriteEntity) {
        subscribeOnBackground {
            VolumesApplication.database.favDao().insert(volume)
        }
    }

    fun delete(favorite: FavoriteEntity) {
        subscribeOnBackground {
            VolumesApplication.database.favDao().delete(favorite)
        }
    }

    private fun getAllFavorites(): Disposable {
        return VolumesApplication.database.favDao()
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dataEntityList ->
                    _isInProgress.postValue(true)
                    if (dataEntityList != null && dataEntityList.isNotEmpty()) {
                        _isError.postValue(false)
                        _data.postValue(dataEntityList)
                    } else {
                        _data.postValue(emptyList())
                    }
                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getAllFavorites()", "Database error: ${it.message}")
                    _isError.postValue(true)
                    _isInProgress.postValue(false)
                }
            )
    }

    fun fetchFavoritesFromDatabase(): Disposable = getAllFavorites()
}