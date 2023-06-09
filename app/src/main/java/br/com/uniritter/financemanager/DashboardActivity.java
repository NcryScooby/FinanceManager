package br.com.uniritter.financemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.NumberFormat;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Locale;

import br.com.uniritter.financemanager.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    double sumExpense = 0;
    double sumIncome = 0;
    double total = 0;
    ArrayList<TransactionModel> transactionModelArrayList;
    TransactionAdapter transactionAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        transactionModelArrayList = new ArrayList<>();
        binding.historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.historyRecyclerView.setHasFixedSize(true);

        firebaseAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null){
                    startActivity(new Intent(DashboardActivity.this, MainActivity.class));
                    finish();
                }
            }
        });

        binding.signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createSignOutDialog();
            }
        });

        binding.addFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(DashboardActivity.this, AddTransactionActivity.class));
                } catch (Exception e) {

                }
            }
        });

        binding.refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivity(new Intent(DashboardActivity.this, DashboardActivity.class));
                    finish();
                } catch (Exception e) {
            }
        }
        });
        loadData();
    }

    private void createSignOutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle("Sair").setMessage("Tem certeza que deseja sair?").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
            }
        }).setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
    firebaseFirestore.collection("expenses").document(firebaseAuth.getUid()).collection("Notes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    transactionModelArrayList.clear();

                    sumExpense = 0.0;
                    sumIncome = 0.0;
                    total = 0.0;

                    for (DocumentSnapshot ds: task.getResult()){
                        TransactionModel model = new TransactionModel(
                                ds.getString("id"),
                                ds.getString("note"),
                                ds.getString("amount"),
                                ds.getString("category"),
                                ds.getString("type"),
                                ds.getString("date"));
                        double amount = Double.parseDouble(ds.getString("amount"));
                        if (ds.getString("type").equals("Expense")){
                            sumExpense += amount;
                        } else {
                            sumIncome += amount;
                        }
                        transactionModelArrayList.add(model);
                    }

                    System.out.println(sumExpense);
                    System.out.println(sumIncome);

                    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

                    total = sumIncome - sumExpense;

                    binding.totalIncome.setText("+ " + currencyFormat.format(sumIncome));
                    binding.totalExpense.setText("- " + currencyFormat.format(sumExpense));
                    binding.totalBalance.setText(
                            total < 0 ? (
                                    "- " + currencyFormat.format(total).replace("-", "")
                            ) : String.valueOf(currencyFormat.format(total))
                    );

                    transactionAdapter = new TransactionAdapter(DashboardActivity.this, transactionModelArrayList);
                    binding.historyRecyclerView.setAdapter(transactionAdapter);
                }
            });
    }
}