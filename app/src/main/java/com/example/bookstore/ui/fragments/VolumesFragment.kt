package com.example.bookstore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentVolumesBinding
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.room.FavoritesApplication
import com.example.bookstore.room.toVolume
import com.example.bookstore.ui.adapters.VolumesAdapter
import com.example.bookstore.viewmodels.VolumesViewModel
import com.example.bookstore.viewmodels.VolumesViewModelFactory

class VolumesFragment : Fragment() {

    private var data = MutableLiveData<List<VolumeDto.Volume>>()
    private var favData = MutableLiveData<List<VolumeDto.Volume>>()
    private var _binding: FragmentVolumesBinding? = null
    private val vm: VolumesViewModel by viewModels {
        VolumesViewModelFactory((activity?.application as FavoritesApplication).repository)
    }

    private val binding get() = _binding!!
    private lateinit var layoutManager : LinearLayoutManager
    private var mainAdapter : VolumesAdapter? = null
    private var favAdapter : VolumesAdapter? = null

    private var loading = false
    private var isFilter = false
    private var totalItemCount : Int? = null
    private var lastVisibleItem : Int? = null

    companion object {
        const val VOLUME_ID = "VOLUME_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVolumesBinding.inflate(inflater, container, false)

        updateUi()
        populateList()

        vm.favorites.observe(viewLifecycleOwner, Observer { favorites ->
            if(!favorites.any()) return@Observer
            
            var listConverted = ArrayList<VolumeDto.Volume>()
            favorites.forEach {
                listConverted.add(it.toVolume())
            }

            if(listConverted.any()) {
                favData.value = listConverted.toList()

                favAdapter = context?.let { VolumesAdapter(it, favData, clickListener()) }
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(){
        layoutManager = LinearLayoutManager(context)
        binding.volumesRv.layoutManager = layoutManager

        if(favData.value != null)
            binding.volumesFilterCl.visibility = if(favData.value?.any()!!) View.VISIBLE else View.GONE
        else
            binding.volumesFilterCl.visibility = View.GONE
    }

    private fun populateList(){
        vm?.getListData()?.observe(viewLifecycleOwner) { list ->
            data.value = list
            mainAdapter = context?.let { VolumesAdapter(it, data, clickListener()) }
            recyclerScrollListener()
            binding.volumesRv.adapter = mainAdapter
            binding.volumesPb.visibility = View.GONE

            binding.volumesFilterCl.setOnClickListener(filterOnclickListener())
        }
    }

    private fun clickListener() : VolumesAdapter.OnClickListener{
        return VolumesAdapter.OnClickListener { id ->
            val bundle = Bundle()
            bundle.putString(VOLUME_ID, id)

            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
        }
    }

    private fun filterOnclickListener() : View.OnClickListener {
        return View.OnClickListener {
            isFilter = !isFilter
            var imageResource = if(isFilter) R.drawable.ic_filter else R.drawable.ic_filter_outlined

            binding.volumesFilterIv.setImageDrawable(ContextCompat.getDrawable(context!!, imageResource))
            binding.volumesRv.adapter = if(isFilter) favAdapter else mainAdapter
        }
    }

    private fun recyclerScrollListener() {
        binding.volumesRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int, dy: Int
            ) {

                totalItemCount = binding.volumesRv.layoutManager?.itemCount
                lastVisibleItem = layoutManager.findLastVisibleItemPosition() + 1

                if(lastVisibleItem == totalItemCount){
                    binding.volumesBottomPb.visibility = View.VISIBLE
                }
            }
        })
    }
}