package ca.bcit.ass1.cfood;
import android.graphics.Color;
import android.os.Parcelable;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;


public class MapsActivity extends Fragment {

    MapView mMapView;
    private GoogleMap googleMap;
    String [] coordsLat;
    String [] coordsLong;
    LatLng[] latLngs;

    String [] shopsNames;
    String [] shopsX;
    String [] shopsY;
    ArrayList<Marker> shopsMarkers = new ArrayList<Marker>();

    String [] parksLat;
    String [] parksLong;
    String [] parksNames;
    ArrayList<Marker> parksMarkers = new ArrayList<Marker>();

    String [] busStopX;
    String [] busStopY;
    String [] busStopNames;
    ArrayList<Marker> busStopMarkers = new ArrayList<Marker>();

    String [] recX;
    String [] recY;
    String [] recNames;
    ArrayList<Marker> recMarkers = new ArrayList<Marker>();

    String [] schoolsX;
    String [] schoolsY;
    String [] schoolsNames;
    ArrayList<Marker> schoolsMarkers = new ArrayList<Marker>();

    //Contains all the places
     LatLng[] latLngsShops = null;
     LatLng[] latLngsParks = null;
     LatLng[] latLngsBusStops = null;
     LatLng[] latLngsRecreation = null;
     LatLng[] latLngsSchools = null;

    //Contains only those places/landmarks that are within
    //the specified neighbourhood (polygon)
     LatLng[] latLngsShopsInPolygon = null;
     LatLng[] latLngsParksInPolygon = null;
     LatLng[] latLngsBusStopsInPolygon = null;
     LatLng[] latLngsRecreationInPolygon = null;
     LatLng[] latLngsSchoolsInPolygon = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        coordsLat = getArguments().getStringArray("coordsLat");
        coordsLong = getArguments().getStringArray("coordsLong");

        shopsNames = getArguments().getStringArray("shopsNames");
        shopsX = getArguments().getStringArray("shopsX");
        shopsY = getArguments().getStringArray("shopsY");

        parksNames = getArguments().getStringArray("parksNames");
        parksLat = getArguments().getStringArray("parksLat");
        parksLong = getArguments().getStringArray("parksLong");

        busStopNames = getArguments().getStringArray("busStopNames");
        busStopX = getArguments().getStringArray("busStopX");
        busStopY = getArguments().getStringArray("busStopY");

        recNames = getArguments().getStringArray("recNames");
        recX = getArguments().getStringArray("recX");
        recY = getArguments().getStringArray("recY");

        schoolsNames = getArguments().getStringArray("schoolsNames");
        schoolsX = getArguments().getStringArray("schoolsX");
        schoolsY = getArguments().getStringArray("schoolsY");

//        //Add the polygon's LatLngs
//        latLngs = new LatLng[coordsLat.length];
//        for(int i = 0; i < coordsLat.length; i++) {
//            latLngs[i] = new LatLng(Float.parseFloat(coordsLong[i]), Float.parseFloat(coordsLat[i]));
//        }

        latLngs = new LatLng[coordsLat.length];
        latLngsShops = new LatLng[shopsX.length];
        latLngsParks = new LatLng[parksLat.length];
        latLngsBusStops = new LatLng[busStopX.length];
        latLngsRecreation = new LatLng[recX.length];
        latLngsSchools = new LatLng[schoolsX.length];

        //Contains only those places/landmarks that are within
        //the specified neighbourhood (polygon)
        latLngsShopsInPolygon = new LatLng[shopsX.length];
        latLngsParksInPolygon = new LatLng[parksLat.length];
        latLngsBusStopsInPolygon = new LatLng[busStopX.length];
        latLngsRecreationInPolygon = new LatLng[recX.length];
        latLngsSchoolsInPolygon = new LatLng[schoolsX.length];

//        shopsMarkers = new ArrayList<Marker>();

        mMapView = (MapView) rootView.findViewById(R.id.mapView2);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback()  {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //Add the polygon's LatLngs
                latLngs = new LatLng[coordsLat.length];
                for(int i = 0; i < coordsLat.length; i++) {
                    latLngs[i] = new LatLng(Float.parseFloat(coordsLong[i]), Float.parseFloat(coordsLat[i]));
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngs[4], 14.0f));
                PolygonOptions rectOptions = new PolygonOptions()
                        .add(latLngs)
                        .fillColor(Color.argb(50, 50, 0, 255))
                        .strokeWidth(2.0f);
                Polygon polygon = googleMap.addPolygon(rectOptions);


