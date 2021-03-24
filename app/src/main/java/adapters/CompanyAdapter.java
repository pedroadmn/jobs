package adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import models.Company;
import pedroadmn.com.companyjobs.R;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.MyViewHolder> {

    private List<Company> companies = new ArrayList<>();
    private Context context;

    public CompanyAdapter(Context context, List<Company> companies) {
        this.context = context;
        this.companies = companies;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_item_adapter, parent, false);
        return new CompanyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Company company = companies.get(position);
//        holder.companyName.setText(company.getName());

        Glide.with(context).load(company.getCover()).into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView companyName;
        TextView companyCategory;
        TextView jobsOpportunityUrl;
        ImageView cover;

        public MyViewHolder(View itemView) {
            super(itemView);

//            companyName = itemView.findViewById(R.id.tvCompanyName);
            cover = itemView.findViewById(R.id.ivCover);
        }
    }
}
