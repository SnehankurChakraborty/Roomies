/*
 * Copyright 2016 Snehankur Chakraborty
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phaseii.rxm.roomies.ui.customviews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.utils.ColorTemplate;
import com.phaseii.rxm.roomies.R;
import com.phaseii.rxm.roomies.database.model.MiscExpense;

import java.util.List;

/**
 * Created by Snehankur on 4/16/2015.
 */
public class DetailMiscellaneousAdapter extends ArrayAdapter<MiscExpense> {

    Context mContext;
    String[] subcategoryTypes;
    private List<MiscExpense> miscExpenses;

    public DetailMiscellaneousAdapter(Context context, int resource,
            List<MiscExpense> miscExpenses) {
        super(context, resource);
        this.mContext = context;
        this.miscExpenses = miscExpenses;
        subcategoryTypes = mContext.getResources().getStringArray(R.array.subcategory);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context
                    .LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.show_expense_layout, null);
        }
        MiscExpense miscExpense = miscExpenses.get(position);
        if (miscExpense != null) {
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.expense_layout);
            TextView subCategory = (TextView) convertView.findViewById(R.id.subcategory);
            TextView description = (TextView) convertView.findViewById(R.id.description);
            TextView amount = (TextView) convertView.findViewById(R.id.amount);
            TextView quantity = (TextView) convertView.findViewById(R.id.quantity);
            if (subCategory != null) {
                subCategory.setText(miscExpense.getType());
                if (subcategoryTypes[0].equals(miscExpense.getType())) {
                    layout.setBackgroundColor(ColorTemplate.COLORFUL_COLORS[0]);
                }
                if (subcategoryTypes[1].equals(miscExpense.getType())) {
                    layout.setBackgroundColor(ColorTemplate.COLORFUL_COLORS[1]);
                }
                if (subcategoryTypes[2].equals(miscExpense.getType())) {
                    layout.setBackgroundColor(ColorTemplate.COLORFUL_COLORS[2]);
                }
                if (subcategoryTypes[3].equals(miscExpense.getType())) {
                    layout.setBackgroundColor(ColorTemplate.COLORFUL_COLORS[3]);
                }
            }
            if (description != null) {
                description.setText(miscExpense.getDescription());
            }
            if (amount != null) {
                amount.setText("Rs. " + String.valueOf(miscExpense.getAmount() + "/-"));
            }
            if (quantity != null) {
                quantity.setText(String.valueOf(miscExpense.getQuantity() + "kg/lr/pc"));
            }
        }
        return convertView;
    }
}
