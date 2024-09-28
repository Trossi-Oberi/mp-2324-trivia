package it.scvnsc.whoknows.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class NetworkMonitorService : Service() {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback

    companion object {
        private val _isOffline = MutableLiveData<Boolean>()
        val isOffline: LiveData<Boolean> = _isOffline

        fun startMonitoring(context: Context) {
            //controllo immediatamente lo stato della connessione all'avvio dell'applicazione
            Log.d("NetworkMonitorService", "startMonitoring: checking connection...")
            _isOffline.value = !isNetworkAvailable(context)
            Log.d("NetworkMonitorService", "startMonitoring: isOffline: ${_isOffline.value}")

            //Log.d("NetworkMonitorService", "startMonitoring")
            val intent = Intent(context, NetworkMonitorService::class.java)
            context.startService(intent)
        }

        private fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            val isConnected = activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            _isOffline.value = !isConnected
            //Log.d("NetworkMonitorService", "isNetworkAvailable: $isConnected")

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        fun stopMonitoring(context: Context) {
            val intent = Intent(context, NetworkMonitorService::class.java)
            context.stopService(intent)
        }

    }

    override fun onCreate() {
        super.onCreate()
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOffline.postValue(false)
            }

            override fun onLost(network: Network) {
                _isOffline.postValue(true)
            }

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        } catch (e: IllegalArgumentException) {
            // Il callback non era registrato, possiamo ignorare questa eccezione
        }

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    override fun onBind(intent: Intent?): IBinder? = null

}