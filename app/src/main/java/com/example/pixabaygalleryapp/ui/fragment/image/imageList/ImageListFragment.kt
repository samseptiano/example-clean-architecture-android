package com.example.pixabaygalleryapp.ui.fragment.image.imageList

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.pixabaygalleryapp.R
import com.example.pixabaygalleryapp.base.adapters.GenericListAdapter
import com.example.pixabaygalleryapp.base.ui.BaseFragment
import com.example.pixabaygalleryapp.base.data.ResponseStatus
import com.example.pixabaygalleryapp.ui.fragment.image.ImageViewModel
import com.example.pixabaygalleryapp.databinding.FragmentImageListBinding
import com.example.pixabaygalleryapp.model.ImagesInfo
import com.example.pixabaygalleryapp.utils.hideKeyboard
import com.example.pixabaygalleryapp.utils.obtainViewModel
import com.kennyc.view.MultiStateView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class ImageListFragment : BaseFragment<FragmentImageListBinding>() {

    override val inflateLayout: (LayoutInflater, ViewGroup?, Boolean) -> FragmentImageListBinding
        get() = FragmentImageListBinding::inflate

    private val viewModel: ImageViewModel by lazy {
        obtainViewModel(requireActivity(), ImageViewModel::class.java, viewModelFactory)
    }

    private lateinit var adapter: GenericListAdapter<ImagesInfo>
    private var actionBarHeight = 0


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        /*
        * Get actionbar height for use in translation
        * */
        context?.let { item ->
            actionBarHeight = with(TypedValue().also {
                item.theme.resolveAttribute(
                    android.R.attr.actionBarSize,
                    it,
                    true
                )
            }) {
                TypedValue.complexToDimensionPixelSize(this.data, resources.displayMetrics)
            }

            /*
            * Translate items on menu click
            * */
            actionBarHeight *= -1
            binding.fldGrpSearchPhotos.translationY = actionBarHeight.toFloat()
            binding.nestedScrollView.translationY = actionBarHeight.toFloat() / 2

        }

        /*
        * Initiating recyclerview
        * */
        callingRecyclerView()

        /*
        * Fetch image list
        * */
        viewModel.imagesResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                ResponseStatus.SUCCESS -> {
                    it.data?.apply {
                        adapter.productItems = imagesInfo as ArrayList<ImagesInfo>
                        binding.multiStateView.viewState = MultiStateView.ViewState.CONTENT
                    }
                }
                ResponseStatus.ERROR -> {
                    it.data?.let { item ->
                        if (item.page == 1)
                            binding.multiStateView.viewState = MultiStateView.ViewState.EMPTY
                        else
                            showSnackBar(binding.nestedScrollView, it.message.toString())

                    } ?: run {
                        binding.multiStateView.viewState = MultiStateView.ViewState.ERROR
                        showSnackBar(binding.nestedScrollView, "Internet not available", "Retry") {
                            viewModel.retryConnection()
                        }

                    }
                }
                ResponseStatus.LOADING -> {
                    it.data?.let { item ->
                        if (item.page == 1) binding.multiStateView.viewState =
                            MultiStateView.ViewState.LOADING
                        else
                            showSnackBar(binding.nestedScrollView, "Loading more images")
                    }
                }
            }

        }

        /*
        * Checking scrollview scroll end
        * */
        binding.nestedScrollView.setOnScrollChangeListener { v: NestedScrollView, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
                viewModel.loadNextPagePhotos()
            }
        }

        /*
        * Image search
        * */
        binding.edtSearchPhotos.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                binding.edtSearchPhotos.hideKeyboard()
                val s = binding.edtSearchPhotos.text.toString()
                adapter.clearProductItem()
                binding.populateTxt.text = "Search: ${s.toUpperCase(Locale.ENGLISH)}"
                viewModel.searchImagesFromRemote(s)
            }
            false
        }

        /*
        * Image search clear
        * */
        binding.inputSearchPhotos.setEndIconOnClickListener {
            binding.edtSearchPhotos.setText("")
            adapter.clearProductItem()
            binding.populateTxt.text = "Search: Latest"
            viewModel.fetchImagesFromRemoteServer(1)
        }

    }

    /*
    * Initialize recyclerView with onClickListener
    * */
    @SuppressLint("ResourceType")
    private fun callingRecyclerView() {
        adapter = GenericListAdapter(R.layout.product_view) { item, position ->
            viewModel.setSelectedProduct(item)
            findNavController().navigate(ImageListFragmentDirections.actionImageListFragmentToImageDetailFragment())
        }
        adapter.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.productList.adapter = adapter
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.search_menu).isVisible = true
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.search_menu -> {
                binding.fldGrpSearchPhotos.animate().apply {
                    duration = 1000
                    translationY(if (binding.fldGrpSearchPhotos.translationY == actionBarHeight.toFloat()) 10f else actionBarHeight.toFloat())
                }.start()
                binding.nestedScrollView.animate().apply {
                    duration = 1000
                    translationY(if (binding.nestedScrollView.translationY == actionBarHeight.toFloat() / 2) 10f else actionBarHeight.toFloat() / 2)
                }.start()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}