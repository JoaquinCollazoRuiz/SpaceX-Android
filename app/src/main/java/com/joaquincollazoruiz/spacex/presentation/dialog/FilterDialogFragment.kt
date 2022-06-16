package com.joaquincollazoruiz.spacex.presentation.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joaquincollazoruiz.spacex.databinding.DialogFragmentFilteringBinding
import com.joaquincollazoruiz.spacex.domain.model.FilteringOption
import com.joaquincollazoruiz.spacex.domain.model.LaunchStatus
import com.joaquincollazoruiz.spacex.presentation.LaunchesViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class FilterDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: DialogFragmentFilteringBinding
    private val launchesViewModel: LaunchesViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentFilteringBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            btnLaunchStatusAll.setOnClickListener { chooseStatusFilteringAction(null) }
            btnLaunchStatusSuccessful.setOnClickListener {
                chooseStatusFilteringAction(FilteringOption.ByLaunchStatus(LaunchStatus.Successful))
            }
            btnLaunchStatusFailed.setOnClickListener {
                chooseStatusFilteringAction(FilteringOption.ByLaunchStatus(LaunchStatus.Failed))
            }
        }

        launchesViewModel.filterStatusPreference.observe(viewLifecycleOwner) { option ->
            handleStatusFilteringChoice(option)
        }
    }


    private fun handleStatusFilteringChoice(
        choice: FilteringOption.ByLaunchStatus?
    ) {
        setRadioButtonStatus(
            false,
            binding.btnLaunchStatusAllRb,
            binding.btnLaunchStatusFailedRb,
            binding.btnLaunchStatusSuccessfulRb
        )
        when (choice?.status) {
            null -> {
                // Disable launch status filtering scenario
                setRadioButtonStatus(true, binding.btnLaunchStatusAllRb)
            }
            LaunchStatus.Successful -> setRadioButtonStatus(
                true,
                binding.btnLaunchStatusSuccessfulRb
            )
            LaunchStatus.Failed -> setRadioButtonStatus(true, binding.btnLaunchStatusFailedRb)
        }
    }

    private fun chooseStatusFilteringAction(choice: FilteringOption.ByLaunchStatus?) {
        launchesViewModel.handleUserAction(
            LaunchesViewModel.UserAction.SelectStatusFilteringPreference(choice)
        )
    }

    private fun setRadioButtonStatus(checked: Boolean, vararg views: RadioButton) {
        views.forEach { it.isChecked = checked }
    }
}