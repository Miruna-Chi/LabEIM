package ro.pub.cs.systems.eim.practicaltest01var07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest01var07.general.Constants;

public class PracticalTest01Var07SecondaryActivity extends AppCompatActivity {

    private TextView box1;
    private TextView box2;
    private TextView box3;
    private TextView box4;

    int no1, no2, no3, no4;

    private Button sum, product;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.sum:
                    setResult(no1 + no2 + no3 + no4, null);
                    break;
                case R.id.product:
                    setResult(no1 * no2 * no3 * no4, null);
                    break;
            }
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var07_secondary);


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getExtras().containsKey(Constants.Box1)) {
                no1 = intent.getIntExtra(Constants.Box1, -1);
                box1.setText(String.valueOf(no1));
            }

            if (intent.getExtras().containsKey(Constants.Box2)) {
                no2 = intent.getIntExtra(Constants.Box2, -1);
                box2.setText(String.valueOf(no2));
            }

            if (intent.getExtras().containsKey(Constants.Box3)) {
                no3 = intent.getIntExtra(Constants.Box3, -1);
                box3.setText(String.valueOf(no3));
            }

            if (intent.getExtras().containsKey(Constants.Box4)) {
                no4 = intent.getIntExtra(Constants.Box4, -1);
                box4.setText(String.valueOf(no4));
            }
        }
        sum = (Button) findViewById(R.id.sum);
        product = (Button) findViewById(R.id.product);

        sum.setOnClickListener(buttonClickListener);
        product.setOnClickListener(buttonClickListener);

    }
}