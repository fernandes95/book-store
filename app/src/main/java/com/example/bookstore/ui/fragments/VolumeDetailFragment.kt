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
import com.example.bookstore.MainActivity
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentVolumeDetailBinding
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.utils.BUNDLE_VOLUME_ID
import com.example.bookstore.viewmodels.VolumeDetailViewModel

class VolumeDetailFragment : Fragment() {
    private val vm: VolumeDetailViewModel by viewModels()

    private var _binding: FragmentVolumeDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentVolumeDetailBinding.inflate(inflater, container, false)

        updateUi()
        observeLiveData()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeFavoriteList()
    }

    private fun observeInProgress() {//TODO
        vm.repository.isInProgress.observe(viewLifecycleOwner) { isLoading ->
            isLoading.let {

            }
        }
    }

    private fun observeIsError() {//TODO
        vm.repository.isError.observe(viewLifecycleOwner) { isError ->
            isError.let {

            }
        }
    }

    private fun observeFavoriteList() {
        vm.repository.favoriteData.observe(viewLifecycleOwner) { list ->
            list.let {
                if(!list.any()) return@let

                val favItems = list.filter { item -> item.id == vm.volumeId }

                //assuring that it only gives a value if value not given before hand
                if(vm.isFavorite.value == null)
                    vm.isFavorite.value = favItems.any()

                vm.favorite = if(favItems.any()) favItems.first() else null
            }
        }
    }

    private fun favoriteOnclickListener() : View.OnClickListener {
        return View.OnClickListener {
            vm.setFavorite()
        }
    }

    private fun setFavoriteImage(isFav : Boolean?){
        val imageResource = if(isFav == true) R.drawable.ic_favorite else R.drawable.ic_favorite_border
        binding.volumeDetailFavoriteIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), imageResource))
    }

    private fun updateUi(){
        (activity as MainActivity).showNavBar(false)

        binding.volumeDetailFavoriteIv.setOnClickListener(favoriteOnclickListener())

        val volumeId = arguments?.getString(BUNDLE_VOLUME_ID).toString()
        vm.getVolume(volumeId)?.observe(viewLifecycleOwner) { volume ->
            vm.selectedVolume = volume

            Glide.with(this)
                .load(volume?.volumeInfo?.imageLinks?.thumbnail)
                .error(R.drawable.no_photo)
                .into(binding.volumeDetailThumbIv)

            setVisibility(volume)
            setText(volume)
            binding.volumeDetailPb.visibility = View.GONE
            binding.volumeDetailContentCl.visibility = View.VISIBLE

            vm.isFavorite.observe(viewLifecycleOwner){
                setFavoriteImage(it)
            }
        }

        vm.isLoading.observe(viewLifecycleOwner){
            binding.volumeDetailPb.visibility = if(it) View.VISIBLE else View.GONE
        }
    }

    private fun setVisibility(volume: VolumeDto.Volume){
        binding.volumeDetailAuthorTitleTv.visibility = if(volume.volumeInfo?.authors != null) View.VISIBLE else View.GONE
        binding.volumeDetailAuthorContentTv.visibility = if(volume.volumeInfo?.authors != null) View.VISIBLE else View.GONE

        binding.volumeDetailDescriptionTitleTv.visibility = if(volume.volumeInfo?.description != null) View.VISIBLE else View.GONE
        binding.volumeDetailDescriptionContentTv.visibility = if(volume.volumeInfo?.description != null) View.VISIBLE else View.GONE
    }

    private fun setText(volume: VolumeDto.Volume){
        val authorsSize = volume.volumeInfo?.authors?.size
        if(authorsSize != null) {
            binding.volumeDetailAuthorTitleTv.text =
                if (authorsSize > 1)
                    "Authors"
                else
                    "Author"

            binding.volumeDetailAuthorContentTv.text =
                if (authorsSize > 1)
                    volume.volumeInfo.authors.joinToString(separator = ", ")
                else
                    volume.volumeInfo.authors.joinToString()
        }


        //CONTENT
        binding.volumeDetailTitleContentTv.text = volume.volumeInfo?.title

        if(volume.volumeInfo?.description != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                binding.volumeDetailDescriptionContentTv.text =
                    Html.fromHtml(volume.volumeInfo.description, Html.FROM_HTML_MODE_LEGACY)
            } else {
                binding.volumeDetailDescriptionContentTv.text =
                    Html.fromHtml(volume.volumeInfo.description)
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