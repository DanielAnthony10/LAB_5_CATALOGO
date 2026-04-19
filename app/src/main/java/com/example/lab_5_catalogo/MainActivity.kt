package com.example.lab_5_catalogo

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtCantidad: EditText
    private lateinit var edtPrecio: EditText
    private lateinit var btnAgregar: Button
    private lateinit var btnLimpiar: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var prefs: SharedPreferences

    private val listaProductos = mutableListOf<Producto>()
    private lateinit var adapter: ProductoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtNombre = findViewById(R.id.edtNombre)
        edtCantidad = findViewById(R.id.edtCantidad)
        edtPrecio = findViewById(R.id.edtPrecio)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnLimpiar = findViewById(R.id.btnLimpiar)
        recyclerView = findViewById(R.id.recyclerViewProductos)

        prefs = getSharedPreferences("productos_prefs", MODE_PRIVATE)

        adapter = ProductoAdapter(listaProductos)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        cargarProductos()

        btnAgregar.setOnClickListener {
            guardarProducto()
        }

        btnLimpiar.setOnClickListener {
            limpiarProductos()
        }
    }

    private fun guardarProducto() {
        val nombre = edtNombre.text.toString().trim()
        val cantidadTexto = edtCantidad.text.toString().trim()
        val precioTexto = edtPrecio.text.toString().trim()

        if (nombre.isEmpty() || cantidadTexto.isEmpty() || precioTexto.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val cantidad = cantidadTexto.toIntOrNull()
        val precio = precioTexto.toDoubleOrNull()

        if (cantidad == null || precio == null) {
            Toast.makeText(this, "Cantidad o precio inválido", Toast.LENGTH_SHORT).show()
            return
        }

        val producto = Producto(nombre, cantidad, precio)
        listaProductos.add(producto)
        adapter.notifyDataSetChanged()

        guardarEnSharedPreferences()

        edtNombre.text.clear()
        edtCantidad.text.clear()
        edtPrecio.text.clear()

        Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show()
    }

    private fun guardarEnSharedPreferences() {
        val datos = listaProductos.joinToString(";") {
            "${it.nombre},${it.cantidad},${it.precio}"
        }

        prefs.edit()
            .putString("lista_productos", datos)
            .apply()
    }

    private fun cargarProductos() {
        val datosGuardados = prefs.getString("lista_productos", "")

        if (!datosGuardados.isNullOrEmpty()) {
            val productosSeparados = datosGuardados.split(";")

            for (item in productosSeparados) {
                val partes = item.split(",")
                if (partes.size == 3) {
                    val nombre = partes[0]
                    val cantidad = partes[1].toIntOrNull() ?: 0
                    val precio = partes[2].toDoubleOrNull() ?: 0.0

                    listaProductos.add(Producto(nombre, cantidad, precio))
                }
            }
        }

        if (::adapter.isInitialized) {
            adapter.notifyDataSetChanged()
        }
    }

    private fun limpiarProductos() {
        listaProductos.clear()
        adapter.notifyDataSetChanged()

        prefs.edit()
            .remove("lista_productos")
            .apply()

        Toast.makeText(this, "Catálogo limpiado", Toast.LENGTH_SHORT).show()
    }
}