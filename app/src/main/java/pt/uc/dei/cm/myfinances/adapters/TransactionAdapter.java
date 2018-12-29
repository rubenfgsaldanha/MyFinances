package pt.uc.dei.cm.myfinances.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.MyFinancesApplication;
import pt.uc.dei.cm.myfinances.general.Categories;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static java.lang.Math.round;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private AdapterView.OnItemClickListener onItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.textDate) TextView transactionDate;
        @BindView(R.id.transaction_item_category) TextView transactionCategory;
        @BindView(R.id.transaction_item_comment) TextView transactionComment;
        @BindView(R.id.transaction_balance) TextView transactionBalance;
        @BindView(R.id.transaction_item_image) ImageView transactionImage;
        @BindView(R.id.transaction_id) TextView transactionID;

        public ViewHolder(View v)  {
            super(v);
            ButterKnife.bind(this,v);
        }

        @OnClick(R.id.transaction_id)
        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener, List<Transaction> transactions) {
        this.transactions = transactions;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set the data to be displayed
        if(!transactions.get(position).getComment().isEmpty()){
            holder.transactionComment.setText(transactions.get(position).getComment());
        }
        else{
            holder.transactionCategory.setText(transactions.get(position).getCategory());
        }

        holder.transactionDate.setText(transactions.get(position).getDateString());
        holder.transactionID.setText(String.valueOf(transactions.get(position).getId()));

        DecimalFormat df2 = new DecimalFormat(".##");       //this is to only have 2 decimal numbers
        holder.transactionBalance.setText(df2.format(transactions.get(position).getAmount()));

        if(transactions.get(position).getAmount() < 0){
            holder.transactionImage.setColorFilter(Color.RED);
        }
        else{
            holder.transactionImage.setColorFilter(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
