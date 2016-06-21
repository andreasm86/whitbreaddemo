package demo.whitbread.andreas.whitbreaddemo;

import org.junit.Test;

import demo.whitbread.andreas.whitbreaddemo.Adapters.VenuesRecyclerAdapter;
import demo.whitbread.andreas.whitbreaddemo.Helpers.UnitConversionHelper;

import static org.junit.Assert.assertEquals;

/**
 * Created by WestPlay on 21/6/2016.
 */
public class MetersToKilometersConversionTest {
    @Test
    public void kilometerConversionIsCorrect() throws Exception {
        //Test 1
        int testMetersInput1 = 100;
        int result1 = UnitConversionHelper.convertMetersToKilometersIfOverLimit(testMetersInput1, VenuesRecyclerAdapter.METER_LIMIT_BEFORE_KILOMETERS);
        assertEquals("Meter conversion test 1 failed", testMetersInput1, result1);
        //Test 2
        int testMetersInput2 = 5684;
        int result2 = UnitConversionHelper.convertMetersToKilometersIfOverLimit(testMetersInput2, VenuesRecyclerAdapter.METER_LIMIT_BEFORE_KILOMETERS);
        assertEquals("Meter conversion test 2 failed", 5, result2);
    }
}
