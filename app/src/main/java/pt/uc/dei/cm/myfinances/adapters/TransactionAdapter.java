package pt.uc.dei.cm.myfinances.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private ArrayList<Transaction> transactions;
    private AdapterView.OnItemClickListener onItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView transactionText;
        private TextView transactionBalance;
        private ImageView transactionImage;

        public ViewHolder(View v)  {
            super(v);

            transactionText = v.findViewById(R.id.transaction_item_text);
            transactionBalance = v.findViewById(R.id.transaction_balance);
            transactionImage = v.findViewById(R.id.transaction_item_image);

            transactionText.setOnClickListener(this::onClick);
        }

        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public TransactionAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener, ArrayList<Transaction> transactions) {
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
        if(!transactions.get(position).getComment().equals("")){
            holder.transactionText.setText(transactions.get(position).getComment());
        }
        else{
            holder.transactionText.setText(transactions.get(position).getCategory());
        }
        holder.transactionBalance.setText(String.valueOf(transactions.get(position).getAmount()));
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
