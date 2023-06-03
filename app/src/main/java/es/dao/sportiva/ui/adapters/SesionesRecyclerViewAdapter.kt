package es.dao.sportiva.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.dxcustomlibrary.gone
import com.example.dxcustomlibrary.visible
import es.dao.sportiva.R
import es.dao.sportiva.databinding.ItemSesionBinding
import es.dao.sportiva.models.sesion.Sesion
import es.dao.sportiva.utils.Constantes
import es.dao.sportiva.utils.fromBase64

class SesionesRecyclerViewAdapter(
    private val onApuntarse: (binding: ItemSesionBinding, sesion: Sesion) -> Unit,
    private val onDesapuntarse: (binding: ItemSesionBinding, sesion: Sesion) -> Unit
) : RecyclerView.Adapter<SesionesViewHolder>() {

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
        return SesionesViewHolder(binding, this, onApuntarse, onDesapuntarse)
    }

    override fun onBindViewHolder(holder: SesionesViewHolder, position: Int) {
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

class SesionesViewHolder(
    val binding: ItemSesionBinding,
    val adapter: SesionesRecyclerViewAdapter,
    private val onApuntarse: (binding: ItemSesionBinding, sesion: Sesion) -> Unit,
    private val onDesapuntarse: (binding: ItemSesionBinding, sesion: Sesion) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(sesion: Sesion) {
        binding.sesion = sesion

        // Pinto el aforo en caso de que no sea ilimitado
        if (sesion.aforoMaximo == Constantes.AFORO_ILIMITADO) {
            binding.tvAforo.text = binding.root.context.getString(R.string.aforo_ilimitado)
        } else {
            binding.tvAforo.text = "Aforo: ${sesion.numeroDeInscripciones} / ${sesion.aforoMaximo}"
        }

        // Pinto las imagenes, la del entrenador y la de la sesión
        sesion.imagen?.let {
            try {
                binding.sivSesion.setImageBitmap(it.fromBase64())
            } catch (e: Exception) {
                binding.sivSesion.visibility = View.GONE
            }
        } ?: run {
            binding.sivSesion.visibility = View.GONE
        }

        sesion.creador.imagen?.let {
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

        // Pinto la targeta de verde si el empleado ya esta inscrito a esa sesion
        if (sesion.isCurrentEmpleadoInscrito) {
            setModeInscrito(sesion)
        }

        // Por ultimo se setean los listeners
        binding.btnApuntarse.setOnClickListener { onApuntarse(binding, sesion) }
        binding.btnDesapuntarse.setOnClickListener { onDesapuntarse(binding, sesion) }
    }

    private fun setModeInscrito(sesion: Sesion) {

        binding.mcvSesion.strokeColor = ResourcesCompat.getColor(
            binding.root.resources,
            R.color.green_sportiva,
            null
        )
        binding.btnApuntarse.gone()
        binding.btnDesapuntarse.visible()

        sesion.creador.imagen?:run {
            binding.sivFotoCreador.setColorFilter(
                ResourcesCompat.getColor(
                    binding.root.resources,
                    R.color.green_sportiva,
                    null
                )
            )
        }

    }

}