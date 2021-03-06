package akhmedoff.usman.thevt.ui.view.holders

import akhmedoff.usman.data.model.Catalog
import akhmedoff.usman.data.model.CatalogItem
import akhmedoff.usman.thevt.R
import akhmedoff.usman.thevt.ui.home.CatalogItemsRecyclerAdapter
import akhmedoff.usman.thevt.ui.view.CenterZoomLayoutManager
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.catalog_item.view.*

class CatalogViewHolder(
        itemView: View,
        clickListener: (CatalogItem, View) -> Unit
) : AbstractViewHolder<Catalog>(itemView) {

    private val adapter = CatalogItemsRecyclerAdapter(clickListener)

    private val catalogTitle = itemView.catalog_title

    private val catalogRecycler = itemView.findViewById<RecyclerView>(R.id.catalog_recycler)

    init {
        catalogRecycler.setHasFixedSize(true)
        catalogRecycler.adapter = adapter

        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(catalogRecycler)

        val layoutManager = CenterZoomLayoutManager(itemView.context, 0.1f, 0.199999f)
        catalogRecycler.layoutManager = layoutManager

        val margin = itemView.resources.getDimensionPixelSize(R.dimen.activity_vertical_margin)

        catalogRecycler.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = margin
                outRect.left = margin
            }
        })
    }

    override fun bind(item: Catalog) {
        catalogTitle.text = item.name
        adapter.items = item.items
        adapter.notifyDataSetChanged()

        catalogRecycler.smoothScrollToPosition(item.items.size / 2)
    }
}