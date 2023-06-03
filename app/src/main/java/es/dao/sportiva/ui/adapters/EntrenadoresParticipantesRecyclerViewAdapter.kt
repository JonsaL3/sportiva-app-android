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
import es.dao.sportiva.databinding.ItemEntrenadorParticipanteBinding
import es.dao.sportiva.models.entrenador.Entrenador
import es.dao.sportiva.utils.fromBase64

class EntrenadoresParticipantesRecyclerViewAdapter(
    private val onRemoveClicked: (Entrenador, ItemEntrenadorParticipanteBinding) -> Unit
) : RecyclerView.Adapter<EntrenadoresParticipantesViewHolder>() {

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
        val copylist = list.map { it.copy() }
        differ.submitList(copylist)
    }

    fun getList(): List<Entrenador> {
        return differ.currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenadoresParticipantesViewHolder {
        val binding =
            ItemEntrenadorParticipanteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntrenadoresParticipantesViewHolder(binding, onRemoveClicked)
    }

    override fun onBindViewHolder(holder: EntrenadoresParticipantesViewHolder, position: Int) {
        val inscripcion = differ.currentList[position]
        holder.bind(inscripcion)
    }

}

class EntrenadoresParticipantesViewHolder(
    private val binding: ItemEntrenadorParticipanteBinding,
    private val onRemoveClicked: (Entrenador, ItemEntrenadorParticipanteBinding) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var mEntrenador: Entrenador

    fun bind(entrenador: Entrenador) {
        mEntrenador = entrenador

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

        binding.entrenador = entrenador
        setupListeners()
    }

    private fun setupListeners() {
        binding.sivDelete.setOnClickListener { onRemoveClicked(mEntrenador, binding) }
    }

}