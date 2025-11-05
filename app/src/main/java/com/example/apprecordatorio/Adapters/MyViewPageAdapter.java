package com.example.apprecordatorio.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.apprecordatorio.fragments.AlarmasFragment;
import com.example.apprecordatorio.fragments.GeneralesFragment;
import com.example.apprecordatorio.fragments.TutorFragment;

public class MyViewPageAdapter extends FragmentStateAdapter {
    public MyViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AlarmasFragment();
            case 1:
                return new GeneralesFragment();
            case 2:
                return new TutorFragment();
            default:
                return new AlarmasFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
