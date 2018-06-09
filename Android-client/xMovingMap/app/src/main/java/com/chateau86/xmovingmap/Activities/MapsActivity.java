package com.chateau86.xmovingmap.Activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Log;

import com.chateau86.xmovingmap.R;
import com.chateau86.xmovingmap.model.Navaid;
import com.chateau86.xmovingmap.model.NavaidDatabaseHolder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<Navaid> navaid_list;

    private HashMap<String, Marker> visibleMarkers;

    private HashMap<String, BitmapDescriptor> symbols;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle b = getIntent().getExtras();
        //navaid_list = (List<Navaid>) b.getSerializable("list");

        visibleMarkers = new HashMap<>();
        symbols = new HashMap<>();
        symbols.put("NDB", getBitmapFromVector(getApplicationContext(), R.drawable.ic_ndb));
        symbols.put("VOR", getBitmapFromVector(getApplicationContext(), R.drawable.ic_vor));
        symbols.put("VORDME", getBitmapFromVector(getApplicationContext(), R.drawable.ic_vordme));
        symbols.put("VORTAC", getBitmapFromVector(getApplicationContext(), R.drawable.ic_vortac));
        symbols.put("TACAN", getBitmapFromVector(getApplicationContext(), R.drawable.ic_tacan));

        Log.e("MapsActivity","Init ok");

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                double ZOOM_MIN = 5.0;
                int MAX_COUNT = 500;
                if (mMap.getCameraPosition().zoom < ZOOM_MIN){
                    return;
                }
                LatLngBounds bound = mMap.getProjection().getVisibleRegion().latLngBounds;
                navaid_list = NavaidDatabaseHolder.getInstance().navaidDAO()
                                .getWindowNearby(
                                        bound.southwest.latitude,
                                        bound.northeast.latitude,
                                        bound.southwest.longitude,
                                        bound.northeast.longitude);
                if(navaid_list.size() > MAX_COUNT){
                    return;
                }
                HashSet<String> newMarkerNames = new HashSet<>();
                for(Navaid n: navaid_list){
                    newMarkerNames.add(n.ident);
                }
                //Now do pruning of lost navaids
                Iterator itr = visibleMarkers.entrySet().iterator();
                while(itr.hasNext()){
                    Map.Entry item = (Map.Entry) itr.next();
                    if(!newMarkerNames.contains((String)item.getKey())){
                        Marker m = (Marker) item.getValue();
                        m.remove();
                        itr.remove();
                    }
                }
                //Now add new markers
                for(Navaid n: navaid_list){
                    if(!visibleMarkers.containsKey(n.ident)){
                        //add n
                        LatLng loc = new LatLng(n.lat, n.lon);
                        BitmapDescriptor icon = null;
                        if(n.name.contains("VOR/DME")) {
                            icon = symbols.get("VORDME");
                        } else if(n.name.contains("VORTAC")){
                            icon = symbols.get("VORTAC");
                        } else if (n.name.contains("VOR")) {
                            icon = symbols.get("VOR");
                        } else if (n.name.contains("TACAN")) {
                            icon = symbols.get("TACAN");
                        } else if (n.name.contains("NDB")) {
                            icon = symbols.get("NDB");
                        }
                        Marker m;
                        if(icon == null) {
                            m = mMap.addMarker(new MarkerOptions()
                                    .position(loc)
                                    .title(n.ident)
                                    .snippet(n.toString())
                                    .zIndex(-1.0f)
                            );
                        } else {
                            m = mMap.addMarker(new MarkerOptions()
                                    .position(loc)
                                    .title(n.ident)
                                    .snippet(n.toString())
                                    .icon(icon)
                            );
                        }
                        visibleMarkers.put(n.ident, m);
                    }
                }

            }
        });
    }

    private static BitmapDescriptor getBitmapFromVector(@NonNull Context context,
                                                        @DrawableRes int vectorResourceId) {

        Drawable vectorDrawable = ResourcesCompat.getDrawable(
                context.getResources(), vectorResourceId, null);
        if (vectorDrawable == null) {
            Log.e("svg2bmp", "Requested vector resource was not found");
            return BitmapDescriptorFactory.defaultMarker();
        }
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
