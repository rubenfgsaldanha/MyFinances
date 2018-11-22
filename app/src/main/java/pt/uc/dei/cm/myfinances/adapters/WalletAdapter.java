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
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private ArrayList<Wallet> wallets;
    private AdapterView.OnItemClickListener onItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView walletText;
        private TextView walletBalance;
        private ImageView walletImage;

        public ViewHolder(View v)  {
            super(v);

            walletText = v.findViewById(R.id.wallet_item_text);
            walletBalance = v.findViewById(R.id.wallet_balance);
            walletImage = v.findViewById(R.id.wallet_item_image);

            walletText.setOnClickListener(this::onClick);
        }

        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WalletAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener, ArrayList<Wallet> wallets) {
        this.wallets = wallets;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WalletAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wallet_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set the data to be displayed
        holder.walletText.setText(wallets.get(position).getName());
        holder.walletBalance.setText(String.valueOf(wallets.get(position).getBalance()));
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }
}
