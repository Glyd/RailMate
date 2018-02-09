package assignment.lewisd97.railmate.src.Helpers;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import assignment.lewisd97.railmate.R;

/**
 * Created by lewisd97 on 09/02/2018.
 */

public class MyFocusChangeListener implements View.OnFocusChangeListener {
    Context context;

    public MyFocusChangeListener(Context context) {
        this.context = context;
    }

    public void onFocusChange(View v, boolean hasFocus){

        if(v.getId() == R.id.postcodeEntry && !hasFocus) {
            InputMethodManager imm =  (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

        }
    }
}
