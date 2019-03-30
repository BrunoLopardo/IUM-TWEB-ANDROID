package project.ium.tweb.ium_tweb_android.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import project.ium.tweb.ium_tweb_android.R;
import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;

import static java.lang.reflect.Array.getInt;


public class RecyclerViewPrenotazioneAdapter extends SectionedRecyclerViewAdapter<RecyclerView.ViewHolder> {

    private List<Categoria> modelList;
    private OnItemClickListener mItemClickListener;
    private Context context;

    public RecyclerViewPrenotazioneAdapter(Context context, List<Categoria> modelList) {
        this.modelList = modelList;
        this.context = context;
    }

    public void updateList(ArrayList<Categoria> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public int getSectionCount() {
        return modelList.size();
    }

    @Override
    public int getItemCount(int section) {
        return modelList.get(section).getList().size();
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int section) {
        String sectionName = String.valueOf(modelList.get(section).getCategoria()) + ":00";
        SectionViewHolder sectionViewHolder = (SectionViewHolder) holder;
        sectionViewHolder.nomeCategoria.setText(sectionName);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int section, int relativePosition, int absolutePosition) {
        List<Ripetizione> itemsInSection = modelList.get(section).getList();
        String itemTitle = itemsInSection.get(relativePosition).getDocente().getNome() + " " + itemsInSection.get(relativePosition).getDocente().getCognome();
        ViewHolder itemViewHolder = (ViewHolder) holder;
        itemViewHolder.nomeProf.setText(itemTitle);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, boolean header) {
        if (header) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_section, parent, false);
            return new SectionViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.item_recycler_list, parent, false);
            return new ViewHolder(view);
        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Ripetizione ripetizione);
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {

        final TextView nomeCategoria;

        public SectionViewHolder(View itemView) {
            super(itemView);
            nomeCategoria = itemView.findViewById(R.id.nome_categoria);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        final TextView nomeProf;

        public ViewHolder(View itemView) {
            super(itemView);
            nomeProf = itemView.findViewById(R.id.nome_prof);
            itemView.setOnClickListener(view -> {
                int position = getInt(getSectionIndexAndRelativePosition(getAdapterPosition()), 1);
                int categoria = getInt(getSectionIndexAndRelativePosition(getAdapterPosition()), 0);
                mItemClickListener.onItemClick(itemView, position, modelList.get(categoria).getList().get(position));
            });
        }
    }

    public static class Categoria {
        private int orario;
        private List<Ripetizione> list;

        public Categoria(int orario, List<Ripetizione> list) {
            this.orario = orario;
            this.list = list;
        }

        public int getCategoria() {
            return orario;
        }

        public List<Ripetizione> getList() {
            return list;
        }
    }
}

