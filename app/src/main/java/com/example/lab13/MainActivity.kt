package com.example.lab13

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val urlBase = "http://192.168.215.247:8000/"

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val policy =
            StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val x_id: EditText = findViewById(R.id.edtID)
        val x_name: EditText = findViewById(R.id.edtName)
        val x_release_date: EditText = findViewById(R.id.edtReleaseDate)
        val x_rating: EditText = findViewById(R.id.edtRating)
        val x_category: EditText = findViewById(R.id.edtCategory)
        val x_liista_series: ListView = findViewById(R.id.lvSeries)

        val btnSelect = findViewById<Button>(R.id.btnSelect)
        val btnInsert = findViewById<Button>(R.id.btnInsert)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val retrofit = Retrofit.Builder().baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(SerieApiService::class.java)
        val context: Context = this

        btnSelect.setOnClickListener {
            lifecycleScope.launch {
                val xid = x_id.text.toString()
                if (xid.trim().isNotEmpty()) {
                    val rpta = service.selectSerie(xid)
                    runOnUiThread {
                        if (rpta.isSuccessful) {
                            x_name.setText(rpta.body()?.name.toString())
                            x_release_date.setText(rpta.body()?.release_date.toString())
                            x_rating.setText(rpta.body()?.rating.toString())
                            x_category.setText(rpta.body()?.category.toString())
                        } else {
                            Toast.makeText(
                                context,
                                "Error: NO EXISTE EL ID ${x_id.text.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    val rpta = service.selectSeries()
                    if (rpta.isSuccessful) {
                        runOnUiThread {
                            val listaSeries = rpta.body() ?: emptyList()
                            val adapter = SerieListAdapter(context, listaSeries)
                            x_liista_series.adapter = adapter
                        }
                    }
                }
            }
        }
            btnInsert.setOnClickListener {
                val serie = SerieModel(
                    id = 0,
                    name = x_name.text.toString(),
                    release_date = x_release_date.text.toString(),
                    rating = x_rating.text.toString().toInt(),
                    category = x_category.text.toString()
                )
                lifecycleScope.launch {
                    val rpta = service.insertSerie(serie)
                    var msg = "ERROR al agregar el Registro"
                    if (rpta.isSuccessful) {
                        msg =
                            "Registro Grabado correctamente! nuevo id = ${rpta.body()?.id.toString()}"
                    }
                    runOnUiThread {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnUpdate.setOnClickListener {
                val serie = SerieModel(
                    id = x_id.text.toString().toInt(),
                    name = x_name.text.toString(),
                    release_date = x_release_date.text.toString(),
                    rating = x_rating.text.toString().toInt(),
                    category = x_category.text.toString()
                )
                lifecycleScope.launch {
                    val rpta = service.updateSerie(serie.id.toString(), serie)
                    var msg = "ERROR al actualizar el Registro"
                    if (rpta.isSuccessful) {
                        msg = "Registro Actualizado correctamente!"
                    }
                    runOnUiThread {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            btnDelete.setOnClickListener {
                lifecycleScope.launch {
                    val xid = x_id.text.toString()
                    if (xid.trim().isNotEmpty()) {
                        val rpta = service.deleteSerie(xid)
                        runOnUiThread {
                            var msg = "ERROR al eliminar el Registro de ID:{$xid}"
                            if (rpta.isSuccessful)
                                msg = "Registro Eliminado correctamente!"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
