package es.dao.sportiva.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.dao.sportiva.databinding.ItemSesionBinding
import es.dao.sportiva.models.sesion.Sesion

class SesionesRecyclerViewAdapter : RecyclerView.Adapter<SesionesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Sesion>() {
        override fun areItemsTheSame(oldItem: Sesion, newItem: Sesion): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Sesion, newItem: Sesion): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SesionesViewHolder {
        val binding =
            ItemSesionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SesionesViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: SesionesViewHolder, position: Int) {
        val sesion = differ.currentList[position]
        holder.bind(sesion)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Sesion>) {
        differ.submitList(list)
    }

}

class SesionesViewHolder(
    val binding: ItemSesionBinding,
    val adapter: SesionesRecyclerViewAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(sesion: Sesion) {
        binding.sesion = sesion
        binding.btnApuntarme.setOnClickListener { Toast.makeText(binding.root.context, "Este botón no hace nada :D", Toast.LENGTH_LONG).show() }
        binding.btnDescartar.setOnClickListener { Toast.makeText(binding.root.context, "Este botón no hace nada :D", Toast.LENGTH_LONG).show() }
    }

}