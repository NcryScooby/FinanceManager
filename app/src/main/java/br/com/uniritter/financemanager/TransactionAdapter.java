package br.com.uniritter.financemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    Context context;
    ArrayList<TransactionModel> transactionModelArrayList;

    public TransactionAdapter(Context context, ArrayList<TransactionModel> transactionModelArrayList) {
        this.context = context;
        this.transactionModelArrayList = transactionModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.one_recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TransactionModel model = transactionModelArrayList.get(position);
        String priority = model.getType();
        if (priority.equals("Expense")){
            holder.type.setText("Saída");
            holder.type.setTextColor(context.getResources().getColor(R.color.red));
        } else {
            holder.type.setTextColor(context.getResources().getColor(R.color.green));
        }

        // add icon to category
        switch (model.getCategory()) {
            case "Alimentação":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_fastfood_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Roupas":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_shopping_bag_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Transporte":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_directions_bus_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Lazer":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_beach_access_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Educação":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_school_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Saúde":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_medical_services_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Salário":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_attach_money_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            case "Outros":
                holder.category.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_deck_24, 0, 0, 0);
                holder.category.setCompoundDrawablePadding(20);
                break;
            default:
                break;
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

        if (priority.equals("Expense")){
            holder.amount.setText("- " + currencyFormat.format(Double.parseDouble(model.getAmount())));
        } else {
            holder.amount.setText("+ " + currencyFormat.format(Double.parseDouble(model.getAmount())));
        }

        holder.date.setText(model.getDate());
        holder.note.setText(model.getNote());
        holder.category.setText(model.getCategory());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", transactionModelArrayList.get(position).getId());
                intent.putExtra("amount", transactionModelArrayList.get(position).getAmount());
                intent.putExtra("category", transactionModelArrayList.get(position).getCategory());
                intent.putExtra("note", transactionModelArrayList.get(position).getNote());
                intent.putExtra("type", transactionModelArrayList.get(position).getType());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return transactionModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView note, amount, date, category, type;
        View priority;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note = itemView.findViewById(R.id.note_one);
            amount = itemView.findViewById(R.id.amount_one);
            category = itemView.findViewById(R.id.category_one);
            date = itemView.findViewById(R.id.date_one);
            type = itemView.findViewById(R.id.type_one);
        }
    }
}
