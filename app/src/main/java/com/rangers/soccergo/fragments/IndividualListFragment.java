package com.rangers.soccergo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rangers.soccergo.R;

/**
 * IndividualListFragment
 * Desc: 个人赛列表Fragment
 * Team: Rangers
 * Date: 2015/4/14
 * Time: 14:30
 * Created by: Wooxxx
 */
public class IndividualListFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_individual_list, container, false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initViews() {
        super.initViews();
    }

    @Override
    public void setListeners() {
        super.setListeners();
    }
}
