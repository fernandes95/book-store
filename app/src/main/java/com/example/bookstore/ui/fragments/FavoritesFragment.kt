package com.example.bookstore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstore.MainActivity
import com.example.bookstore.R
import com.example.bookstore.data.models.toVolume
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.databinding.FragmentFavoritesBinding
import com.example.bookstore.ui.adapters.VolumesAdapter
import com.example.bookstore.utils.BUNDLE_VOLUME_ID
import com.example.bookstore.viewmodels.FavoritesViewModel

class FavoritesFragment : Fragment() {
    private val vm: FavoritesViewModel by viewModels()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var layoutManager : LinearLayoutManager
    private var adapter : VolumesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        updateUi()
        observeLiveData()

        return binding.root
    }

    private fun observeLiveData() {
        observeVolumesList()
    }

    private fun observeVolumesList(){
        vm.repository.favoriteData.observe(viewLifecycleOwner) { volumes ->
            volumes.let {
                val list = convertEntityList(volumes)
                adapter = VolumesAdapter(list, clickListener())

                if(adapter != binding.favoritesRv.adapter)
                    binding.favoritesRv.adapter = adapter
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
        (activity as MainActivity).showNavBar(true)
    }

    private fun updateUi(){
        layoutManager = LinearLayoutManager(context)
        binding.favoritesRv.layoutManager = layoutManager
    }


    private fun clickListener() : VolumesAdapter.OnClickListener {
        return VolumesAdapter.OnClickListener { id ->
            val bundle = Bundle()
            bundle.putString(BUNDLE_VOLUME_ID, id)

            findNavController().navigate(R.id.action_favoritesFragment_to_VolumeDetailFragment, bundle)
        }
    }

    private fun convertEntityList(list : List<FavoriteEntity>) : ArrayList<VolumeDto.Volume> {
        val listConverted = ArrayList<VolumeDto.Volume>()
        list.forEach { listConverted.add(it.toVolume())}

        return listConverted
    }
}