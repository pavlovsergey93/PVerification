package devices

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmail.pavlovsv93.verification.R
import com.gmail.pavlovsv93.verification.databinding.FragmentListDevicesBinding
import com.gmail.pavlovsv93.verification.domain.KipEntity
import com.gmail.pavlovsv93.verification.ui.VerificationActivity
import addfragment.AddKipEntityFragment
import com.gmail.pavlovsv93.verification.ui.listdevices.ListDeviceViewModel
import com.gmail.pavlovsv93.verification.ui.listdevices.adapter.ListAdapter
import com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.update.BottomSheetLocationCorrectFragment
import com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.filter.BottomSheetFilterFragment
import com.gmail.pavlovsv93.verification.ui.listdevices.bottomsheet.filter.FilterEntity
import com.gmail.pavlovsv93.verification.ui.profile.ProfileFragment
import com.gmail.pavlovsv93.verification.utils.AppState
import com.gmail.pavlovsv93.verification.utils.swipe.ItemTouchHelperCallback
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class ListDevicesFragment : Fragment() {

    companion object {
        const val KEY_ID_KIP = "ListDevicesFragment.KEY_ID_KIP"
        const val ARG_ID_KIP = "ListDevicesFragment.ARG_ID_KIP"
        fun newInstance() = ListDevicesFragment()
    }

    private var _binding: FragmentListDevicesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ListDeviceViewModel by viewModel(named("ListDeviceViewModel"))
    private val adapter: ListAdapter = ListAdapter(object : VerificationActivity.OnClickTheDevice {
        override fun onClick(kipEntity: KipEntity) {
            val data = Bundle().apply {
                putString(ARG_ID_KIP, kipEntity.idKip)
            }
            parentFragmentManager.setFragmentResult(KEY_ID_KIP, data)
        }

        override fun onSwipe(kipEntity: KipEntity) {
            BottomSheetLocationCorrectFragment.newInstance(
                kipEntity.idKip,
                kipEntity.keyStation,
                kipEntity.station,
                kipEntity.position
            )
                .show(parentFragmentManager, BottomSheetLocationCorrectFragment.TAG)
        }
    })
    private var filter: FilterEntity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListDevicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as VerificationActivity).setOnBackPressedListener(this@ListDevicesFragment)
        val recyclerView = binding.rvListDevices
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = adapter
        ItemTouchHelper(ItemTouchHelperCallback(adapter)).attachToRecyclerView(recyclerView)

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { state ->
            ranger(state)
        })
        viewModel.getAllData()
        binding.fabAddKipEntity.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .add(R.id.fcContainer, AddKipEntityFragment.newInstance())
                .commit()
        }
        binding.tietEditTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH && !binding.tietEditTextSearch.text.isNullOrEmpty()) {
                startSearch()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        binding.srlRefresh.setOnRefreshListener {
            viewModel.getAllData()
            binding.srlRefresh.isRefreshing = false
        }
        binding.fabFilter.setOnClickListener {
            BottomSheetFilterFragment.newInstance(filter)
                .show(parentFragmentManager, BottomSheetFilterFragment.TAG)
        }
        binding.fabProfile.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.fcContainerMain, ProfileFragment.newInstance()
            ).addToBackStack(null).commit()
        }
        pullFilterParameters()
    }

    private fun pullFilterParameters() {
        parentFragmentManager.setFragmentResultListener(
            BottomSheetFilterFragment.KEY_FILTER_PARAMETER,
            this,
            FragmentResultListener { _, data ->
                filter = data.get(BottomSheetFilterFragment.ARG_FILTER_PARAMETER) as FilterEntity
                filter?.let { viewModel.getDataAsFilter(it) }
            })
    }

    private fun startSearch() {
        val searchStr: String = binding.tietEditTextSearch.text.toString()
        viewModel.getResultSearch(searchStr)
    }

    private fun ranger(state: AppState) {
        when (state) {
            is AppState.OnError -> {
                val message = state.exception.message ?: getString(R.string.error)
                Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    .setAction(R.string.relode) {
                        viewModel
                    }
                    .show()
            }
            is AppState.OnLoading -> {
                binding.pbProgress.visibility = if (state.load) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
            is AppState.OnSuccess<*> -> {
                val result = state.success as List<KipEntity>
                binding.tilTextLayout.helperText = "Найдено совпадений: ${result.size} ед."
                adapter.setData(result)
            }
            is AppState.OnShowMessage -> {
                Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}