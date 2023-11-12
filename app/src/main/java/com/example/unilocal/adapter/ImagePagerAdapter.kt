package com.example.unilocal.adapter

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.unilocal.fragment.AcceptedPlacesFragment
import com.example.unilocal.fragment.ImageFragment

class ImagePagerAdapter(var fragment: Fragment, var images:ArrayList<String>) : FragmentStateAdapter(fragment) {

    /*override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setImageURI(images[position])
        container.addView(imageView)
        return imageView
    }

    override fun getCount(): Int = images.size

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }*/
    override fun getItemCount() = images.size

    override fun createFragment(position: Int): Fragment {

        if(images.size > 0){
            for(i in 0..images.size-1){
                return ImageFragment.newInstance(images[i])
            }
        }

        return ImageFragment.newInstance(images[0])

    }
}



