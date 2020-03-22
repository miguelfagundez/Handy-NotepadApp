package com.devproject.fagundezdev.handynotepad.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.model.db.Notes
import com.devproject.fagundezdev.handynotepad.model.db.NotesDatabase
import com.devproject.fagundezdev.handynotepad.model.sharedpreferences.NoteSharedPreferences
import com.devproject.fagundezdev.handynotepad.repositories.NotesRepository
import com.devproject.fagundezdev.handynotepad.utils.ResponseObj
import kotlinx.coroutines.launch
import timber.log.Timber

/********************************************
 * ViewModel - NotesViewModel
 * This class handle the UI data
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
class NotesViewModel(application: Application):AndroidViewModel(application) {

    private val repository : NotesRepository
    val listNotes : LiveData<List<Notes>>?
    val context = application.applicationContext

    init {
        NoteSharedPreferences.getSharedPreferences(context)
        val notesDao = NotesDatabase.getInstance(application, viewModelScope)?.NotesDao()
        repository = NotesRepository(notesDao)
        listNotes = repository.listNotes
    }

    //***************************************************************
    // Database access methods
    //***************************************************************

    fun insert(id: Int?, title: String, description: String, body: String, imageUrl: String,
               priority: Int, selected: Boolean, creationDate: String, editDate: String)  = viewModelScope.launch {
        repository.insertNote(id, title, description, body, imageUrl, priority, selected, creationDate, editDate)
    }

    fun update(id: Int?, title: String, description: String, body: String, imageUrl: String,
               priority: Int, selected: Boolean, creationDate: String, editDate: String) = viewModelScope.launch {
        repository.updateNote(id, title, description, body, imageUrl, priority, selected, creationDate, editDate)
    }

    fun updateSelected(id: Int?, selected: Boolean) = viewModelScope.launch {
        repository.updateSelected(id, selected)
    }

    fun deleteSelectedNotes(numberCheckBoxs: Int) = viewModelScope.launch {
        if (numberCheckBoxs == repository.numberNotes)
            repository.deleteAll()
        else
            repository.deleteSelectedNotes()
    }

    fun checkBoxUnsuscribed()  = viewModelScope.launch {
        Timber.i("ViewModel Unsuscribed")
        repository.checkBoxUnsuscribed()
    }

    //***************************************************************
    // Shared Preferences access methods
    //***************************************************************

    fun checkingLoginCredentials(username : String, password : String) : ResponseObj {
        if (username.isNullOrBlank() || username.isNullOrEmpty() ||
                password.isNullOrEmpty() || password.isNullOrBlank()){
            return (ResponseObj(context.resources.getString(R.string.data_empty), false))
        }else{
            if (repository.checkingLoginCredentials(username, password)){
                //repository.loginUser(true)
                return(ResponseObj(context.resources.getString(R.string.data_login_correct), true))
            }else{
                return (ResponseObj(context.resources.getString(R.string.data_no_match), false))
            }
        }
    }

    fun checkingCredentialsAndRegister(username:String, confirmUsername:String,
    password:String, confirmPassword:String):ResponseObj{
        // Data is null - empty or blank
        if (username.isNullOrEmpty() || username.isNullOrBlank() ||
            confirmUsername.isNullOrEmpty() || confirmUsername.isNullOrBlank() ||
            password.isNullOrEmpty() || password.isNullOrBlank() ||
            confirmPassword.isNullOrEmpty() || confirmPassword.isNullOrBlank()){

            return(ResponseObj(context.resources.getString(R.string.data_empty), false))
        }else{
            // Data does not match
            if (username != confirmUsername || password != confirmPassword ){
                return(ResponseObj(context.resources.getString(R.string.data_no_match), false))
            }else{
                // Data match:
                // 1. User register (Shared Preferences)
                // 2. Going to Login Activity
                // 3. Send a successfully message
                repository.writingLoginCredentials(username, password)
                repository.registerUserSuccessfully()
                return(ResponseObj(context.resources.getString(R.string.data_correct), true))
            }
        }
    }

    fun isUserRegistered():Boolean? {
        return repository.isRegistered()
    }

    fun isUserLogged(): Boolean? {
        return repository.isLogged()
    }

    fun isSignInChecked(checked: Boolean) {
        repository.loginUser(checked)
    }


}