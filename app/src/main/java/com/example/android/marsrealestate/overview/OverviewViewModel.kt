/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.marsrealestate.overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.marsrealestate.network.MarsApi
import com.example.android.marsrealestate.network.MarsProperty
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response status
    private val _status = MutableLiveData<String>()

    // The external immutable LiveData for the status String
    val status: LiveData<String>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties get() : LiveData<List<MarsProperty>> = _properties




    init {
        getMarsRealEstateProperties()
    }

    private fun getMarsRealEstateProperties() {
        viewModelScope.launch {
            try {
                val listResult = MarsApi.retrofitService.getProperties()
                _status.value = "Success: ${listResult.size} Mars properties retrieved"
                if (listResult.isNotEmpty()) {
                    _properties.value = listResult
                }
            } catch (e: Exception) {
                _status.value = "Failure: ${e.message}"
            }
        }
    }

}
