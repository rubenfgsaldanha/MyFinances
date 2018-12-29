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
import pt.uc.dei.cm.myfinances.general.Wallet;
import pt.uc.dei.cm.myfinances.myfinances.R;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder> {
    private List<Wallet> wallets;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.wallet_item_text) TextView walletText;
        @BindView(R.id.wallet_balance) TextView walletBalance;
        @BindView(R.id.wallet_item_image) ImageView walletImage;

        public ViewHolder(View v)  {
            super(v);
            ButterKnife.bind(this,v);
        }

        @OnClick(R.id.wallet_item_text)
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
    public WalletAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener,
                         AdapterView.OnItemLongClickListener onItemLongClickListener, List<Wallet> wallets) {
        this.wallets = wallets;
        this.onItemClickListener = onItemClickListener;
        this.onItemLongClickListener = onItemLongClickListener;
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
        DecimalFormat df2 = new DecimalFormat(".##");       //this is to only have 2 decimal numbers
        holder.walletText.setText(wallets.get(position).getName());
        holder.walletBalance.setText(df2.format(wallets.get(position).getBalance()));

        if(wallets.get(position).getBalance() < 0){
            holder.walletImage.setColorFilter(Color.RED);
        }
        else{
            holder.walletImage.setColorFilter(Color.GREEN);
        }
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }
}
