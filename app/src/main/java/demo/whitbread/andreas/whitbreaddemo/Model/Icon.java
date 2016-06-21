
package demo.whitbread.andreas.whitbreaddemo.Model;

import android.content.res.Resources;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import demo.whitbread.andreas.whitbreaddemo.R;

public class Icon implements Serializable{

    public static final String ICON_SIZE_88 = "88";

    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("suffix")
    @Expose
    private String suffix;

    /**
     * 
     * @return
     *     The prefix
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * 
     * @param prefix
     *     The prefix
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * 
     * @return
     *     The suffix
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * 
     * @param suffix
     *     The suffix
     */
    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getIconUrl(boolean isGreyBackground, Resources resources){
        return isGreyBackground ? String.format(resources.getString(R.string.foursquare_icon_grey_background_url), getPrefix(), ICON_SIZE_88, getSuffix())
                : String.format(resources.getString(R.string.foursquare_icon_url), getPrefix(), ICON_SIZE_88, getSuffix());
    }
}
