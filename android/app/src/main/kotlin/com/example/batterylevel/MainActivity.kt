package com.example.batterylevel

import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES


class MainActivity: FlutterActivity() {
    private val CHANNEL = 'samples.flutter.dev/battery'

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
                .setMethodCallHandler{
                    call, result ->
                        if(call.method == 'getBatteryLevel'){
                            var batteryLevel = getBatteryLevel();

                            if(batteryLevel != -1) {
                                result.success(batteryLevel);
                            } else {
                                result.error('UNAVAILABLE', 'BATTERY LEVEL NOT AVAILABLE')
                            }
                        } else {
                            result.notImplemented()
                        }
                }
    }

    private fun getBatteryLevel(): Int {
        var batteryLevel: Int
        if(VERSION.SDK_INT >= VERSION_CODES.LOLILOP) {
            batteryLevel = batteryManager.getIntPorperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
        } else {
            val intent = ContextWrapper(applicationContext).registerReceiver(null,  IntentFilter(Intent.ACTION_BATTERY_CHANGED))
            batteryLevel= intent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100 / intent.getIntExtra(BattryManager.EXTRA_SCALE, -1)
        }

        return batteryLevel
    }
}
