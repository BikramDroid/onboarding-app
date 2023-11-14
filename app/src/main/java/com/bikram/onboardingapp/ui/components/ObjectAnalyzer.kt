package com.bikram.onboardingapp.ui.components

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions

class ObjectAnalyzer(
    private val onImageLabeled: (labels: List<ImageLabel>) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.5f)
        .build()

    private val labeler = ImageLabeling.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        imageProxy.image?.let {
            val image = InputImage.fromMediaImage(it, imageProxy.imageInfo.rotationDegrees)
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    onImageLabeled(labels)
                    imageProxy.close()
                }
        }
    }
}