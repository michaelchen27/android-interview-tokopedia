package com.tokopedia.filter.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.filter.R
import com.tokopedia.filter.view.Util
import com.tokopedia.filter.view.data.vo.ProductX

class ProductAdapter(
    private var productList: List<ProductX>,
    private val context: Context
) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private var ivProduct: ImageView = view.findViewById(R.id.iv_product)
        private var tvTitle: TextView = view.findViewById(R.id.tv_product_name)
        private var tvProductPrice: TextView = view.findViewById(R.id.tv_product_price)
        private var tvProductDiscount: TextView = view.findViewById(R.id.tv_product_discount)
        private var tvProductPriceSlashed: TextView =
            view.findViewById(R.id.tv_product_price_slashed)
        private var tvShopCity: TextView = view.findViewById(R.id.tv_shop_city)
        private var llProductPriceSlashed: LinearLayout =
            view.findViewById(R.id.ll_product_price_slashed)

        fun bind(productX: ProductX?, context: Context) {
            val imageUrl = productX?.imageUrl
            val name = productX?.name
            val price = productX?.priceInt
            val discountPercentage = productX?.discountPercentage
            val priceSlashed = productX?.slashedPriceInt
            val shopCity = productX?.shop?.city

            Glide.with(context)
                .load(imageUrl)
                .into(ivProduct)

            if (discountPercentage != 0) {
                llProductPriceSlashed.visibility = View.VISIBLE
            }

            tvTitle.text = name
            tvProductPrice.text = price?.let { Util.formatRupiah(it) }
            tvProductDiscount.text = "${discountPercentage.toString()}%"
            tvProductPriceSlashed.text = priceSlashed?.let { Util.formatRupiah(it) }
            tvShopCity.text = shopCity
        }

//        override fun getFilter(): Filter {
//            return object : Filter() {
//                override fun performFiltering(constraint: CharSequence?): FilterResults {
//                    val charFilter = constraint.toString()
//                    productFilterList = if (charFilter.isEmpty()) {
//                        productList
//                    } else {
//                        val resultList = ArrayList<ProductX>()
//                        for (row in productList) {
//                            if (row.shop.city.toLowerCase(Locale.ROOT).contains(
//                                    charFilter.toLowerCase(Locale.ROOT)
//                                )
//                            ) {
//                                resultList.add(row)
//                            }
//                        }
//                        resultList.toList()
//                    }
//                    val filterResults = FilterResults()
//                    filterResults.values = productFilterList
//                    return filterResults
//                }
//
//                override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//                    productFilterList = results?.values as List<ProductX>
//                    notifyDataSetChanged()
//                }
//            }
//        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(productList[position], context)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    fun updateData(filteredProductList: List<ProductX>) {
        productList = filteredProductList
        notifyDataSetChanged()
    }

}