package ca.bcit.ass1.cfood;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

public class MapsActivity extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    String [] coordsLat;
    String [] coordsLong;
    LatLng[] latLngs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        if (getArguments() == null) {
            ZoneDetailsActivity mDashboardActivity = (ZoneDetailsActivity) getActivity();
            if(mDashboardActivity!=null){
                mDashboardActivity.refreshMyData();
            }
        }
        coordsLat = getArguments().getStringArray("coordsLat");
        coordsLong = getArguments().getStringArray("coordsLong");
        latLngs = new LatLng[coordsLat.length];

        for(int i = 0; i < coordsLat.length; i++) {
            latLngs[i] = new LatLng(Float.parseFloat(coordsLong[i]), Float.parseFloat(coordsLat[i]));
        }

        mMapView = (MapView) rootView.findViewById(R.id.mapView2);
        if (mMapView == null) {
            ZoneDetailsActivity mDashboardActivity = (ZoneDetailsActivity) getActivity();
            if(mDashboardActivity!=null){
                mDashboardActivity.refreshMyData();
            }
        }
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                //googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map
               // googleMap.addMarker(new MarkerOptions().position(latLngs[0]).title("test").snippet("Marker Description"));


                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[4], 14.0f));
                PolygonOptions rectOptions = new PolygonOptions()
                        .add(latLngs)
                        .fillColor(Color.argb(50, 50, 0, 255))
                        .strokeWidth(2.0f);
                Polygon polygon = mMap.addPolygon(rectOptions);

                // For zooming automatically to the location of the marker
                //CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                //googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}