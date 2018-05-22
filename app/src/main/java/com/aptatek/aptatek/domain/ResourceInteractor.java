package com.aptatek.aptatek.domain;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.aptatek.aptatek.injection.qualifier.ApplicationContext;

import java.lang.reflect.Field;

import javax.inject.Inject;


/**
 * Interactor for resources
 */

@SuppressWarnings("unused")
public class ResourceInteractor {
    private Context context;
    private Resources resources;

    @Inject
    public ResourceInteractor(@ApplicationContext Context context) {
        this.context = context;
        this.resources = context.getResources();
    }

    /**
     * Convert dp to pixels according to the system display metrics
     */
    public float convertDpToPixel(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    /**
     * Provide the app accessible resources
     */
    public Resources getResources() {
        return resources;
    }

    /**
     * Return the string associated with the given resource id
     */
    public String getStringResource(int resourceId) {
        return getResources().getString(resourceId);
    }

    /**
     * Return the string associated with the given resource id
     */
    public String getStringResource(int resourceId, Object... formatArgs) {
        return getResources().getString(resourceId, formatArgs);
    }

    /**
     * Return a formatted string associated with the given resource id, filled with the given parameters
     *
     * @param resourceId The id of the resource
     * @param items      The fillable items
     */
    public String getFormattedString(int resourceId, Object... items) {
        return String.format(getStringResource(resourceId), items);
    }

    /**
     * Return the string associated with the given resource id by matching the correct plural form
     */
    public String getQuantityString(int quantity, int resourceId) {
        return getResources().getQuantityString(resourceId, quantity);
    }

    /**
     * Return a string associated it's name
     *
     * @return The string if the value is presented, null otherwise
     */
    public String getStringResource(String resourceName) {
        int id = getResources().getIdentifier(
                resourceName,
                "string", context.getPackageName());
        if (id == 0) {
            return null;
        } else {
            return getStringResource(id);
        }
    }

    /**
     * Return an id for the given resource array name
     *
     * @return The valid id if the resource has found, 0 otherwise
     */
    public int getArrayResourceIdentifier(String resourceName) {
        return getResources().getIdentifier(resourceName, "array", context.getPackageName());
    }

    /**
     * Return an id for the given resource string name
     *
     * @return The valid id if the resource has found, 0 otherwise
     */
    public int getStringResourceIdentifier(String resourceName) {
        return getResources().getIdentifier(resourceName, "string", context.getPackageName());
    }

    /**
     * Return an id for the given resource drawable name
     *
     * @return The valid id if the resource has found, 0 otherwise
     */
    public int getDrawableResourceIdentifier(String resourceName) {
        return getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }

    /**
     * Return an id for the given resource layout name
     *
     * @return The valid id if the resource has found, 0 otherwise
     */
    public int getLayoutResourceIdentifier(String resourceName) {
        return getResources().getIdentifier(resourceName, "layout", context.getPackageName());
    }

    /**
     * Return an id for the given resource view name
     *
     * @return The valid id if the resource has found, 0 otherwise
     */
    public int getViewIdResourceIdentifier(String resourceName) {
        return getResources().getIdentifier(resourceName, "id", context.getPackageName());
    }

    /**
     * Return an id for the given resource name (using reflection)
     *
     * @return The valid id if the resource has found, 0 otherwise
     */
    public int getResId(String resourceName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(resourceName);
            return idField.getInt(idField);
        } catch (Exception e) {
            return 0; // No resource found for given name
        }
    }

    /**
     * Return a string array with the data of the given array id
     */
    public String[] getStringArray(int arrayResourceId) {
        return getResources().getStringArray(arrayResourceId);
    }

    /**
     * Return an integer array with the data of the given array id
     */
    public int[] getIntArray(int arrayResourceId) {
        return getResources().getIntArray(arrayResourceId);
    }

    /**
     * Return a raw typed array for the given resource
     */
    public TypedArray getTypedArray(int arrayResourceId) {
        return getResources().obtainTypedArray(arrayResourceId);
    }

    /**
     * Return a drawable associated with the given resource id
     */
    public Drawable getDrawableResource(int resourceId) {
        return getResources().getDrawable(resourceId);
    }

    /**
     * Return a color associated with the given resource id
     */
    public int getColorResource(int resourceId) {
        return getResources().getColor(resourceId);
    }

    /**
     * Return a dimension associated with the given resource id
     */
    public float getDimension(int resourceId) {
        return getResources().getDimension(resourceId);
    }

    /**
     * Return an integer associated with the given resource id
     */
    public int getInteger(int resourceId) {
        return getResources().getInteger(resourceId);
    }
}
