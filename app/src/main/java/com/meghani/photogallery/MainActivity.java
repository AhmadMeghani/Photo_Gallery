package com.meghani.photogallery;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import com.meghani.photogallery.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Navigation.findNavController(findViewById(R.id.fragment)).addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getLabel().toString().contains("main")) {
                    binding.backBtn.setVisibility(View.GONE);
                    binding.searchBar.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH && !v.getText().toString().trim().equals("")) {
                    if (!Navigation.findNavController(findViewById(R.id.fragment)).getCurrentDestination().getLabel().equals("fragment_search")) {
                        MainFragmentDirections.ActionMainFragmentToSearchFragment action =
                                MainFragmentDirections.actionMainFragmentToSearchFragment();
                        action.setSearchQuery(v.getText().toString());
                        Navigation.findNavController(findViewById(R.id.fragment)).navigate(action);

                    } else {
                        SearchFragment searchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.fragment).getChildFragmentManager().getPrimaryNavigationFragment();
                        searchFragment.initRecyclerviewAndAdapter(v.getText().toString());
                    }
                    binding.backBtn.setVisibility(View.VISIBLE);
                    binding.searchBar.clearFocus();
                    hideSoftKeyboard(v);
                }
                return false;
            }
        });

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(findViewById(R.id.fragment)).navigate(R.id.favoritesFragment);
            }
        });

    }

    private void hideSoftKeyboard(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}