package com.app.deliver2me.helpers;

import com.google.android.gms.maps.model.LatLng;

public class PositionHelper {
    private static PositionHelper instance;
    private LatLng position;

    public static PositionHelper getInstance()
    {
        if(instance == null)
        {
            instance = new PositionHelper();
        }
        return instance;
    }

    public LatLng getPosition()
    {
        return position;
    }

    public void setPosition(LatLng position)
    {
        this.position = position;
    }
}
