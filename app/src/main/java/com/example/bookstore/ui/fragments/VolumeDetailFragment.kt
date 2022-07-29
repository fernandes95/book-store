package com.example.bookstore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.bookstore.databinding.FragmentVolumeDetailBinding
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.ui.fragments.VolumesFragment.Companion.VOLUME_ID
import com.example.bookstore.viewmodels.VolumeDetailViewModel

class VolumeDetailFragment : Fragment() {

    private var _binding: FragmentVolumeDetailBinding? = null
    private val binding get() = _binding!!
    private var vm : VolumeDetailViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVolumeDetailBinding.inflate(inflater, container, false)
        vm = ViewModelProvider(this)[VolumeDetailViewModel::class.java]
        updateUi()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(){
        val volumeId = arguments?.getString(VOLUME_ID).toString()

        vm?.getVolume(volumeId)?.observe(viewLifecycleOwner) { volume ->

            //TODO ADD ERROR DRAWABLE
            if(!volume?.volumeInfo?.imageLinks?.thumbnail.isNullOrEmpty()) {
                Glide.with(this)
                    .load(volume?.volumeInfo?.imageLinks?.thumbnail)
                    .into(binding.volumeDetailThumbIv)
            }

            setText(volume)
        }
    }

    private fun setText(volume: VolumeDto.Volume){
        val authorsSize = volume.volumeInfo?.authors?.size
        val authorTitle =
            if(authorsSize!! > 1)
                "Authors"
            else
                "Author"

        val authors =
            if(authorsSize!! > 1)
                volume.volumeInfo?.authors?.joinToString(separator = ", ")
            else
                volume.volumeInfo?.authors?.toString()

        //TITLES
        binding.volumeDetailTitleTv.text = "Title"
        binding.volumeDetailAuthorTitleTv.text = authorTitle
        binding.volumeDetailDescriptionTitleTv.text = "Description"
        binding.volumeDetailBuyBtn.text = "Buy"

        //CONTENT
        binding.volumeDetailTitleContentTv.text = volume.volumeInfo?.title
        binding.volumeDetailAuthorContentTv.text = authors
        binding.volumeDetailDescriptionContentTv.text = volume.volumeInfo?.description
        binding.volumeDetailBuyBtn.isEnabled = volume.saleInfo?.saleability == "FOR_SALE"//TODO CREATE ENUM FOR saleability
    }
}