package es.dao.sportiva.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.dao.sportiva.databinding.ItemEntrenadorBinding
import es.dao.sportiva.databinding.ItemSesionBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.models.sesion.Sesion

class EntrenadoresRecyclerViewAdapter : RecyclerView.Adapter<EntrenadoresViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<Entrenador>() {
        override fun areItemsTheSame(oldItem: Entrenador, newItem: Entrenador): Boolean {
            return  oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Entrenador, newItem: Entrenador): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenadoresViewHolder {
        val binding =
            ItemEntrenadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntrenadoresViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: EntrenadoresViewHolder, position: Int) {
        val sesion = differ.currentList[position]
        holder.bind(sesion)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Entrenador>) {
        differ.submitList(list)
    }

}

class EntrenadoresViewHolder(
    val binding: ItemEntrenadorBinding,
    val adapter: EntrenadoresRecyclerViewAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entrenador: Entrenador) {
        binding.entrenador = entrenador
        "Entrenador asignado a: ${entrenador.empresaAsignada.nombre}".also { binding.tvEntrenadorEmpresa.text = it }
    }

}