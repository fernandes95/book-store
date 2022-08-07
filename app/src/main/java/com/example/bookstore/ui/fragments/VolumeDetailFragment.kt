package com.example.bookstore.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentVolumeDetailBinding
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.room.FavoritesApplication
import com.example.bookstore.ui.fragments.VolumesFragment.Companion.VOLUME_ID
import com.example.bookstore.viewmodels.VolumeDetailViewModel
import com.example.bookstore.viewmodels.VolumeDetailViewModelFactory

class VolumeDetailFragment : Fragment() {

    private var _binding: FragmentVolumeDetailBinding? = null
    private val binding get() = _binding!!
    private val vm: VolumeDetailViewModel by viewModels {
        VolumeDetailViewModelFactory((activity?.application as FavoritesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVolumeDetailBinding.inflate(inflater, container, false)

        updateUi()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun favoriteOnclickListener() : View.OnClickListener {
        return View.OnClickListener {

            if(vm.isFavorite)
                vm.delete()
            else
                vm.insert()

            setFavoriteImage()
        }
    }

    private fun setFavoriteImage(){
        var imageResource = if(vm.isFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border

        binding.volumeDetailFavoriteIv.setImageDrawable(ContextCompat.getDrawable(context!!, imageResource))
    }

    private fun updateUi(){
        binding.volumeDetailFavoriteIv.setOnClickListener(favoriteOnclickListener())
        val volumeId = arguments?.getString(VOLUME_ID).toString()
        vm?.getVolume(volumeId)?.observe(viewLifecycleOwner) { volume ->

            //TODO ADD ERROR DRAWABLE
            if(!volume?.volumeInfo?.imageLinks?.thumbnail.isNullOrEmpty()) {
                Glide.with(this)
                    .load(volume?.volumeInfo?.imageLinks?.thumbnail)
                    .into(binding.volumeDetailThumbIv)
            }

            setText(volume)
            binding.volumeDetailPb.visibility = View.GONE
            binding.volumeDetailContentCl.visibility = View.VISIBLE
        }
    }

    private fun setText(volume: VolumeDto.Volume){
        val authorsSize = volume.volumeInfo?.authors?.size
        binding.volumeDetailAuthorTitleTv.text =
            if(authorsSize!! > 1)
                "Authors"
            else
                "Author"

        binding.volumeDetailAuthorContentTv.text =
            if(authorsSize!! > 1)
                volume.volumeInfo?.authors?.joinToString(separator = ", ")
            else
                volume.volumeInfo?.authors?.joinToString()

        //CONTENT
        binding.volumeDetailTitleContentTv.text = volume.volumeInfo?.title

        if(volume.volumeInfo?.description != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.volumeDetailDescriptionContentTv.text =
                    Html.fromHtml(volume.volumeInfo?.description, Html.FROM_HTML_MODE_LEGACY)
            } else {
                binding.volumeDetailDescriptionContentTv.text =
                    Html.fromHtml(volume.volumeInfo?.description)
            }
        }

        val isForSale = volume.saleInfo?.saleability == "FOR_SALE"//TODO CREATE ENUM FOR saleability
        binding.volumeDetailBuyBtn.isEnabled = isForSale
        binding.volumeDetailBuyBtn.setOnClickListener {
            if(isForSale) {
                val openURL = Intent(Intent.ACTION_VIEW)
                openURL.data = Uri.parse(volume.saleInfo?.buyLink)
                startActivity(openURL)
            }
        }
    }
}