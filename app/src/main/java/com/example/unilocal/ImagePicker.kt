package com.example.unilocal

import android.app.Activity
import android.content.Intent


class ImagePicker(private val activity: Activity) {

    fun pickImages() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        activity.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGES_REQUEST)
    }

    companion object {
        const val PICK_IMAGES_REQUEST = 1
    }
}


