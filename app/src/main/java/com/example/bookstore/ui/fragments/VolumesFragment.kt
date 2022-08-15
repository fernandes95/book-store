package com.example.bookstore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.data.models.toVolume
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.databinding.FragmentVolumesBinding
import com.example.bookstore.ui.adapters.VolumesAdapter
import com.example.bookstore.utils.API_MAX_RESULTS
import com.example.bookstore.utils.BUNDLE_VOLUME_ID
import com.example.bookstore.viewmodels.VolumesViewModel

class VolumesFragment : Fragment() {
    private val vm: VolumesViewModel by viewModels()

    private var _binding: FragmentVolumesBinding? = null
    private val binding get() = _binding!!


    private lateinit var layoutManager : LinearLayoutManager
    private var adapter : VolumesAdapter? = null
    private var favoritesAdapter : VolumesAdapter? = null

    private var totalItemCount : Int = 0
    private var lastVisibleItem : Int = 0
    private var firstVisibleItem : Int = 0
    private var isLoadingMore : Boolean = false
    private var volumesLimit : Boolean = false
    private var isFilter = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVolumesBinding.inflate(inflater, container, false)

        updateUi()
        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        observeVolumesList()
        observeFavoriteList()
    }

    private fun observeVolumesList(){
        vm.repository.volumesData.observe(viewLifecycleOwner) { volumes ->
            volumes.let {

                if(adapter == null)
                    adapter = VolumesAdapter(ArrayList(volumes), clickListener())
                else if(isLoadingMore)
                    adapter!!.addData(volumes)

                if(binding.volumesRv.adapter == null) {
                    binding.volumesRv.adapter = adapter

                    if(firstVisibleItem != 0)
                        binding.volumesRv.layoutManager?.scrollToPosition(firstVisibleItem)
                }

                binding.volumesPb.visibility = View.GONE
                binding.volumesBottomPb.visibility = View.GONE
                isLoadingMore = false
                volumesLimit = volumes.count() < API_MAX_RESULTS.toInt()
            }
        }
    }

    private fun observeFavoriteList() {
        vm.repository.favoriteData.observe(viewLifecycleOwner) { favorites ->
            favorites.let {
                binding.volumesFilterCl.visibility = if(it != null && it.isNotEmpty()) View.VISIBLE else View.GONE
                val list = convertEntityList(favorites)
                favoritesAdapter = VolumesAdapter(list, clickListener())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        vm.getFavData()
    }

    private fun updateUi(){
        setFilterUi()
        layoutManager = LinearLayoutManager(context)
        binding.volumesRv.layoutManager = layoutManager
        recyclerScrollListener()
        binding.volumesFilterCl.setOnClickListener(filterOnclickListener())
    }

    private fun recyclerScrollListener() {
        binding.volumesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {
                if(isFilter || volumesLimit) return

                totalItemCount = binding.volumesRv.layoutManager?.itemCount!!
                lastVisibleItem = layoutManager.findLastVisibleItemPosition().plus(1)
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

                if(lastVisibleItem == totalItemCount && !isLoadingMore){
                    isLoadingMore = true
                    binding.volumesBottomPb.visibility = View.VISIBLE
                    vm.getMoreVolumes(totalItemCount)
                }
            }
        })
    }

    private fun clickListener() : VolumesAdapter.OnClickListener {
        return VolumesAdapter.OnClickListener { id ->
            val bundle = Bundle()
            bundle.putString(BUNDLE_VOLUME_ID, id)

            findNavController().navigate(R.id.action_VolumesFragment_to_VolumeDetailFragment, bundle)
        }
    }

    private fun convertEntityList(list : List<FavoriteEntity>) : ArrayList<VolumeDto.Volume> {
        val listConverted = ArrayList<VolumeDto.Volume>()
        list.forEach { listConverted.add(it.toVolume())}

        return listConverted
    }

    private fun filterOnclickListener() : View.OnClickListener {
        return View.OnClickListener {
            if(binding.volumesPb.visibility == View.VISIBLE)return@OnClickListener

            isFilter = !isFilter
            setFilterUi()
        }
    }

    private fun setFilterUi(){
        val imageResource = if(isFilter) R.drawable.ic_filter else R.drawable.ic_filter_outlined
        binding.volumesFilterIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), imageResource))

        if(isFilter && favoritesAdapter == null || !isFilter && adapter == null) return

        binding.volumesRv.adapter = if(isFilter) favoritesAdapter else adapter

        if(!isFilter && firstVisibleItem != 0)
            binding.volumesRv.layoutManager?.scrollToPosition(firstVisibleItem)
    }
}