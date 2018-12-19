package pt.uc.dei.cm.myfinances.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import pt.uc.dei.cm.myfinances.adapters.LoanAdapter;
import pt.uc.dei.cm.myfinances.myfinances.R;



public class LoanFragment extends androidx.fragment.app.Fragment implements AdapterView.OnItemClickListener {

    @BindView(R.id.loanFragment) View mRootView;
    @BindView(R.id.recycle_view_loans)
    RecyclerView loansList;
    @BindView(R.id.fab_add_loan)
    FloatingActionButton fabAddLoam;
    @BindView(R.id.past_month)
    ImageButton pastsMonth;
    @BindView(R.id.following_month) ImageButton followingMonth;
    @BindView(R.id.now_month) TextView nowMonth;
    private RecyclerView.LayoutManager mLayoutManager;
    private LoanAdapter adapter;


    private OnFragmentInteractionListener mListener;

    public LoanFragment() {
        // Required empty public constructor


    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_loan, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
