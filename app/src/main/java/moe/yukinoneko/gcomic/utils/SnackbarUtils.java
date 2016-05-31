/*
 * Copyright (C) 2016  SamuelGjk <samuel.alva@outlook.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package moe.yukinoneko.gcomic.utils;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by SamuelGjk on 2016/4/7.
 */
public class SnackbarUtils {
    /**
     * 短时间显示Snackbar
     *
     * @param view
     * @param message
     */
    public static void showShort(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 短时间显示Snackbar
     *
     * @param view
     * @param resId
     */
    public static void showShort(View view, @StringRes int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 长时间显示Snackbar
     *
     * @param view
     * @param message
     */
    public static void showLong(View view, CharSequence message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Snackbar
     *
     * @param view
     * @param resId
     */
    public static void showLong(View view, @StringRes int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 长时间显示带Action的Snackbar
     *
     * @param view
     * @param message
     */
    public static void showLongWithAction(View view, CharSequence message, CharSequence actionMessage, View.OnClickListener action) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(actionMessage, action).show();
    }

    /**
     * 长时间显示带Action的Snackbar
     *
     * @param view
     * @param resId
     */
    public static void showLongWithAction(View view, @StringRes int resId, @StringRes int actionResId, View.OnClickListener action) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).setAction(actionResId, action).show();
    }

    /**
     * 自定义显示Snackbar时间
     *
     * @param view
     * @param message
     * @param duration
     */
    public static void show(View view, CharSequence message, int duration) {
        Snackbar.make(view, message, duration).show();
    }

    /**
     * 自定义显示Snackbar时间
     *
     * @param view
     * @param resId
     * @param duration
     */
    public static void show(View view, @StringRes int resId, int duration) {
        Snackbar.make(view, resId, duration).show();
    }
}
