package com.guet.flexbox.litho.load

import android.content.Context
import android.graphics.Bitmap
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.ParcelFileDescriptor
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.resource.bitmap.*
import com.bumptech.glide.load.resource.transcode.TranscoderRegistry
import com.bumptech.glide.module.LibraryGlideModule
import com.guet.flexbox.litho.drawable.ExBitmapDrawable
import java.io.InputStream
import java.nio.ByteBuffer


@GlideModule
class ExBitmapDrawableLibraryModule : LibraryGlideModule() {

    override fun registerComponents(
            context: Context,
            glide: Glide,
            registry: Registry
    ) {
        val arrayPool = glide.arrayPool
        val bitmapPool = glide.bitmapPool
        val resources = context.resources
        val downSampler = Downsampler(
                registry.imageHeaderParsers,
                resources.displayMetrics, bitmapPool, arrayPool)
        val bitmapEncoder = BitmapEncoder(arrayPool)
        val parcelDecoder = VideoDecoder.parcel(bitmapPool)
        val byteBufferBitmapDecoder: ResourceDecoder<ByteBuffer, Bitmap>
        val streamBitmapDecoder: ResourceDecoder<InputStream, Bitmap>
        if (VERSION.SDK_INT >= VERSION_CODES.P) {
            streamBitmapDecoder = InputStreamBitmapImageDecoderResourceDecoder()
            byteBufferBitmapDecoder = ByteBufferBitmapImageDecoderResourceDecoder()
        } else {
            byteBufferBitmapDecoder = ByteBufferBitmapDecoder(downSampler)
            streamBitmapDecoder = StreamBitmapDecoder(downSampler, arrayPool)
        }
        registry.prepend(
                Constants.BUCKET_EX_BITMAP_DRAWABLE,
                ByteBuffer::class.java,
                ExBitmapDrawable::class.java,
                ExBitmapDrawableDecoder(byteBufferBitmapDecoder, resources)
        ).prepend(Constants.BUCKET_EX_BITMAP_DRAWABLE,
                InputStream::class.java,
                ExBitmapDrawable::class.java,
                ExBitmapDrawableDecoder(streamBitmapDecoder, resources)
        ).prepend(Constants.BUCKET_EX_BITMAP_DRAWABLE,
                ParcelFileDescriptor::class.java,
                ExBitmapDrawable::class.java,
                ExBitmapDrawableDecoder(parcelDecoder, resources)
        ).prepend(
                ExBitmapDrawable::class.java,
                ExBitmapDrawableEncoder(bitmapPool, bitmapEncoder)
        )

        registry.register(
                Bitmap::class.java,
                ExBitmapDrawable::class.java,
                ExBitmapDrawableTranscoder(resources)
        )

        @Suppress("UNCHECKED_CAST")
        val transcoders = TranscoderRegistry::class.java.getDeclaredField("transcoders").apply {
            isAccessible = true
        }.get(Registry::class.java.getDeclaredField("transcoderRegistry").apply {
            isAccessible = true
        }.get(registry)) as ArrayList<Any>
        val tc = transcoders.removeAt(transcoders.size - 1)
        transcoders.add(0, tc)

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            val byteBufferDecoder = VideoDecoder.byteBuffer(bitmapPool)
            registry.prepend(
                    Constants.BUCKET_EX_BITMAP_DRAWABLE,
                    ByteBuffer::class.java,
                    ExBitmapDrawable::class.java,
                    ExBitmapDrawableDecoder(byteBufferDecoder, resources)
            )
        }
        registry.setResourceDecoderBucketPriorityList(listOf(
                Registry.BUCKET_GIF,
                Registry.BUCKET_BITMAP,
                Constants.BUCKET_EX_BITMAP_DRAWABLE,
                Registry.BUCKET_BITMAP_DRAWABLE
        ))
    }


}