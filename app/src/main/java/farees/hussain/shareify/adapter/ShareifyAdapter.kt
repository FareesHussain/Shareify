package farees.hussain.shareify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import farees.hussain.shareify.R
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.databinding.ItemShareifyBinding
import java.text.SimpleDateFormat
import java.util.*

class ShareifyAdapter(
    val clickListner: ShareifyItemClickListner
): ListAdapter<ShareifyItem ,ShareifyAdapter.ViewHolder>(ShareifyItemDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemShareifyBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListner)
    }

    class ShareifyItemClickListner(val clickListner: (item: ShareifyItem) -> Unit)
    class ViewHolder(val binding: ItemShareifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ShareifyItem, clickListner: ShareifyItemClickListner){
            binding.tvFileName.text = item.filename
            if (!item.isExpired){
                val d: Date = Calendar.getInstance().time
                val secondsInMillis = 1000
                val minutesInMillis : Long = secondsInMillis.toLong() * 60
                val hoursInMillis : Long = minutesInMillis * 60
                val daysInMillis: Long = hoursInMillis * 24
                var diff = d.time - item.sharedDate.time
                val elapsedDays = diff /daysInMillis
                diff%=daysInMillis
                val elapsedHrs = diff/hoursInMillis
                if(elapsedHrs<24){
                    item.isExpired = true
                }
            }
            if(!item.isExpired){
                binding.tvfileStatus.text = "expired"
                binding.tvfileStatus.setBackgroundResource(R.drawable.rounded_textview_expired)
            } else {
                binding.tvfileStatus.text = "active"
                binding.tvfileStatus.setBackgroundResource(R.drawable.rounded_textview)
            }
        }
    }
    class ShareifyItemDiffCallBack : DiffUtil.ItemCallback<ShareifyItem>(){
        override fun areItemsTheSame(oldItem: ShareifyItem, newItem: ShareifyItem)
        = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ShareifyItem, newItem: ShareifyItem)
        = oldItem == newItem
    }
}