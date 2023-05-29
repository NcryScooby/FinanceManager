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
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.uniritter.financemanager.databinding.ActivityUpdateBinding;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    String newType;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        Spinner dropdown = findViewById(R.id.spinner1);
        String[] items = new String[]{"Alimentação", "Roupas", "Transporte", "Lazer", "Educação", "Saúde", "Salário", "Outros"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items); dropdown.setAdapter(adapter);

        String id = getIntent().getStringExtra("id");
        String amount = getIntent().getStringExtra("amount");
        String note = getIntent().getStringExtra("note");
        String category = getIntent().getStringExtra("category");
        String type = getIntent().getStringExtra("type");

        binding.userAmountAdd.setText(amount);
        binding.userNoteAdd.setText(note);
        dropdown.setSelection(adapter.getPosition(category));

        switch (type) {
            case "Income":
                newType = "Income";
                binding.incomeCheckBox.setChecked(true);
                break;
            case "Expense":
                newType = "Expense";
                binding.expenseCheckBox.setChecked(true);
                break;
        }
        binding.incomeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newType = "Income";
                binding.incomeCheckBox.setChecked(true);
                binding.expenseCheckBox.setChecked(false);
            }
        });

        binding.expenseCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newType = "Expense";
                binding.expenseCheckBox.setChecked(true);
                binding.incomeCheckBox.setChecked(false);
            }
        });

        binding.btnDeleteTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("expenses").document(firebaseAuth.getUid())
                        .collection("Notes")
                        .document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(UpdateActivity.this, "Deletado com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.btnUpdateTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amount = binding.userAmountAdd.getText().toString();
                String note = binding.userNoteAdd.getText().toString();
                String category = dropdown.getSelectedItem().toString();

                firebaseFirestore.collection("expenses").document(firebaseAuth.getUid())
                        .collection("Notes").document(id).update("amount", amount, "note", note, "type", newType, "category", category)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                onBackPressed();
                                Toast.makeText(UpdateActivity.this, "Atualizado com sucesso", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}