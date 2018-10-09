package com.aptatek.pkulab.util;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

public class StringUtils {

    private static final float SCALE_BIG = 1.9f;
    private static final float SCALE_SMALL = 0.9f;

    public static CharSequence highlightWord(final String highlightedText, final String text) {
        final SpannableString span1 = new SpannableString(highlightedText);
        span1.setSpan(new RelativeSizeSpan(SCALE_BIG), 0, highlightedText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        final SpannableString span2 = new SpannableString(text);
        span2.setSpan(new RelativeSizeSpan(SCALE_SMALL), 0, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return TextUtils.concat(span1, "\n", span2);
    }
}
