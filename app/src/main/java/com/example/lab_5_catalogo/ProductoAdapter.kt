package com.example.lab_5_catalogo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProductoAdapter(
    private val listaProductos: List<Producto>
) : RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder>() {

    class ProductoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNombre: TextView = itemView.findViewById(R.id.txtNombreProducto)
        val txtCantidad: TextView = itemView.findViewById(R.id.txtCantidadProducto)
        val txtPrecio: TextView = itemView.findViewById(R.id.txtPrecioProducto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_producto, parent, false)
        return ProductoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductoViewHolder, position: Int) {
        val producto = listaProductos[position]
        holder.txtNombre.text = "Nombre: ${producto.nombre}"
        holder.txtCantidad.text = "Cantidad: ${producto.cantidad}"
        holder.txtPrecio.text = "Precio: S/ ${"%.2f".format(producto.precio)}"
    }

    override fun getItemCount(): Int = listaProductos.size
}