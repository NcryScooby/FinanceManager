package br.com.uniritter.financemanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import br.com.uniritter.financemanager.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}