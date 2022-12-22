package com.example.pixabaygalleryapp.ui.fragment.image.imageDetail

import android.os.Bundle
import android.view.*
import androidx.databinding.library.baseAdapters.BR
import androidx.lifecycle.lifecycleScope
import com.example.pixabaygalleryapp.R
import com.example.pixabaygalleryapp.base.ui.BaseFragment
import com.example.pixabaygalleryapp.base.data.ResponseStatus
import com.example.pixabaygalleryapp.ui.fragment.image.ImageViewModel
import com.example.pixabaygalleryapp.databinding.FragmentImageDetailBinding
import com.example.pixabaygalleryapp.model.ImagesInfo
import com.example.pixabaygalleryapp.utils.obtainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ImageDetailFragment : BaseFragment<FragmentImageDetailBinding>() {

    override val inflateLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentImageDetailBinding
        get() = FragmentImageDetailBinding::inflate

    private val viewModel: ImageViewModel by lazy {
        obtainViewModel(requireActivity(), ImageViewModel::class.java, viewModelFactory)
    }

    private var data: ImagesInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.search_menu).isVisible = false
        super.onPrepareOptionsMenu(menu)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
        * Fetch product list
        * */
        viewModel.selectedImagesResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    data = it.data
                    data?.let { item ->

                        /*
                        * Passing data to view
                        * */
                        binding.setVariable(BR.selImageItem, item)

                        /*
                        * Show loading and wait until view is not ready
                        * */
                        lifecycleScope.launch {
                            delay(500)
                            binding.productImg.visibility = View.VISIBLE
                        }
                    }
                }
                ResponseStatus.ERROR -> {
                }
                ResponseStatus.LOADING -> {
                }
            }

        }
    }

}