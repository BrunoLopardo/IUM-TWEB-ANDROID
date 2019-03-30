package project.ium.tweb.ium_tweb_android.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.ium.tweb.ium_tweb_android.R;
import project.ium.tweb.ium_tweb_android.callbacks.RipetizioniDiffUtilCallback;
import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;
import project.ium.tweb.ium_tweb_android.databinding.RowBinding;


public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private List<Ripetizione> ripetizioni;
    private ItemClickListener mClickListener;

    public MainActivityAdapter(List<Ripetizione> ripetizioni) {
        this.ripetizioni = ripetizioni;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RowBinding viewDataBinding = holder.getBinding();
        viewDataBinding.setRipetizione(getItem(position));
    }

    public void swapItems(List<Ripetizione> ripetizioni) {
        // Detect moves disabilitato per migliorare performance, si può fare dal momento che la lista è ordinata (in base al nome del corso)
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new RipetizioniDiffUtilCallback(this.ripetizioni, ripetizioni), false);
        this.ripetizioni.clear();
        this.ripetizioni.addAll(ripetizioni);
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return ripetizioni.size();
    }

    public Ripetizione getItem(int id) {
        return ripetizioni.get(id);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RowBinding binding;

        ViewHolder(RowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.executePendingBindings();
            itemView.setOnClickListener(this);
        }

        public RowBinding getBinding() {
            return binding;
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
