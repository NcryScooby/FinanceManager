package br.com.uniritter.financemanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import br.com.uniritter.financemanager.databinding.ActivityAddTransactionBinding;

public class AddTransactionActivity extends AppCompatActivity {
    ActivityAddTransactionBinding binding;
    String type = "";
    FirebaseFirestore fStore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fStore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Alimentação", "Roupas", "Transporte", "Lazer", "Educação", "Saúde", "Salário", "Outros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items); dropdown.setAdapter(adapter);

        binding.expenseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Expense";
                binding.expenseCheckBox.setChecked(true);
                binding.incomeCheckBox.setChecked(false);
            }
        });

        binding.incomeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type = "Income";
                binding.expenseCheckBox.setChecked(false);
                binding.incomeCheckBox.setChecked(true);
            }
        });
        binding.btnAddTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String amount = binding.userAmountAdd.getText().toString().trim();
              String note = binding.userNoteAdd.getText().toString().trim();
              if (amount.length() <= 0){
                  return;
              }

              if (type.length() <= 0){
                  Toast.makeText(AddTransactionActivity.this, "Selecione um Tipo de Transação", Toast.LENGTH_SHORT).show();
                    return;
              }

                SimpleDateFormat sdf = new SimpleDateFormat("dd MM yyyy HH:mm", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());

              String id = UUID.randomUUID().toString();
              Map<String, Object> transaction = new HashMap<>();
              transaction.put("id", id);
              transaction.put("amount", amount);
              transaction.put("note", note);
              transaction.put("category", dropdown.getSelectedItem().toString());
              transaction.put("type", type);
              transaction.put("date", currentDateandTime);

              fStore.collection("expenses").document(firebaseAuth.getUid()).collection("Notes").document(id).set(transaction).addOnSuccessListener(new OnSuccessListener<Void>() {
                  @Override
                  public void onSuccess(Void unused) {
                      Toast.makeText(AddTransactionActivity.this, "Adicionado", Toast.LENGTH_SHORT).show();
                      binding.userNoteAdd.setText("");
                      binding.userAmountAdd.setText("");
                  }
              })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddTransactionActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                          }
                      });
            }
        });


    }
}