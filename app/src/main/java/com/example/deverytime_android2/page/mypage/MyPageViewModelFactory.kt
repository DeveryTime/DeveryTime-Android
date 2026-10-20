package com.example.deverytime_android2.page.mypage

import android.content.ContentResolver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.deverytime_android2.ContentResolverProfileImageReader
import com.example.deverytime_android2.MyPageRepository

class MyPageViewModelFactory(
    private val contentResolver: ContentResolver,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(MyPageViewModel::class.java)) {
            "지원하지 않는 ViewModel: ${modelClass.name}"
        }
        @Suppress("UNCHECKED_CAST")
        return MyPageViewModel(
            repository = MyPageRepository(
                profileImageReader = ContentResolverProfileImageReader(contentResolver),
            ),
        ) as T
    }
}
