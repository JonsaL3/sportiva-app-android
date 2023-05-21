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
import es.dao.sportiva.databinding.ItemSeleccionarEntrenadorBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.utils.fromBase64

class SeleccionarEntrenadoresRecyclerViewAdapter(
    private val onCheckedChanged: (Entrenador, Boolean) -> Unit
) : RecyclerView.Adapter<SeleccionarEntrenadoresViewHolder>() {

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

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Entrenador>) {
        differ.submitList(list)
    }

    fun getList(): List<Entrenador> {
        return differ.currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeleccionarEntrenadoresViewHolder {
        val binding =
            ItemSeleccionarEntrenadorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SeleccionarEntrenadoresViewHolder(binding, onCheckedChanged)
    }

    override fun onBindViewHolder(holder: SeleccionarEntrenadoresViewHolder, position: Int) {
        val inscripcion = differ.currentList[position]
        holder.bind(inscripcion)
    }

}

class SeleccionarEntrenadoresViewHolder(
    private val binding: ItemSeleccionarEntrenadorBinding,
    private val onCheckedChanged: (Entrenador, Boolean) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(entrenador: Entrenador) {
        binding.entrenador = entrenador

        entrenador.imagen?.let {
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

        // si no toco la checkbox que esta cambie igual
        binding.mcvEntrenador.setOnClickListener {
            binding.chckBox.isChecked = !binding.chckBox.isChecked
        }

        // Cuando presiono la checkbox
        binding.chckBox.addOnCheckedStateChangedListener { checkBox, state ->
            onCheckedChanged(entrenador, checkBox.isChecked)
        }

    }

}