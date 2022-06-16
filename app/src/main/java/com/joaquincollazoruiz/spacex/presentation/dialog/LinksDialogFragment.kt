package com.joaquincollazoruiz.spacex.presentation.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joaquincollazoruiz.spacex.R
import com.joaquincollazoruiz.spacex.databinding.DialogFragmentLinksBinding

class LinksDialogFragment : BottomSheetDialogFragment() {

    private val args: LinksDialogFragmentArgs by navArgs()
    private lateinit var binding: DialogFragmentLinksBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogFragmentLinksBinding.inflate(inflater, container, false)
        onBindingReady()
        return binding.root
    }

    private fun onBindingReady() {
        val noLinksAvailable =
            listOf(args.articleUrl, args.wikiUrl, args.webcastUrl).all { it.isNullOrBlank() }
        if (noLinksAvailable) {
            binding.heading.text = getString(R.string.links_none_available)
        }
        setupClickListenerOrHide(args.articleUrl, binding.btnArticle)
        setupClickListenerOrHide(args.webcastUrl, binding.btnWebcast)
        setupClickListenerOrHide(args.wikiUrl, binding.btnWiki)
    }

    private fun setupClickListenerOrHide(url: String?, v: View) {
        if (url.isNullOrBlank()) {
            v.visibility = View.GONE
        } else {
            v.setOnClickListener { launchUrlIntent(url) }
        }
    }

    private fun launchUrlIntent(url: String) {
        val linkIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(linkIntent)
        dismiss()
    }
}