package pt.uc.dei.cm.myfinances.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Calendar;
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

        @BindView(R.id.limit) TextView loanDate;
        @BindView(R.id.loan_amount) TextView loanAmount;
        @BindView(R.id.thirdParty) TextView loanThirdParty;
        @BindView(R.id.loan_id) TextView loanID;

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
    public void onBindViewHolder(ViewHolder holder, int position) {
        //set the data to be displayed
        holder.loanThirdParty.setText(loans.get(position).getThirdParty());
        DecimalFormat df2 = new DecimalFormat(".##");       //this is to only have 2 decimal numbers
        holder.loanAmount.setText(df2.format(loans.get(position).getLoanAmount()));
        holder.loanID.setText(String.valueOf(loans.get(position).getLoanId()));
        /*if (loans.get(position).getDuemonth()== Calendar.getInstance().get(Calendar.MONTH)) {
            holder.loanDate.setText(loans.get(position).getDateString(loans.get(position).getDueday(), loans.get(position).getDuemonth(),loans.get(position).getDueyear()));
        }
        else*/
        //if (loans.get(position).getMonth()== Calendar.getInstance().get(Calendar.MONTH)){
        holder.loanDate.setText(loans.get(position).getDateString());

        //}

    }

    @Override
    public int getItemCount() {
        return loans.size();
    }
}