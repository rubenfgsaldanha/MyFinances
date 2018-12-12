package pt.uc.dei.cm.myfinances.adapters;

import android.content.Context;
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
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

import static java.lang.Math.round;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private List<Transaction> transactions;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.textDate) TextView transactionDate;
        @BindView(R.id.transaction_item_category) TextView transactionCategory;
        @BindView(R.id.transaction_item_comment) TextView transactionComment;
        @BindView(R.id.transaction_balance) TextView transactionBalance;
        @BindView(R.id.transaction_item_image) ImageView transactionImage;

        public ViewHolder(View v)  {
            super(v);
            ButterKnife.bind(this,v);
        }

        @OnClick(R.id.transaction_item_category)
        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }

        @Override
        public boolean onLongClick(View v) {
            onItemLongClickListener.onItemLongClick(null, v, getAdapterPosition(), v.getId());
            return true;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener,
                              AdapterView.OnItemLongClickListener onItemLongClickListener, List<Transaction> transactions) {
        this.transactions = transactions;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
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
        holder.transactionCategory.setText(transactions.get(position).getCategory());
        if(!transactions.get(position).getComment().isEmpty()){
            holder.transactionComment.setText(transactions.get(position).getComment());
        }

        holder.transactionDate.setText(transactions.get(position).getDateString());

        DecimalFormat df2 = new DecimalFormat(".##");       //this is to only have 2 decimal numbers
        holder.transactionBalance.setText(df2.format(transactions.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
