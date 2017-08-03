package com.greatcall.activitytransition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.greatcall.activitytransition.options.AdapterOptions;
import com.greatcall.activitytransition.options.OptionModel;

public class SharedElementActivity extends AppCompatActivity {

    TextView header;
    TextView typeAndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_element);
        this.header = (TextView) findViewById(R.id.text_from_item);
        this.typeAndDate = (TextView) findViewById(R.id.dateAndTypeOfPhone);
        CallViewModel model = (CallViewModel)getIntent().getSerializableExtra("call");
        this.typeAndDate.setText(model.getPhoneNumber());
        this.header.setText(model.getName());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.optionRecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OptionModel modeloption = new OptionModel();
        AdapterOptions adapterOptions = new AdapterOptions(this);
        recyclerView.setAdapter(adapterOptions);
        adapterOptions.setDataSet(modeloption.getOptions(this));
    }
}
