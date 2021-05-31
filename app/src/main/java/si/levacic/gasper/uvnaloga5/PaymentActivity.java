package si.levacic.gasper.uvnaloga5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PaymentActivity extends AppCompatActivity {
    public static String EXTRA_PRICE = "si.levacic.gasper.uvnaloga5.addPassenger.price";
    public static String EXTRA_CARD = "si.levacic.gasper.uvnaloga5.addPassenger.card";
    private boolean CCVInput = false;
    private boolean cardInput = false;
    private double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // izpiši ceno
        Intent intent = getIntent();
        TextView cenaTV = (TextView) findViewById(R.id.cenaTV);
        price = intent.getDoubleExtra(MainActivity.EXTRA, 0);
        cenaTV.setText(String.format("Cena: %.2f€", price));

        // dodaj listenerje
        EditText cardET = (EditText) findViewById(R.id.cardETN);
        EditText ccvET = (EditText) findViewById(R.id.ccvETN);

        cardET.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((s.length() == 4 || s.length() == 9 || s.length() == 14)) {
                    if (before == 0) {
                        cardET.setText(cardET.getText() + "-");
                        cardET.setSelection(s.length() + 1);
                } else if (count == 0) {
                        cardET.setText(s.toString().substring(0, start - 1));
                        cardET.setSelection(start - 1);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 19) {
                    cardInput = true;
                } else {
                    cardInput = false;
                }

                checkInput();
            }
        });

        ccvET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 3) {
                    CCVInput = true;
                } else {
                    CCVInput = false;
                }

                checkInput();
            }
        });
    }

    private void checkInput() {
        Button button = (Button) findViewById(R.id.paymentBttn);
        button.setEnabled(this.cardInput && this.CCVInput);
    }

    public void cancel(View view) {
        setResult(RESULT_CANCELED);
        finish();
    }

    public void pay(View view) {
        Intent res = new Intent();
        res.putExtra(EXTRA_PRICE, price);
        EditText cardET = (EditText) findViewById(R.id.cardETN);
        String cardNumber = cardET.getText().toString();
        res.putExtra(EXTRA_CARD, cardNumber.substring(cardNumber.length()-4));
        setResult(RESULT_OK, res);
        finish();
    }
}