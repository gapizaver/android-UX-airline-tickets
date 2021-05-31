package si.levacic.gasper.uvnaloga5;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.Date;

public class PassengerActivity extends AppCompatActivity {
    public static String EXTRA_CP_SEX = "si.levacic.gasper.uvnaloga5.addPassenger.sex";
    public static String EXTRA_CP_CATEGORY = "si.levacic.gasper.uvnaloga5.addPassenger.category";
    private String name = "";
    private String surname = "";
    private String sex = "n";
    // kategorija:
    //      1: do 2 leti
    //      2: do 12 let
    //      3: odrasli
    private int category = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger);



        // add listeners
        EditText nameET = (EditText) findViewById(R.id.name);
        EditText surnameET = (EditText) findViewById(R.id.surname);
        RadioGroup sexRG = (RadioGroup) findViewById(R.id.sexRG);

        sexRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.male) {
                    sex = "m";
                } else if (checkedId == R.id.female) {
                    sex = "f";
                } else {
                    sex = "n";
                }

                checkInput();
            }
        });

        nameET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                name = s.toString();
                checkInput();
            }
        });

        surnameET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                surname = s.toString();
                checkInput();
            }
        });
    }

    private void checkInput(){
        // če vpisan ves input, omogoči gumb "Dodaj potnika"
        Button button = findViewById(R.id.addPassengerBttn);
        button.setEnabled(name.length() > 0 & surname.length() > 0 & category > 0 & !sex.equals("n"));
    }

    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

        newFragment.setOnDateClickListener(new onDateClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                LocalDate birth = LocalDate.of(datePicker.getYear(), datePicker.getMonth()+1, datePicker.getDayOfMonth());
                int age = LocalDate.now().getYear() - birth.getYear() -1;
                // preveri če je letos osebek že imel rojstni dan
                if (!birth.withYear(LocalDate.now().getYear()).isAfter(LocalDate.now())) {
                    age += 1;
                }

                if (age >= 0) {
                    Button button = (Button) findViewById(R.id.datumRojstvaBttn);
                    button.setText(datePicker.getDayOfMonth() +"."+ (datePicker.getMonth()+1) +"."+ datePicker.getYear());

                    if (age < 2) category = 1;
                    else if (age <= 12) category = 2;
                    else category = 3;

                    checkInput();
                } else {
                    showToast("NAPAKA: Izberite pretekli datum.");
                }
            }
        });
    }

    public void showToast(String s) {
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                // Code here will run in UI thread
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void addPassenger(View view) {
        //Passenger newPassenger = new Passenger(name, surname, sex, category);
        Intent res = new Intent();
        //res.putExtra(EXTRA_CP, newPassenger);
        res.putExtra(EXTRA_CP_CATEGORY, category);
        res.putExtra(EXTRA_CP_SEX, sex);
        setResult(RESULT_OK, res);
        finish();
    }
}