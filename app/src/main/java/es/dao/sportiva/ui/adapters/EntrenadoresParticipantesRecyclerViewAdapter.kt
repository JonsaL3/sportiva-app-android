package es.dao.sportiva.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import es.dao.sportiva.databinding.ItemEntrenadorParticipanteBinding
import es.dao.sportiva.models.entrenador.Entrenador

class EntrenadoresParticipantesRecyclerViewAdapter : RecyclerView.Adapter<EntrenadoresParticipantesViewHolder>() {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenadoresParticipantesViewHolder {
        val binding =
            ItemEntrenadorParticipanteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EntrenadoresParticipantesViewHolder(binding, this)
    }

    override fun onBindViewHolder(holder: EntrenadoresParticipantesViewHolder, position: Int) {
        val inscripcion = differ.currentList[position]
        holder.bind(inscripcion)
    }

}

class EntrenadoresParticipantesViewHolder(
    private val binding: ItemEntrenadorParticipanteBinding,
    private val adapter: EntrenadoresParticipantesRecyclerViewAdapter
) : RecyclerView.ViewHolder(binding.root) {

    private lateinit var mEntrenador: Entrenador

    fun bind(entrenador: Entrenador) {
        mEntrenador = entrenador
        binding.entrenador = entrenador
        setupListeners()
    }

    private fun setupListeners() {
        binding.sivDelete.setOnClickListener { borrarRow() }
    }

    private fun borrarRow() {
        val aux = adapter.getList().map { it.copy() } as MutableList
        aux.removeIf { it.id == mEntrenador.id }
        adapter.submitList(aux)
    }

}