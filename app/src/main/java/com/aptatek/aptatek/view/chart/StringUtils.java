package com.aptatek.aptatek.view.chart;

import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;

import static android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE;

public class StringUtils {


    public static CharSequence highlightWord(final String highlightedText, final String text) {
        final SpannableString span1 = new SpannableString(highlightedText);
        span1.setSpan(new RelativeSizeSpan(1.5f), 0, highlightedText.length(), SPAN_INCLUSIVE_INCLUSIVE);

        final SpannableString span2 = new SpannableString(text);
        span2.setSpan(new RelativeSizeSpan(0.5f), 0, text.length(), SPAN_INCLUSIVE_INCLUSIVE);
        return TextUtils.concat(span1, "\n", span2);
    }
}
