package com.meghani.photogallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.tabs.TabLayoutMediator;
import com.meghani.photogallery.databinding.MainFragmentBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainFragment extends Fragment {
    MainFragmentBinding binding;

    private final List<String> TABS_TITLE_LIST = new ArrayList<>(Arrays.asList("Editor's Choice", "Backgrounds", "Fashion", "Nature", "Education", "Feelings", "Health", "People", "Religion",
            "Places", "Animals", "Industry", "Computer", "Food", "Sports", "Transportation", "Travel",
            "Buildings", "Business", "Music"));

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.viewPager.setAdapter(createAdapter());

        setupViews();

        new TabLayoutMediator(binding.tabs, binding.viewPager, (tab, position) -> tab.setText(TABS_TITLE_LIST.get(position))).attach();

    }

    private void setupViews() {
        getActivity().findViewById(R.id.profilePicture).setVisibility(View.VISIBLE);
        EditText search_bar = getActivity().findViewById(R.id.search_bar);
        getActivity().findViewById(R.id.headerLayout).setVisibility(View.VISIBLE);
        search_bar.clearFocus();
        search_bar.setText("");
        getActivity().findViewById(R.id.backBtn).setVisibility(View.GONE);
    }

    private TabAdapter createAdapter() {
        return new TabAdapter(getActivity());
    }

    private class TabAdapter extends FragmentStateAdapter {

        public TabAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return ImageListingFragment.newInstance(TABS_TITLE_LIST.get(position).toLowerCase());
        }

        @Override
        public int getItemCount() {
            return TABS_TITLE_LIST.size();
        }

    }
}
