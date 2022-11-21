package vn.edu.greenwich.cw1.ui.tripitem.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import android.widget.Button;

import vn.edu.greenwich.cw1.R;
import vn.edu.greenwich.cw1.database.TripEntry;
import vn.edu.greenwich.cw1.database.TripItemEntry;
import vn.edu.greenwich.cw1.models.TripItem;
import vn.edu.greenwich.cw1.database.TripDAO;
import vn.edu.greenwich.cw1.ui.trip.TripDetailFragment;
import vn.edu.greenwich.cw1.models.Trip;
public class TripItemAdapter extends RecyclerView.Adapter<TripItemAdapter.ViewHolder> implements Filterable {
    protected ArrayList<TripItem> _originalList;
    protected ArrayList<TripItem> _filteredList;
    protected TripItemAdapter.ItemFilter _itemFilter = new TripItemAdapter.ItemFilter();
    protected Button deleteid;
    protected TripDAO _db;
    String ARG_TRIP_ID ="trip";
    FragmentManager fragmentManager;
    TripDetailFragment frg = new TripDetailFragment();

    public TripItemAdapter(ArrayList<TripItem> list,FragmentManager fragmentManager) {
        _originalList = list;
        _filteredList = list;

        this.fragmentManager =fragmentManager;

    }



    public void updateList(ArrayList<TripItem> list) {
        _originalList = list;
        _filteredList = list;

        notifyDataSetChanged();
    }
    public void addcontext(Context context){

    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_tripitem, parent, false);
        deleteid =view.findViewById(R.id.DeleteTripItem);
        _db = new TripDAO(view.getContext());
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TripItem tripItem = _filteredList.get(position);

        holder.listItemTripItemDate.setText(tripItem.getDate());
        holder.listItemTripItemTime.setText(tripItem.getTime());
        holder.listItemTripItemType.setText(tripItem.getType());
        holder.listItemTripItemContent.setText(tripItem.getContent());
        holder.listItemTripItemAmount.setText(String.valueOf(tripItem.getamount())+" VND");
        deleteid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Trip trip = new Trip();
                trip.setId(tripItem.getTripId());
                String query ="DELETE FROM "+ TripItemEntry.TABLE_NAME+ " WHERE "+TripItemEntry.COL_ID+" = "+tripItem.getId();
                String query1 ="UPDATE "+ TripEntry.TABLE_NAME +" SET "+TripEntry.COL_Current_Amount+" = "+TripEntry.COL_Current_Amount +" - "+ tripItem.getamount()+" WHERE "+TripEntry.COL_ID+" = " +tripItem.getTripId();
                _db.updatedb(query);
                _db.updatedb(query1);
                Bundle args = new Bundle();
                args.putSerializable(ARG_TRIP_ID,trip);
                frg.setArguments(args);
                fragmentManager.beginTransaction()
                        .replace(R.id.frg_trip_detail,frg)
                        .detach(frg)
                        .attach(frg)
                        .commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return _filteredList == null ? 0 : _filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return _itemFilter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView listItemTripItemDate, listItemTripItemTime, listItemTripItemType, listItemTripItemContent,listItemTripItemAmount;

        public ViewHolder(View itemView) {
            super(itemView);

            listItemTripItemDate = itemView.findViewById(R.id.listItemTripItemDate);
            listItemTripItemTime = itemView.findViewById(R.id.listItemTripItemTime);
            listItemTripItemType = itemView.findViewById(R.id.listItemITripItemType);
            listItemTripItemContent = itemView.findViewById(R.id.listItemTripItemContent);
            listItemTripItemAmount = itemView.findViewById(R.id.itemamount);
        }

    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final ArrayList<TripItem> list = _originalList;
            final ArrayList<TripItem> nlist = new ArrayList<>(list.size());

            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();

            for (TripItem tripItem : list) {
                String filterableString = tripItem.toString();

                if (filterableString.toLowerCase().contains(filterString))
                    nlist.add(tripItem);
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            _filteredList = (ArrayList<TripItem>) results.values;
            notifyDataSetChanged();
        }


    }
}