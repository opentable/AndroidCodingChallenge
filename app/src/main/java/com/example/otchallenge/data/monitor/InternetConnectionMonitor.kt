package com.example.otchallenge.data.monitor

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

fun createInternetConnectionMonitor(
    context: Context
): Observable<Boolean> {
    return BehaviorSubject.create { emitter ->

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                emitter.onNext(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                emitter.onNext(false)
            }
        }

        (context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager)
            .also { connectivityManager ->
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
                emitter.setCancellable {
                    connectivityManager.unregisterNetworkCallback(networkCallback)
                }
            }

    }
}
