package com.example.bookstore

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstore.databinding.FragmentFirstBinding
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.ui.adapters.VolumesAdapter
import com.example.bookstore.viewmodels.FirstViewModel

class FirstFragment : Fragment() {

    private var data = MutableLiveData<List<VolumeDto.Volume>>()
    private var _binding: FragmentFirstBinding? = null
    private var vm : FirstViewModel? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        vm = ViewModelProvider(this)[FirstViewModel::class.java]

        updateUi()

        return binding.root
    }

    private fun updateUi(){
        binding.volumesRv.layoutManager = LinearLayoutManager(context)
        vm?.getListData()?.observe(viewLifecycleOwner) { list ->
            data.value = list
            binding.volumesRv.adapter = context?.let { VolumesAdapter(it, data) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}