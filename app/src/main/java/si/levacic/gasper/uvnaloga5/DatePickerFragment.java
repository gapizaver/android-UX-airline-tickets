package si.levacic.gasper.uvnaloga5;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
{
    private onDateClickListener onDateClickListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                onDateClickListener.onDateSet(datePicker,i, i1, i2);
            }
        }, year, month, day);
    }
    public void setOnDateClickListener(onDateClickListener onDateClickListener){
        if(onDateClickListener != null){
            this.onDateClickListener = onDateClickListener;
        }
    }

}

interface onDateClickListener {
    void onDateSet(DatePicker datePicker, int i, int i1, int i2);
}
