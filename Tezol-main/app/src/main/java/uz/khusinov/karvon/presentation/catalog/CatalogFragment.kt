package uz.khusinov.karvon.presentation.catalog

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import uz.khusinov.karvon.R
import uz.khusinov.karvon.databinding.FragmentCatalogBinding
import uz.khusinov.karvon.domain.model.shop.CategoryRespons
import uz.khusinov.karvon.presentation.BaseFragment
import uz.khusinov.karvon.presentation.catalog.components.CatalogsAdapter
import uz.khusinov.karvon.utills.launchAndRepeatWithViewLifecycle
import uz.khusinov.karvon.utills.navigateSafe
import uz.khusinov.karvon.utills.viewBinding

@AndroidEntryPoint
class CatalogFragment : BaseFragment(R.layout.fragment_catalog) {

    private val binding by viewBinding { FragmentCatalogBinding.bind(it) }
    private val viewModel by viewModels<CatalogViewModel>()
    private val adapter by lazy { CatalogsAdapter(::onItemClicked) }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.apply {
            rvCatalog.adapter = adapter

            search.setOnClickListener {
                requireActivity().findNavController(R.id.main_container).navigateSafe(
                    R.id.action_mainFragment_to_searchFragment
                )
            }
        }
    }

    private fun setupObserver() {
        launchAndRepeatWithViewLifecycle {
            viewModel.categoryPaging.collectLatest { data ->
                adapter.submitData(data)
            }
        }
    }

    private fun onItemClicked(category: CategoryRespons) {
        requireActivity().findNavController(R.id.main_container).navigate(
            R.id.action_mainFragment_to_selectedCategoryFragment,
            Bundle().apply {
                putInt("categoryId", category.id)
                putString("title", category.name)
            }
        )
    }
}