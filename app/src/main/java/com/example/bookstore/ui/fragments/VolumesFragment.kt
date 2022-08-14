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
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentVolumesBinding
import com.example.bookstore.data.api.dto.toVolume
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.ui.adapters.VolumesAdapter
import com.example.bookstore.viewmodels.VolumesViewModel

class VolumesFragment : Fragment() {

    companion object {
        const val VOLUME_ID = "VOLUME_ID"
    }

    private var _binding: FragmentVolumesBinding? = null
    private val binding get() = _binding!!

    private val vm: VolumesViewModel by viewModels()
    private var isFilter = false

    private lateinit var layoutManager : LinearLayoutManager
    private var adapter : VolumesAdapter? = null
    private var favoritesAdapter : VolumesAdapter? = null

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
        observeInProgress()
        observeIsError()
        observeFavoriteList()
    }

    private fun observeInProgress() {
        vm.repository.isInProgress.observe(viewLifecycleOwner) { isLoading ->
            isLoading.let {
                //binding.volumesPb.visibility = if(it) View.VISIBLE else View.GONE
            }
        }
    }

    private fun observeIsError() {//TODO
        vm.repository.isError.observe(viewLifecycleOwner) { isError ->
            isError.let {

            }
        }
    }

    private fun observeVolumesList(){
        vm.repository.volumesData.observe(viewLifecycleOwner) { volumes ->
            volumes.let {
                adapter = VolumesAdapter(ArrayList(volumes), clickListener())
                binding.volumesRv.adapter = adapter
                binding.volumesPb.visibility = View.GONE
                binding.volumesFilterCl.setOnClickListener(filterOnclickListener())
                isFilter = false
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
        layoutManager = LinearLayoutManager(context)
        binding.volumesRv.layoutManager = layoutManager
    }

    private fun clickListener() : VolumesAdapter.OnClickListener {
        return VolumesAdapter.OnClickListener { id ->
            val bundle = Bundle()
            bundle.putString(VOLUME_ID, id)

            findNavController().navigate(R.id.action_VolumesFragment_to_VolumeDetailFragment, bundle)
        }
    }

    private fun observeFavoriteList() {
        vm.repository.favoriteData.observe(viewLifecycleOwner) { favorites ->
            favorites.let {
                binding.volumesFilterCl.visibility = if(it != null && it.isNotEmpty()) View.VISIBLE else View.GONE
                val list = convertEntityList(favorites)

                if(favoritesAdapter == null)
                    favoritesAdapter = VolumesAdapter(list, clickListener())
                else
                    favoritesAdapter!!.setUpData(list)
            }
        }
    }

    private fun convertEntityList(list : List<FavoriteEntity>) : ArrayList<VolumeDto.Volume> {
        val listConverted = ArrayList<VolumeDto.Volume>()
        list.forEach { listConverted.add(it.toVolume())}

        return listConverted
    }

    private fun filterOnclickListener() : View.OnClickListener {
        return View.OnClickListener {
            isFilter = !isFilter
            val imageResource = if(isFilter) R.drawable.ic_filter else R.drawable.ic_filter_outlined

            binding.volumesFilterIv.setImageDrawable(ContextCompat.getDrawable(requireContext(), imageResource))
            binding.volumesRv.adapter = if(isFilter) favoritesAdapter else adapter
        }
    }
}