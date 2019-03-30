package project.ium.tweb.ium_tweb_android.callbacks;

import android.support.v7.util.DiffUtil;

import java.util.List;

import project.ium.tweb.ium_tweb_android.dao.dto.Prenotazione;

public class PrenotazioniUtenteDiffUtilCallback extends DiffUtil.Callback {
    private List<Prenotazione> oldList;
    private List<Prenotazione> newList;

    public PrenotazioniUtenteDiffUtilCallback(List<Prenotazione> oldList, List<Prenotazione> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Prenotazione oldItem = oldList.get(oldItemPosition);
        Prenotazione newItem = newList.get(newItemPosition);

        return oldItem.equals(newItem);
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).identical(newList.get(newItemPosition));
    }
}
