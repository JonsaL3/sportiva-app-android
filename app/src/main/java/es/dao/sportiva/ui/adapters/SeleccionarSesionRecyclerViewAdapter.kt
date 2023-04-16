package es.dao.sportiva.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.dao.sportiva.databinding.ItemSeleccionarSesionBinding
import es.dao.sportiva.models.sesion.Sesion

class SeleccionarSesionRecyclerViewAdapter(
    private var onSessionSelected: (Sesion) -> Unit
) : RecyclerView.Adapter<SeleccionarSesionViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeleccionarSesionViewHolder {
        val binding =
            ItemSeleccionarSesionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeleccionarSesionViewHolder(binding, onSessionSelected)
    }

    override fun onBindViewHolder(holder: SeleccionarSesionViewHolder, position: Int) {
        val sesion = differ.currentList[position]
        holder.bind(sesion)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Sesion>) {
        differ.submitList(list.map { it.copy() })
    }

}

class SeleccionarSesionViewHolder(
    private val binding: ItemSeleccionarSesionBinding,
    private val onSessionSelected: (Sesion) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(sesion: Sesion) {
        binding.sesion = sesion
        binding.cvSesion.setOnClickListener { onSessionSelected.invoke(sesion) }
    }

}