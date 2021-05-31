package si.levacic.gasper.uvnaloga5;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA = "si.levacic.gasper.uvnaloga5.main";
    private final int REQUEST = 1;
    private final int REQUEST_PAYMENT = 2;
    private double price = 0;
    private int[] passengers = new int[3];
    private boolean dateSet = false;
    private double flightClass = -1.0;
    private boolean returnFlight = false;
    private boolean returnDateSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < passengers.length; i++){
            passengers[i] = 0;
        }

        // dodaj listenerje
        RadioGroup classRG = (RadioGroup) findViewById(R.id.classRG);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        classRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                if (checkedId == R.id.businessRB) {
                    flightClass = 2.0;
                } else if (checkedId == R.id.firstRB) {
                    flightClass = 1.5;
                } else if (checkedId == R.id.ecoRB) {
                    flightClass = 1.0;
                } else {
                    flightClass = -1;
                }
                checkInput();
                calculatePrice();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                calculatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void showDatePickerDialog1(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

        newFragment.setOnDateClickListener(new onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Button button = (Button) findViewById(R.id.datumOdhodaBttn);
                button.setText(datePicker.getDayOfMonth() +"."+ datePicker.getMonth() +"."+ datePicker.getYear());
                dateSet = true;
                checkInput();
            }
        });
    }

    public void showDatePickerDialog2(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

        newFragment.setOnDateClickListener(new onDateClickListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                Button button = (Button) findViewById(R.id.datumPrihodaBttn);
                button.setText(datePicker.getDayOfMonth() +"."+ datePicker.getMonth() +"."+ datePicker.getYear());
                returnDateSet = true;
                checkInput();
                calculatePrice();
            }
        });
    }

    public void setReturnTicket(View v) {
        CheckBox cb = (CheckBox) findViewById(R.id.returnTicketCB);
        Button button = (Button) findViewById(R.id.datumPrihodaBttn);
        this.returnFlight = cb.isChecked();
        button.setEnabled(cb.isChecked());
        calculatePrice();
        checkInput();
    }

    public void addPassenger(View v) {
        Intent intent = new Intent(this, PassengerActivity.class);
        startActivityForResult(intent, REQUEST);
    }

    @Override
    protected void onActivityResult(int reqC, int resC, Intent data){
        super.onActivityResult(reqC, resC, data);
        if (resC == RESULT_OK){
            if (reqC == REQUEST) {
                int category = data.getIntExtra(PassengerActivity.EXTRA_CP_CATEGORY, 3);
                String sex = data.getStringExtra(PassengerActivity.EXTRA_CP_SEX);
                addPassenger(category, sex);
            } else if (reqC == REQUEST_PAYMENT) {
                Double price = data.getDoubleExtra(PaymentActivity.EXTRA_PRICE, 0);
                String cardNum = data.getStringExtra(PaymentActivity.EXTRA_CARD);
                String msg = String.format("Plačan znesek %.2f€ z kreditno kartico xxxx-xxxx-xxxx-%s", price, cardNum);
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addPassenger(int category, String sex) {
        this.passengers[category-1] += 1;
        TextView tv = (TextView) findViewById(R.id.passengersTV);
        String passengers = tv.getText().toString().substring(9);

        /*int numOfPassengers = 0;

        for (int i = 0; i < passengers.length; i++) {
            numOfPassengers += passengers[i];
        }*/
        if (category == 1){
            passengers = "\uD83D\uDC76" + passengers;
        } else if (category == 2) {
            if (sex.equals("f")) {
                passengers = "\uD83D\uDC67" + passengers;
            } else {
                passengers = "\uD83D\uDC66" + passengers;
            }
        } else {
            if (sex.equals("f")) {
                passengers = "\uD83D\uDC69" + passengers;
            } else {
                passengers = "\uD83D\uDC68" + passengers;
            }
        }

        tv.setText("Potniki: "+ passengers);
        calculatePrice();
        checkInput();
    }

    private void calculatePrice(){
        if (flightClass < 0) return;

        int[] prices = {500, 200, 480, 300, 400, 350, 450, 390};
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        CheckBox cb = (CheckBox) findViewById(R.id.returnTicketCB);
        TextView tv = (TextView) findViewById(R.id.priceTV);

        int destinationPos = spinner.getSelectedItemPosition();
        this.price = (prices[destinationPos]*passengers[2] + prices[destinationPos]*passengers[1]/2.0)*this.flightClass;
        if (this.returnFlight) this.price *= 2;
        tv.setText(String.format("Cena:    %.2f€", this.price));
    }

    public void requestPayment(View view) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(EXTRA, this.price);
        startActivityForResult(intent, REQUEST_PAYMENT);
    }

    private void checkInput() {
        // preveri če vsaj en odrasel potnik - omogoči plačilo
        Button button = (Button) findViewById(R.id.ontoPaymentBttn);
        button.setEnabled(this.flightClass > 0 && this.dateSet && this.passengers[2] > 0 &&
                (!this.returnFlight || this.returnDateSet));
    }

    public void clearInput(View view) {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Potrditev")
                .setMessage("Vsi vnešeni podatki bodo izbrisani. Ali želite nadaljevati?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id) {
                        // počisti gradnike
                        Spinner spinner = findViewById(R.id.spinner);
                        spinner.setSelection(0);
                        Button deparuteBttn = (Button) findViewById(R.id.datumOdhodaBttn);
                        Button returnBttn = (Button) findViewById(R.id.datumPrihodaBttn);
                        deparuteBttn.setText("Izberi datum");
                        returnBttn.setText("Izberi datum");
                        CheckBox cb = (CheckBox) findViewById(R.id.returnTicketCB);
                        cb.setChecked(false);
                        RadioGroup rg = (RadioGroup) findViewById(R.id.classRG);
                        rg.clearCheck();
                        TextView tv = (TextView) findViewById(R.id.passengersTV);
                        tv.setText("Potniki: ");
                        TextView priceTv = (TextView) findViewById(R.id.priceTV);
                        priceTv.setText("Cena:    0.00€");

                        // počisti spremenljivke
                        for (int i = 0; i < passengers.length; i++) {
                            passengers[i] = 0;
                        }
                        dateSet = false;
                        returnFlight = false;
                        returnDateSet = false;
                        flightClass = -1.0;
                    }
                })
                .setNegativeButton("Prekliči", null).show();
    }
}