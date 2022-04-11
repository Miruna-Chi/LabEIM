package ro.pub.cs.systems.eim.practicaltest01var07;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ro.pub.cs.systems.eim.practicaltest01var07.general.Constants;

public class PracticalTest01Var07MainActivity extends AppCompatActivity {

    private EditText box1, box2, box3, box4;
    private Button set;

//    private int serviceStatus = Constants.SERVICE_STOPPED;

    private IntentFilter intentFilter = new IntentFilter();

    private PracticalTest01Var07MainActivity.ButtonClickListener buttonClickListener = new PracticalTest01Var07MainActivity.ButtonClickListener();
    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {

            int no1 = 0, no2 = 0, no3 = 0, no4 = 0;

            if (view.getId() == R.id.Set) {

                int counter = 0;

                if (!box1.getText().toString().equals("")) {
                    no1 = Integer.parseInt(box1.getText().toString());
                    counter++;
                }

                if (!box2.getText().toString().equals("")) {
                    no1 = Integer.parseInt(box2.getText().toString());
                    counter++;
                }

                if (!box3.getText().toString().equals("")) {
                    no1 = Integer.parseInt(box3.getText().toString());
                    counter++;
                }

                if (!box4.getText().toString().equals("")) {
                    no1 = Integer.parseInt(box4.getText().toString());
                    counter++;
                }

                if (counter == 4) {
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01Var07SecondaryActivity.class);

                    intent.putExtra(Constants.Box1, no1);
                    intent.putExtra(Constants.Box2, no2);
                    intent.putExtra(Constants.Box3, no3);
                    intent.putExtra(Constants.Box3, no3);

                    startActivityForResult(intent, Constants.SECONDARY_ACTIVITY_REQUEST_CODE);

                }


            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_var07_secondary);

        box1 = (EditText)findViewById(R.id.text1);
        box2 = (EditText)findViewById(R.id.text2);
        box3 = (EditText)findViewById(R.id.text3);
        box4 = (EditText)findViewById(R.id.text4);


        set = (Button)findViewById(R.id.Set);
        set.setOnClickListener(buttonClickListener);


    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver(messageBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
//        unregisterReceiver(messageBroadcastReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        Intent intent = new Intent(this, PracticalTest01Service.class);
//        stopService(intent);
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey(Constants.Box1))
            savedInstanceState.putString(Constants.Box1, box1.getText().toString());
        if (savedInstanceState.containsKey(Constants.Box2))
            savedInstanceState.putString(Constants.Box2, box2.getText().toString());
        if (savedInstanceState.containsKey(Constants.Box3))
            savedInstanceState.putString(Constants.Box3, box3.getText().toString());
        if (savedInstanceState.containsKey(Constants.Box4))
            savedInstanceState.putString(Constants.Box4, box4.getText().toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(Constants.Box1))
            box1.setText(savedInstanceState.getString(Constants.Box1));
        if (savedInstanceState.containsKey(Constants.Box2))
            box2.setText(savedInstanceState.getString(Constants.Box2));
        if (savedInstanceState.containsKey(Constants.Box3))
            box3.setText(savedInstanceState.getString(Constants.Box3));
        if (savedInstanceState.containsKey(Constants.Box4))
            box4.setText(savedInstanceState.getString(Constants.Box4));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == Constants.SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
}