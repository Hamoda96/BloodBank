package com.hamoda.bloodbank.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hamoda.bloodbank.R;
import com.hamoda.bloodbank.data.model.generalResponse.GeneralResponseData;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    public List<GeneralResponseData> generalResponseDataList = new ArrayList<>();
    private LayoutInflater inflter;
    public int selectedId = 0;

    public SpinnerAdapter(Context applicationContext) {
        inflter = (LayoutInflater.from(applicationContext));
    }

    public void setData(List<GeneralResponseData> generalResponseDataList, String hint) {
        this.generalResponseDataList = new ArrayList<>();
        this.generalResponseDataList.add(new GeneralResponseData(0, hint));
        this.generalResponseDataList.addAll(generalResponseDataList);
    }

    @Override
    public int getCount() {
        return generalResponseDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner_item, null);

        TextView names =  view.findViewById(R.id.item_spinner_tv_text);

        names.setText(generalResponseDataList.get(i).getName());
        selectedId = generalResponseDataList.get(i).getId();

        return view;
    }
}
