package com.example.musicapp.ui.viewmodels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class TopBarViewModel : ViewModel(){
    fun logout(context: Context){
        Firebase.auth.signOut()
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
    }
}