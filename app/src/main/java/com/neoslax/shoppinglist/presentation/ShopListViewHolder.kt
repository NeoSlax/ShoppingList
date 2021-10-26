import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neoslax.shoppinglist.R

class ShopListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvName = itemView.findViewById<TextView>(R.id.tvName)
    val tvCount = itemView.findViewById<TextView>(R.id.tvCount)
}