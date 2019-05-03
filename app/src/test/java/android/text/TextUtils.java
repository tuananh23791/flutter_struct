package android.text;

/**
 * Created by Hoa Nguyen on May 03 2019.
 */
public class TextUtils {
    public static boolean isEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }
}
