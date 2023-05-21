package es.dao.sportiva.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dxcustomlibrary.gone
import com.example.dxcustomlibrary.visible
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ItemInscripcionBinding
import es.dao.sportiva.models.empleado_inscribe_sesion.EmpleadoInscribeSesion
import es.dao.sportiva.utils.fromBase64

class InscripcionesRecyclerViewAdapter : RecyclerView.Adapter<InscripcionesViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<EmpleadoInscribeSesion>() {
        override fun areItemsTheSame(oldItem: EmpleadoInscribeSesion, newItem: EmpleadoInscribeSesion): Boolean {
            return  oldItem.id == newItem.id
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: EmpleadoInscribeSesion, newItem: EmpleadoInscribeSesion): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, differCallback)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<EmpleadoInscribeSesion>) {
        differ.submitList(list.map { it.copy() })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InscripcionesViewHolder {
        val binding =
            ItemInscripcionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InscripcionesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InscripcionesViewHolder, position: Int) {
        val inscripcion = differ.currentList[position]
        holder.bind(inscripcion)
    }

}

class InscripcionesViewHolder(
    private val binding: ItemInscripcionBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(inscripcion: EmpleadoInscribeSesion) {
        binding.inscripcion = inscripcion

        inscripcion.empleadoInscrito.imagen?.let {
            try {
                binding.sivFotoCreador.setImageBitmap(it.fromBase64())
                binding.tvInicial.gone()
            } catch (e: Exception) {
                binding.sivFotoCreador.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        binding.root.resources,
                        R.drawable.default_circle_shape,
                        null
                    )
                )
                binding.tvInicial.visible()
            }
        } ?: run {
            binding.sivFotoCreador.setImageDrawable(
                ResourcesCompat.getDrawable(
                    binding.root.resources,
                    R.drawable.default_circle_shape,
                    null
                )
            )
            binding.tvInicial.visible()
        }

    }

}