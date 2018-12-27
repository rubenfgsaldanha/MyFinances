package pt.uc.dei.cm.myfinances.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static android.app.Activity.RESULT_OK;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private List<Categories> categories;
    private AdapterView.OnItemClickListener onItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.label_item)
        TextView label;
        @BindView(R.id.diplay_color)
        Button dispC;
        @BindView(R.id.category_id)
        TextView catId;
        @BindView(R.id.cat_item_image)
        ImageView catImag;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        @OnClick(R.id.category_id)
        public void onClick(View v) {
            onItemClickListener.onItemClick(null, v, getAdapterPosition(), v.getId());


        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoryAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener, List<Categories> categories) {
        this.categories = categories;
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
// create a new view
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set the data to be displayed


        holder.label.setText(categories.get(position).getLabel());
        holder.dispC.setBackgroundColor(categories.get(position).getColor());
        holder.catId.setText(String.valueOf(categories.get(position).getCatId()));



    }



    @Override
    public int getItemCount() {
        return categories.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

}