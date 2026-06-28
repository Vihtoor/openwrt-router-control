package com.example.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.RouterDatabase
import com.example.data.RouterRepository
import com.example.data.SshClientManager

class RouterViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RouterViewModel::class.java)) {
            val database = RouterDatabase.getDatabase(application)
            val dao = database.routerDao()
            val sshClientManager = SshClientManager()
            val repository = RouterRepository(dao, sshClientManager)
            @Suppress("UNCHECKED_CAST")
            return RouterViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
