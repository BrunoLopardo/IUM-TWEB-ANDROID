package project.ium.tweb.ium_tweb_android.callbacks;

import android.support.v7.util.DiffUtil;

import java.util.List;

import project.ium.tweb.ium_tweb_android.dao.dto.Ripetizione;

public class RipetizioniDiffUtilCallback extends DiffUtil.Callback {

    private List<Ripetizione> oldList;
    private List<Ripetizione> newList;

    public RipetizioniDiffUtilCallback(List<Ripetizione> oldList, List<Ripetizione> newList) {
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
        Ripetizione oldItem = oldList.get(oldItemPosition);
        Ripetizione newItem = newList.get(newItemPosition);

        boolean result = oldItem.getCorso().getID().equals(newItem.getCorso().getID());
        result = result && oldItem.getDocente().getID().equals(newItem.getDocente().getID());
        result = result && oldItem.getGiorno().equals(newItem.getGiorno());
        result = result && oldItem.getOraInizio().equals(newItem.getOraInizio());

        return result;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).identical(newList.get(newItemPosition));
    }
}
