import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import assignment.lewisd97.railmate.BuildConfig;
import assignment.lewisd97.railmate.R;
import assignment.lewisd97.railmate.src.Activities.SearchActivity;

import static junit.framework.Assert.assertEquals;

/**
 * Created by lewisd97 on 09/02/2018.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 23)
public class SearchActivitySpec {

    SearchActivity searchActivity;
    View view;

    @Before
    public void before() throws Exception {
        searchActivity = Robolectric.buildActivity(SearchActivity.class).create().get();
        view = LayoutInflater.from(searchActivity).inflate(R.layout.activity_search, new LinearLayout(searchActivity), false);


        searchActivity = new SearchActivity();
    }

    @Test
    public void testTest() throws Exception {
        assertEquals(1, 1);
    }
}
