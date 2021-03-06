package farees.hussain.shareify.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import farees.hussain.shareify.R
import farees.hussain.shareify.data.local.ShareifyItem
import farees.hussain.shareify.databinding.ItemShareifyBinding
import timber.log.Timber
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
        Timber.d("${getItem(position).sharedDate}")
        holder.bind(getItem(position),clickListner)
    }

    class ShareifyItemClickListner(val clickListner: (item: ShareifyItem) -> Unit){
        fun itemClick(item: ShareifyItem) {
            clickListner(item)
        }
    }
    class ViewHolder(val binding: ItemShareifyBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: ShareifyItem, clickListner: ShareifyItemClickListner){
            binding.shareifyitem = item
            binding.tvFileName.text = item.filename
            binding.itemclicklistner = clickListner
            if (!item.isExpired){
                val d: Date = Calendar.getInstance().time
                val secondsInMillis : Long = 1000
                val minutesInMillis : Long = secondsInMillis * 60
                val hoursInMillis : Long = minutesInMillis * 60
                val daysInMillis: Long = hoursInMillis * 24
                var diff = d.time.toLong() - item.sharedDate.time.toLong()
                Timber.d("first timeber $diff")
                val elapsedDays = diff /daysInMillis
                diff %= daysInMillis
                val elapsedHrs = diff/hoursInMillis
                Timber.d("2nd timeber $diff $elapsedDays $elapsedHrs")
                Timber.d("$elapsedHrs for ${item.filename} ${item.sharedDate}")
                Timber.d("${item.sharedDate}")
                if(elapsedHrs>24 || elapsedDays>=1){
                    item.isExpired = true
                }
            }
            if(item.isExpired){
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