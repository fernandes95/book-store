package com.example.bookstore.viewmodels

import androidx.lifecycle.ViewModel
import com.example.bookstore.dto.Volume

class FirstViewModel : ViewModel() {

    lateinit var liveDataList: MutableList<List<Volume.Volume>>

    fun getList() : MutableList<List<Volume.Volume>> {
        return liveDataList
    }
}