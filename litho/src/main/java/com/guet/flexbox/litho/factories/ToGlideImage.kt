package com.guet.flexbox.litho.factories

import com.bumptech.glide.Glide
import com.facebook.litho.ComponentContext
import com.guet.flexbox.build.AttributeSet
import com.guet.flexbox.enums.ScaleType
import com.guet.flexbox.litho.resolve.mapping
import com.guet.flexbox.litho.toPxFloat
import com.guet.flexbox.litho.widget.GlideImage

internal object ToGlideImage : ToComponent<GlideImage.Builder>(CommonAssigns) {

    override val attributeAssignSet: AttributeAssignSet<GlideImage.Builder> by create {
        register("scaleType") { _, _, value: ScaleType ->
            scaleType(value.mapping())
        }
        register("blurRadius") { _, _, value: Float ->
            blurRadius(value)
        }
        register("blurSampling") { _, _, value: Float ->
            blurSampling(value)
        }
        register("aspectRatio") { _, _, value: Float ->
            imageAspectRatio(value)
        }
        register("url") { _, _, value: String ->
            url(value)
            Glide.with(context!!.androidContext)
                    .load(value)
                    .preload()
        }
        register("internal:resId") { _, _, value: Int ->
            resId(value)
            Glide.with(context!!.androidContext)
                    .load(value)
                    .preload()
        }
        register("borderLeftTopRadius") { _, _, value: Float ->
            leftTopRadius(value.toPxFloat())
        }
        register("borderRightTopRadius") { _, _, value: Float ->
            rightTopRadius(value.toPxFloat())
        }
        register("borderRightBottomRadius") { _, _, value: Float ->
            rightBottomRadius(value.toPxFloat())
        }
        register("borderLeftBottomRadius") { _, _, value: Float ->
            leftBottomRadius(value.toPxFloat())
        }
    }

    override fun create(
            c: ComponentContext,
            visibility: Boolean,
            attrs: AttributeSet
    ): GlideImage.Builder {
        return GlideImage.create(c)
    }
}