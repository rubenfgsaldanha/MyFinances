package pt.uc.dei.cm.myfinances.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pt.uc.dei.cm.myfinances.general.Loan;
import pt.uc.dei.cm.myfinances.general.Transaction;
import pt.uc.dei.cm.myfinances.myfinances.R;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.ViewHolder> {
    private List<Loan> loans;
    private AdapterView.OnItemClickListener onItemClickListener;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.loanDate)   //eu dps vou querer uma so text view que podera la ter ou o limit ou o date
                TextView loanDate;
        @BindView(R.id.loan_amount)
        TextView loanAmount;
        @BindView(R.id.person)
        TextView loanThirdParty;
        @BindView(R.id.loan_isLender)
        TextView loanIsLender;
        @BindView(R.id.loan_item_image)
        ImageView loanImage;
        @BindView(R.id.loan_id)
        TextView loanID;

        public ViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        @OnClick(R.id.loan_id)
        public void onClick(View view) {
            //passing the clicked position to the parent class
            onItemClickListener.onItemClick(null, view, getAdapterPosition(), view.getId());
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public LoanAdapter(Context context, AdapterView.OnItemClickListener onItemClickListener, List<Loan> loans) {
        this.loans = loans;
        this.onItemClickListener = onItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public LoanAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.loan_list_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {   //PARECE ME QUE VAI DAR MERDA!!!!!!!!!!!!!!!!1!!!!!!!!!!!!!!!!!!!!!!!!!1
        //set the data to be displayed
        if (!loans.get(position).getThirdParty().equals("")) {
            holder.loanThirdParty.setText(loans.get(position).getThirdParty());
        }
        DecimalFormat df2 = new DecimalFormat(".##");       //this is to only have 2 decimal numbers
        holder.loanAmount.setText(df2.format(loans.get(position).getLoanAmount()));
    }

    @Override
    public int getItemCount() {
        return loans.size();
    }
}