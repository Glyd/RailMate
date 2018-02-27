package assignment.lewisd97.railmate.src.Helpers;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by lewisd97 on 08/02/2018.
 * Thanks to https://stackoverflow.com/questions/2711858/is-it-possible-to-set-a-custom-font-for-entire-of-application/16883281#16883281
 */

public class FontsOverride {

    public static void setDefaultFont(Context context, String staticTypefaceFieldName, String fontAssetName) {
        Typeface regular = Typeface.createFromAsset(context.getAssets(), fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName, final Typeface newTypeface) {
        try {
            final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
            staticField.setAccessible(true);
            staticField.set(null, newTypeface);
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFont(Activity callingActivity) {
        FontsOverride.setDefaultFont(callingActivity, "MONOSPACE", "Fonts/DidactGothic-Regular.ttf");
    }
}