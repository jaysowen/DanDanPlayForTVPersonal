package com.seiko.player.media

import com.seiko.common.util.getRealTextSizeScale
import com.seiko.danma.DanmakuEngineOptions
import master.flame.danmaku.danmaku.model.IDisplayer

object PlayerOptions {

    /**
     * 弹幕配置
     */
    fun createDanmakuOptions(): DanmakuEngineOptions {
        return DanmakuEngineOptions {
            setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 2f)
//            //设置防弹幕重叠
//            .preventOverlapping()
            //合并重复弹幕
            isDuplicateMergingEnabled = true
            //弹幕滚动速度
            setScrollSpeedFactor(1.2f)
            //弹幕文字大小
            setScaleTextSize(getRealTextSizeScale(3f))
//        //弹幕文字透明度
//        .setDanmakuTransparency(0.8f)
                //是否显示滚动弹幕
            r2LDanmakuVisibility = true
                //是否显示顶部弹幕
            ftDanmakuVisibility = true
                //是否显示底部弹幕
            fbDanmakuVisibility = false
            //同屏数量限制
            setMaximumVisibleSizeInScreen(100)
        }
    }

}