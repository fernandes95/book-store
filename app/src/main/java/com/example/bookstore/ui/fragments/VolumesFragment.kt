package com.example.bookstore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentVolumesBinding
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.ui.adapters.VolumesAdapter
import com.example.bookstore.viewmodels.VolumesViewModel

class VolumesFragment : Fragment() {

    private var data = MutableLiveData<List<VolumeDto.Volume>>()
    private var _binding: FragmentVolumesBinding? = null
    private var vm : VolumesViewModel? = null
    private val binding get() = _binding!!

    companion object {
        const val VOLUME_ID = "VOLUME_ID"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentVolumesBinding.inflate(inflater, container, false)
        vm = ViewModelProvider(this)[VolumesViewModel::class.java]

        updateUi()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(){
        binding.volumesRv.layoutManager = LinearLayoutManager(context)
        vm?.getListData()?.observe(viewLifecycleOwner) { list ->
            data.value = list

            val clickListener = VolumesAdapter.OnClickListener { id ->
                val bundle = Bundle()
                bundle.putString(VOLUME_ID, id)

                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment, bundle)
            }

            binding.volumesRv.adapter = context?.let { VolumesAdapter(it, data, clickListener) }
        }
    }
}