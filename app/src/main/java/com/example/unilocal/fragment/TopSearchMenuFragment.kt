package com.example.unilocal.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import com.example.unilocal.R
import com.example.unilocal.activities.SearchResultActivity
import com.example.unilocal.databinding.FragmentTopSearchMenuBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

lateinit var binding:FragmentTopSearchMenuBinding

class TopSearchMenuFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentTopSearchMenuBinding.inflate(inflater, container, false)
        binding.searchText.setOnEditorActionListener { textView, i, keyEvent ->
            if( i == EditorInfo.IME_ACTION_SEARCH)
            {
                val search = binding.searchText.text.toString()

                if(search.isNotEmpty()){
                    val intent = Intent(activity, SearchResultActivity::class.java)
                    intent.putExtra("text", search)
                    startActivity(intent)
                }
            }
            true
        }
        return binding.root
    }
}