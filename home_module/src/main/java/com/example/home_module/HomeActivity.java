package com.example.home_module;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.annotation.JRouterAnno;

import java.util.Set;

@JRouterAnno(path = "/home_module/HomeActivity")
public class HomeActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            textView=findViewById(R.id.paramInfo);
            Set<String> strings = bundle.keySet();
            StringBuilder sb=new StringBuilder();
            for (String key:
                 strings) {
                sb.append("{");
                sb.append(key);
                sb.append(",");
                sb.append(bundle.get(key));
                sb.append("}\n");
            }
            textView.setText(sb.toString());
        }
    }
}
