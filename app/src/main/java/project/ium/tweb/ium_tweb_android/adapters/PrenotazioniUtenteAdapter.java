package project.ium.tweb.ium_tweb_android.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import project.ium.tweb.ium_tweb_android.R;
import project.ium.tweb.ium_tweb_android.callbacks.PrenotazioniUtenteDiffUtilCallback;
import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;
import project.ium.tweb.ium_tweb_android.databinding.RowPrenotazioneBinding;

public class PrenotazioniUtenteAdapter extends RecyclerView.Adapter<PrenotazioniUtenteAdapter.ViewHolder>{

    private List<Prenotazione> prenotazioniUtente;

    public  PrenotazioniUtenteAdapter(List<Prenotazione> prenotazioniUtente){ this.prenotazioniUtente = prenotazioniUtente; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowPrenotazioneBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_prenotazione, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RowPrenotazioneBinding viewDataBinding = holder.getBinding();
        viewDataBinding.setPrenotazione(getItem(position));
    }

    public void swapItems(List<Prenotazione> prenotazioniUtente) {
        // Detect moves disabilitato per migliorare performance, si può fare dal momento che la lista è ordinata (in base a created_at)
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PrenotazioniUtenteDiffUtilCallback(this.prenotazioniUtente, prenotazioniUtente), false);
        this.prenotazioniUtente.clear();
        this.prenotazioniUtente.addAll(prenotazioniUtente);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return prenotazioniUtente.size();
    }

    public Prenotazione getItem(int id) {
        return prenotazioniUtente.get(id);
    }

    public void cancelPrenotazioneUtente(int position){
        prenotazioniUtente.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        RowPrenotazioneBinding binding;
        public RelativeLayout viewBackground;
        public LinearLayout viewForeground;

        ViewHolder(RowPrenotazioneBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            viewBackground = binding.viewBackground;
            viewForeground = binding.viewForeground;
            binding.executePendingBindings();
        }

        public RowPrenotazioneBinding getBinding() {
            return binding;
        }

    }

}
