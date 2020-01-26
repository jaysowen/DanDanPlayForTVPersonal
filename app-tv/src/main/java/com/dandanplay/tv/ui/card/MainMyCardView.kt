package com.dandanplay.tv.ui.card

import android.content.Context
import com.dandanplay.tv.R
import com.dandanplay.tv.data.model.HomeSettingBean
import com.seiko.common.ui.card.AbsCardView
import kotlinx.android.synthetic.main.item_main_my.view.*

class MainMyCardView(context: Context) : AbsCardView<HomeSettingBean>(context) {

    override fun getLayoutId(): Int {
        return R.layout.item_main_my
    }

    override fun bind(item: HomeSettingBean) {
        img.setImageResource(item.image)
        title.text = item.name
    }

}