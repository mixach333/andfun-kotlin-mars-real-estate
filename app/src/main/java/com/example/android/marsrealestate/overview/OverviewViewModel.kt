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
import androidx.lifecycle.viewModelScope
import com.example.android.marsrealestate.network.MarsApiStatus
import com.example.android.marsrealestate.network.RentOrBuyTypeFilter
import kotlinx.coroutines.launch


class OverviewViewModel : ViewModel() {

    // The internal MutableLiveData String that stores the most recent response status
    private val _status = MutableLiveData<MarsApiStatus>()

    // The external immutable LiveData for the status String
    val status: LiveData<MarsApiStatus>
        get() = _status

    private val _properties = MutableLiveData<List<MarsProperty>>()
    val properties get() : LiveData<List<MarsProperty>> = _properties

    init {
        getMarsRealEstateProperties(RentOrBuyTypeFilter.ALL)
    }

    private fun getMarsRealEstateProperties(filter: RentOrBuyTypeFilter) {
        viewModelScope.launch {
            _status.value = MarsApiStatus.LOADING
            try {
                val listResult = MarsApi.retrofitService.getProperties(filter.type)
                if (listResult.isNotEmpty()) {
                    _status.value = MarsApiStatus.DONE
                    _properties.value = listResult
                }
            } catch (e: Exception) {
                _status.value = MarsApiStatus.ERROR
                _properties.value = listOf()
            }
        }
    }

    private val _navigateToSelectedProperty = MutableLiveData<MarsProperty>()
    val navigateToSelectedProperty : LiveData<MarsProperty> = _navigateToSelectedProperty

    fun doneNavigating(){
        _navigateToSelectedProperty.value = null
    }

    fun displayPropertyDetails(marsProperty: MarsProperty){
        _navigateToSelectedProperty.value = marsProperty
    }

    fun updateFilter(filter: RentOrBuyTypeFilter){
        getMarsRealEstateProperties(filter)
    }

}
