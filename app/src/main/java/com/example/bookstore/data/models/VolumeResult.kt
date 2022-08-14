package com.example.bookstore.data.models

import com.example.bookstore.data.models.dto.VolumeDto
import retrofit2.Call

data class VolumeResult(val call: Call<VolumeDto.Volumes>)