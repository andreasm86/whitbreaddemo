package demo.whitbread.andreas.whitbreaddemo.Helpers;

/**
 * Created by WestPlay on 21/6/2016.
 */
public class UnitConversionHelper {

    public static int convertMetersToKilometersIfOverLimit(int meters, int limit){
        return meters < limit ? meters : meters / 1000;
    }

}
