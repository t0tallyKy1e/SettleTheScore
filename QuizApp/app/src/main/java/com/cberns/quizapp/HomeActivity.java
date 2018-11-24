package com.cberns.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private TextView mTextMessage;

    public void submitQuiz(View view){
        checkLikeMath();
        checkSimpleMult();
        checkhardMult();
        checkRoots();

    }

    private void checkLikeMath(){
        RadioGroup radioGrp = findViewById(R.id.like_math);
        if(radioGrp.getCheckedRadioButtonId() == R.id.yes_math){
            radioGrp.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

        } else {
            radioGrp.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

    }

    private void checkSimpleMult(){
        EditText ans = findViewById(R.id.ans_six_x_six);
        String nameEditable = ans.getText().toString();
        Integer val = new Integer(-1);
        if (!nameEditable.isEmpty()) {
            try{
                val = Integer.parseInt(nameEditable.toString());
            } catch (Error e){
                System.out.print("Empty");
            }
        }
        if(val == 36){
            ans.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            ans.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

    }

    private void checkhardMult(){
        EditText ans = findViewById(R.id.ans_thirteen_x_eleven);
        String nameEditable = ans.getText().toString();
        Integer val = new Integer(-1);
        if (!nameEditable.isEmpty()) {
            try{
                val = Integer.parseInt(nameEditable.toString());
            } catch (Error e){
                System.out.print("Empty");
            }
        }
        if(val == 13 * 11){
            ans.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            ans.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

    }

    private void checkRoots(){
        ArrayList<Integer> ids = new ArrayList<Integer>(Arrays.asList(
                     R.id.one, R.id.neg_one,
                     R.id.neg_two,
                     R.id.three, R.id.neg_three,
                     R.id.zero, R.id.not_applic));

        // Check for right answer
        CheckBox ckBox = findViewById(R.id.two);
        boolean firstCorrect = ckBox.isChecked();
        if(firstCorrect){
            ckBox.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            ckBox.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        }

        // Check the rest
        for (Integer id:ids) {
            ckBox = findViewById(id);
            if (ckBox.isChecked()) {
                ckBox.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else {
                ckBox.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
    }

}
