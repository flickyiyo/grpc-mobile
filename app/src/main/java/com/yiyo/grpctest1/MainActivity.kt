package com.yiyo.grpctest1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.emergentes.ImageRequest
import io.grpc.emergentes.ImageStream
import io.grpc.emergentes.ImgStreamServiceGrpc
import io.grpc.stub.StreamObserver
import android.R.attr.countDown
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.View
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream


class MainActivity : AppCompatActivity() {


    lateinit var stub: ImgStreamServiceGrpc.ImgStreamServiceStub
    lateinit var blockingStub: ImgStreamServiceGrpc.ImgStreamServiceBlockingStub
    lateinit var channel: ManagedChannel
    lateinit var imv: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // 35.203.126.106
//                val stream = blockingStub.askToRasppi(imgRequest)
//        while (stream.hasNext()) {
//            val imgStream = stream.next()
//            val byteArray = imgStream.image.toByteArray()
//            val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
//            imv.setImageBitmap(bmp)
//        }
    }

    override fun onResume() {
        super.onResume()
        channel = ManagedChannelBuilder.forAddress("35.203.126.106", 50051)
            .usePlaintext()
            .build()
        blockingStub = ImgStreamServiceGrpc.newBlockingStub(channel)
        imv = findViewById(R.id.imv)
        println("Element")
//
    }

    fun stuff(v: View) {
//        val blockingStub = ImgStreamServiceGrpc.newBlockingStub(channel)
        val imgRequest = ImageRequest.newBuilder().setUsername("Edgar").build()

        val askToRasppi = blockingStub.askToRasppi(imgRequest)

        val mainHandler = Handler(Looper.getMainLooper())

        mainHandler.post(object : Runnable {
            override fun run() {
                if (askToRasppi.hasNext()) {
                    val stream = askToRasppi.next()
                    val byteArray = stream.image.toByteArray()
                    val bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    imv.setImageBitmap(bmp)
                }
                mainHandler.postDelayed(this, 1000)
            }
        })

    }

    override fun onDestroy() {
        channel.shutdown()
        super.onDestroy()

    }
}