                for(int j = 0; j < shopsX.length; j++) {
                    latLngsShops[j] = new LatLng(Float.parseFloat(shopsX[j]), Float.parseFloat(shopsY[j]));
                    if(PolyUtil.containsLocation(
                            Float.parseFloat(shopsX[j]),
                            Float.parseFloat(shopsY[j]), polygon.getPoints(), false)) {
                        latLngsShopsInPolygon[j] = new LatLng(Float.parseFloat(shopsX[j]), Float.parseFloat(shopsY[j]));
                    }
                }

                for(int j = 0; j < parksLat.length; j++) {
                    latLngsParks[j] = new LatLng(Float.parseFloat(parksLat[j]), Float.parseFloat(parksLong[j]));
                    if(PolyUtil.containsLocation(
                            Float.parseFloat(parksLat[j]),
                            Float.parseFloat(parksLong[j]), polygon.getPoints(), false)) {
                        latLngsParksInPolygon[j] = new LatLng(Float.parseFloat(parksLat[j]), Float.parseFloat(parksLong[j]));
                    }
                }

                for(int j = 0; j < busStopX.length; j++) {
                    latLngsBusStops[j] = new LatLng(Float.parseFloat(busStopX[j]), Float.parseFloat(busStopY[j]));

                    if(PolyUtil.containsLocation(
                            Float.parseFloat(busStopX[j]),
                            Float.parseFloat(busStopY[j]), polygon.getPoints(), false)) {
                        latLngsBusStopsInPolygon[j] = new LatLng(Float.parseFloat(busStopX[j]), Float.parseFloat(busStopY[j]));
                    }
                }

                for(int j = 0; j < recX.length; j++) {
                    latLngsRecreation[j] = new LatLng(Float.parseFloat(recX[j]), Float.parseFloat(recY[j]));

                    if(PolyUtil.containsLocation(
                            Float.parseFloat(recX[j]),
                            Float.parseFloat(recY[j]), polygon.getPoints(), false)) {
                        latLngsRecreationInPolygon[j] = new LatLng(Float.parseFloat(recX[j]), Float.parseFloat(recY[j]));
                    }
                }

                for(int j = 0; j < schoolsX.length; j++) {
                    latLngsSchools[j] = new LatLng(Float.parseFloat(schoolsX[j]), Float.parseFloat(schoolsY[j]));

                    if(PolyUtil.containsLocation(
                            Float.parseFloat(schoolsX[j]),
                            Float.parseFloat(schoolsY[j]), polygon.getPoints(), false)) {
                        latLngsSchoolsInPolygon[j] = new LatLng(Float.parseFloat(schoolsX[j]), Float.parseFloat(schoolsY[j]));
                    }
                }

                showShops();
                showBusStops();
                showParks();
                showRec();
                showSchools();

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

    public void showShops() {
        for(int j = 0; j < latLngsShops.length; j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position((LatLng)latLngsShops[j])
                                .title(shopsNames[j])
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                        );
            shopsMarkers.add(marker);
        }
    }

    public void hideShops() {
        for(int j = 0; j < shopsMarkers.size(); j++) {
            shopsMarkers.get(j).remove();
        }
    }

    public void showSchools() {
        for(int j = 0; j < latLngsSchools.length; j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsSchools[j])
                    .title(schoolsNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            );
            schoolsMarkers.add(marker);
        }
    }

    public void hideSchools() {
        for(int j = 0; j < schoolsMarkers.size(); j++) {
            schoolsMarkers.get(j).remove();
        }
    }

    public void showParks() {
        for(int j = 0; j < latLngsParks.length; j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsParks[j])
                    .title(parksNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
            parksMarkers.add(marker);
        }
    }

    public void hideParks() {
        for(int j = 0; j < parksMarkers.size(); j++) {
            parksMarkers.get(j).remove();
        }
    }

    public void showBusStops() {
        for(int j = 0; j < latLngsBusStops.length; j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                                .position((LatLng)latLngsBusStops[j])
                                .title(busStopNames[j])
                                .icon(BitmapDescriptorFactory
                                        .fromResource(R.drawable.reticle)));
            busStopMarkers.add(marker);
        }
    }

    public void hideBusStops() {
        for(int j = 0; j < busStopMarkers.size(); j++) {
            busStopMarkers.get(j).remove();
        }
    }

    public void showRec() {
        for(int j = 0; j < latLngsRecreation.length; j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsRecreation[j])
                    .title(recNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
            recMarkers.add(marker);
        }
    }

    public void hideRec() {
        for(int j = 0; j < recMarkers.size(); j++) {
            recMarkers.get(j).remove();
        }
    }
}