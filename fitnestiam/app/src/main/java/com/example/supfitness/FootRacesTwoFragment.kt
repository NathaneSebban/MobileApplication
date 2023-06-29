package com.example.supfitness

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.fragment_foot_races_two.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class FootRacesTwoFragment : Fragment() {



    private lateinit var binding: View
    private var timerStarted = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    private lateinit var sqliteHelper: SQLiteHelperRun
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private var lastLocation: Location? = null
    private var latifinal: String? = null
    private var longfinal: String? = null
    private lateinit var recyclerView: RecyclerView
    private var adapter: WeightAdapter? = null
    private var currentTime: String? = null
    public var startDate: String? = null
    private var endDate: String? = null
    private var firststart: Boolean = true


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        binding = inflater!!.inflate(R.layout.fragment_foot_races_two, container, false)

        binding.savePosition.isEnabled = false
        binding.savePosition.isClickable = false
        binding.resetButton.isEnabled = false
        binding.resetButton.isClickable = false

       binding.startStopButton.setOnClickListener { startStopTimer() }
        binding.resetButton.setOnClickListener { resetTimer() }
        binding.savePosition.setOnClickListener { savePos() }

        sqliteHelper = context?.let { SQLiteHelperRun(it) }!!



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        serviceIntent = Intent(context, TimerService::class.java)
        requireActivity().registerReceiver(updateTime, IntentFilter(TimerService.TIMER_UPDATED))

       return binding

    }

    private fun savePos() {
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
        else {
            getLastLocation()
        }
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                time = intent.getDoubleExtra(TimerService.TIME_EXTRA, 0.0)
            }
            binding.timeTV.text = getTimeStringFromDouble(time)
            currentTime = getTimeStringFromDouble(time)
        }

    }

    private fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun resetTimer() {
        sqliteHelper.finishRun(time)
        binding.resetButton.isEnabled = false
        binding.resetButton.isClickable = false
        stopTimer()
        time = 0.0
        binding.timeTV.text = getTimeStringFromDouble(time)
        val tempdate = SimpleDateFormat("dd/M/yyyy H:m:s")
        val endDate: String = tempdate.format(Date())
        firststart = true
        Log.e("pppp", "${startDate} and ${endDate}" )

    }

    private fun startStopTimer() {
        Log.e("pppp", "pass start")
        if(timerStarted)
            stopTimer()
        else
            startTimer()

    }

    private fun stopTimer() {
        requireActivity().stopService(serviceIntent)
        binding.startStopButton.text = "Démarrer"
        binding.startStopButton.icon = context?.let { getDrawable(it,
            R.drawable.ic_baseline_play_arrow_24
        ) }
        timerStarted = false
        binding.savePosition.isEnabled = false
        binding.savePosition.isClickable = false
    }

    private fun startTimer(){
        binding.resetButton.isEnabled = true
        binding.resetButton.isClickable = true
        if(firststart)
            sqliteHelper.startRun()
            firststart = false
        endDate = null

        binding.savePosition.isEnabled = true
        binding.savePosition.isClickable = true
        serviceIntent.putExtra(TimerService.TIME_EXTRA, time)
        requireActivity().startService(serviceIntent)
        binding.startStopButton.text = "Pause"
        binding.startStopButton.icon = context?.let { getDrawable(it,
            R.drawable.ic_baseline_pause_24
        ) }
        timerStarted = true
    }

    private fun makeTimeString(hour: Int, min: Int, sec: Int): String = String.format("%02d:%02d:%02d", hour, min, sec)

    public override fun onStart() {
        super.onStart()
        if (!checkPermissions()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions()
            }
        }
    }

    private fun getLastLocation() {

        activity?.let {
            if (ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationClient?.lastLocation!!.addOnCompleteListener(it) { task ->
                if (task.isSuccessful && task.result != null) {
                    lastLocation = task.result
                    latifinal = (lastLocation)!!.latitude.toString()
                    longfinal = (lastLocation)!!.longitude.toString()
                    Toast.makeText(context, "Position Sauvegardé, Latitude : $latifinal Longitude : $longfinal", Toast.LENGTH_SHORT).show()
                    sqliteHelper.PositionSave(latifinal!!, longfinal!!)

                    Log.e("pppp", "${latifinal} and ${longfinal}")

                    /*latitudeText!!.text = latitudeLabel + ": " + (lastLocation)!!.latitude*/
                    /*longitudeText!!.text = longitudeLabel + ": " + (lastLocation)!!.longitude*/
                } else {
                    Log.w(TAG, "getLastLocation:exception", task.exception)
                    Toast.makeText(context, "Pas de location dectecté", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showSnackbar(
        mainTextStringId: String, actionStringId: String,
        listener: View.OnClickListener
    ) {
        Toast.makeText(activity, mainTextStringId, Toast.LENGTH_LONG).show()
    }
    private fun checkPermissions(): Boolean {
        val permissionState = ActivityCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return permissionState == PackageManager.PERMISSION_GRANTED
    }
    private fun startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
            REQUEST_PERMISSIONS_REQUEST_CODE
        )
    }
    private fun requestPermissions() {
        val shouldProvideRationale = ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (shouldProvideRationale) {
            showSnackbar("Permission nécéssaire", "Okay",
                View.OnClickListener {
                    startLocationPermissionRequest()
                })
        }
        else {
            Log.i(TAG, "resuest perm")
            startLocationPermissionRequest()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            when {
                grantResults.isEmpty() -> {
                    // If user interaction was interrupted, the permission request is cancelled and you
                    // receive empty arrays.
                }
                grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    // Permission granted.
                    getLastLocation()
                }
                else -> {
                    showSnackbar("Permission was denied", "Settings",
                        View.OnClickListener {
                            // Build intent that displays the App settings screen.
                            val intent = Intent()
                            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            val uri = Uri.fromParts(
                                "package",
                                Build.DISPLAY, null
                            )
                            intent.data = uri
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }
    companion object {
        private val TAG = "LocationProvider"
        private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    }

    private fun initRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager( context)
        adapter = WeightAdapter()
        recyclerView.adapter = adapter

    }

    private fun initView() {
        recyclerView = binding.findViewById(R.id.recyclerView)
    }
}