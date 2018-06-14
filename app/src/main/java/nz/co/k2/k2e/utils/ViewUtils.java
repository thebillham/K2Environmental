package nz.co.k2.k2e.utils;

import android.content.res.Resources;

public final class ViewUtils {

    private ViewUtils() {
        // This class is not publicly instantiable
    }

//    public static void changeIconDrawableToGray(Context context, Drawable drawable) {
//        if (drawable != null) {
//            drawable.mutate();
//            drawable.setColorFilter(ContextCompat.getColor(context, R.color.dark_gray), PorterDuff.Mode.SRC_ATOP);
//        }
//    }

    public static int dpToPx(float dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public static float pxToDp(float px) {
        float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
        return px / (densityDpi / 160f);
    }
}
