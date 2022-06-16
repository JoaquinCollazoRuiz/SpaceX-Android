package com.joaquincollazoruiz.spacex.presentation.recyclerview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import com.joaquincollazoruiz.spacex.R
import com.joaquincollazoruiz.spacex.databinding.LaunchRowItemBinding
import com.joaquincollazoruiz.spacex.domain.model.Launch
import java.time.LocalDateTime
import java.time.ZoneOffset

class LaunchesAdapter(
    private val context: Context,
    private val imageLoader: ImageLoader,
    private val imageRequestBuilder: ImageRequest.Builder,
    asyncDifferConfig: AsyncDifferConfig<Launch>
) : ListAdapter<Launch, LaunchesAdapter.ViewHolder>(asyncDifferConfig) {

    private var onClickListener: OnLaunchSelectedListener? = null

    class ViewHolder(val binding: LaunchRowItemBinding) : RecyclerView.ViewHolder(binding.root)

    fun interface OnLaunchSelectedListener {
        fun invoke(launch: Launch)
    }

    fun setOnLaunchSelectedListener(listener: OnLaunchSelectedListener?) {
        onClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LaunchRowItemBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        val unknownFieldString = context.getString(R.string.unknown_value_field)

        val dt = if (item.launchDateTime == null) null else LocalDateTime.ofEpochSecond(
            item.launchDateTime.unixTimestamp,
            0,
            ZoneOffset.UTC
        )

        with(holder) {
            // Bind title data
            binding.title.text = item.name
            binding.dateAtTime.text =
                if (dt == null) context.getString(R.string.blank)
                else context.getString(
                    R.string.launch_field_date_template,
                    dt.year.toString(),
                    dt.monthValue.toString().padStart(2, padChar = '0'),
                    dt.dayOfMonth.toString().padStart(2, padChar = '0'),
                    dt.hour.toString().padStart(2, padChar = '0'),
                    dt.minute.toString().padStart(2, padChar = '0')
                )

            // Bind launch time data
            if (item.launchDateTime == null) {
                binding.daysSinceFromNow.visibility = View.GONE
                binding.headerDaysSinceFromNow.visibility = View.GONE
            }

            // Click listeners
            binding.root.setOnClickListener {
                if (itemCount > position) {
                    onClickListener?.invoke(getItem(position))
                }
            }

            // Bind rocket data field
            val rocketName = item?.rocket?.name ?: unknownFieldString
            val rocketType = item?.rocket?.type ?: unknownFieldString
            val rocketDescription =
                context.getString(R.string.launch_rocket_field_template, rocketName, rocketType)
            binding.rocketNameType.text = rocketDescription

            // Bind patch image
            val patchImage = item.links?.patchImage
            val r = imageRequestBuilder
                .data(patchImage?.smallURL ?: patchImage?.largeURL)
                .target(binding.patchIcon)
            imageLoader.enqueue(r.build())
        }
    }
}

class LaunchesDiffUtilCallback : DiffUtil.ItemCallback<Launch>() {
    override fun areItemsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Launch, newItem: Launch): Boolean {
        return oldItem.name == newItem.name && oldItem.rocket == newItem.rocket
    }
}