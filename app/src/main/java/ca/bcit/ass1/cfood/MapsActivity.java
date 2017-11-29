package ca.bcit.ass1.cfood;
import android.content.Intent;
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
    String[] zoneDesc;
    String [] coordsLat;
    String [] coordsLong;
    LatLng[] latLngs;
    float centerLat = 0.0f;
    float centerLong = 0.0f;

    String [] shopsNames;
    String [] shopsX;
    String [] shopsY;
    ArrayList<Marker> shopsMarkers = new ArrayList<Marker>();
    ArrayList<Marker> shopsMarkersNB = new ArrayList<Marker>();

    String [] parksLat;
    String [] parksLong;
    String [] parksNames;
    ArrayList<Marker> parksMarkers = new ArrayList<Marker>();
    ArrayList<Marker> parksMarkersNB = new ArrayList<Marker>();

    String [] busStopX;
    String [] busStopY;
    String [] busStopNames;
    ArrayList<Marker> busStopMarkers = new ArrayList<Marker>();
    ArrayList<Marker> busStopMarkersNB = new ArrayList<Marker>();

    String [] recX;
    String [] recY;
    String [] recNames;
    ArrayList<Marker> recMarkers = new ArrayList<Marker>();
    ArrayList<Marker> recMarkersNB = new ArrayList<Marker>();

    String [] schoolsX;
    String [] schoolsY;
    String [] schoolsNames;
    ArrayList<Marker> schoolsMarkers = new ArrayList<Marker>();
    ArrayList<Marker> schoolsMarkersNB = new ArrayList<Marker>();

    //Contains all the places
     LatLng[] latLngsShops = null;
     LatLng[] latLngsParks = null;
     LatLng[] latLngsBusStops = null;
     LatLng[] latLngsRecreation = null;
     LatLng[] latLngsSchools = null;

    //Contains only those places/landmarks that are within
    //the specified neighbourhood (polygon)
     ArrayList<LatLng> latLngsShopsInPolygon = new ArrayList<LatLng>();
     ArrayList<LatLng> latLngsParksInPolygon = new ArrayList<LatLng>();
     ArrayList<LatLng> latLngsBusStopsInPolygon = new ArrayList<LatLng>();
     ArrayList<LatLng> latLngsRecreationInPolygon = new ArrayList<LatLng>();
     ArrayList<LatLng> latLngsSchoolsInPolygon = new ArrayList<LatLng>();

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
        centerLong = getArguments().getFloat("centerLong");
        centerLat = getArguments().getFloat("centerLat");
       // zoneDesc = getArguments().getStringArray("zoneDesc");

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

        mMapView.getMapAsync(new OnMapReadyCallback()  {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                //Add the polygon's LatLngs
                latLngs = new LatLng[coordsLat.length];
                for(int i = 0; i < coordsLat.length; i++) {
                    latLngs[i] = new LatLng(Float.parseFloat(coordsLong[i]), Float.parseFloat(coordsLat[i]));
                }

                LatLng centering = new LatLng(centerLat, centerLong);

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centering, 13.0f));
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
                        latLngsShopsInPolygon
                        .add(new LatLng(Float.parseFloat(shopsX[j]),
                                Float.parseFloat(shopsY[j])));
                    }
                }

                for(int j = 0; j < parksLat.length; j++) {
                    latLngsParks[j] = new LatLng(Float.parseFloat(parksLat[j]), Float.parseFloat(parksLong[j]));
                    if(PolyUtil.containsLocation(
                            Float.parseFloat(parksLat[j]),
                            Float.parseFloat(parksLong[j]), polygon.getPoints(), false)) {
                        latLngsParksInPolygon
                                .add(new LatLng(Float.parseFloat(parksLat[j]),
                                        Float.parseFloat(parksLong[j])));
                    }
                }

                for(int j = 0; j < busStopX.length; j++) {
                    latLngsBusStops[j] = new LatLng(Float.parseFloat(busStopX[j]), Float.parseFloat(busStopY[j]));

                    if(PolyUtil.containsLocation(
                            Float.parseFloat(busStopX[j]),
                            Float.parseFloat(busStopY[j]), polygon.getPoints(), false)) {
                        latLngsBusStopsInPolygon
                        .add(new LatLng(Float.parseFloat(busStopX[j]),
                                Float.parseFloat(busStopY[j])));
                    }
                }

                for(int j = 0; j < recX.length; j++) {
                    latLngsRecreation[j] = new LatLng(Float.parseFloat(recX[j]), Float.parseFloat(recY[j]));

                    if(PolyUtil.containsLocation(
                            Float.parseFloat(recX[j]),
                            Float.parseFloat(recY[j]), polygon.getPoints(), false)) {
                        latLngsRecreationInPolygon
                        .add(new LatLng(Float.parseFloat(recX[j]),
                                Float.parseFloat(recY[j])));
                    }
                }

                for(int j = 0; j < schoolsX.length; j++) {
                    latLngsSchools[j] = new LatLng(Float.parseFloat(schoolsX[j]), Float.parseFloat(schoolsY[j]));

                    if(PolyUtil.containsLocation(
                            Float.parseFloat(schoolsX[j]),
                            Float.parseFloat(schoolsY[j]), polygon.getPoints(), false)) {
                        latLngsSchoolsInPolygon
                        .add(new LatLng(Float.parseFloat(schoolsX[j]),
                                Float.parseFloat(schoolsY[j])));
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
                    .title("School: \n" + schoolsNames[j])
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
                    .title("Park: \n" + parksNames[j])
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
                                .title("Bus Stop: \n" + busStopNames[j])
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
                    .title("Recreation Center: \n" + recNames[j])
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

    public void showShopsNB() {
        for(int j = 0; j < latLngsShopsInPolygon.size(); j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsShopsInPolygon.get(j))
                    .title("Shop: \n" + shopsNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            );
            shopsMarkersNB.add(marker);
        }
    }

    public void hideShopsNB() {
        for(int j = 0; j < shopsMarkersNB.size(); j++) {
            shopsMarkersNB.get(j).remove();
        }
    }

    public void showSchoolsNB() {
        for(int j = 0; j < latLngsSchoolsInPolygon.size(); j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsSchoolsInPolygon.get(j))
                    .title("School: \n" + schoolsNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            );
            schoolsMarkersNB.add(marker);
        }
    }

    public void hideSchoolsNB() {
        for(int j = 0; j < schoolsMarkersNB.size(); j++) {
            schoolsMarkersNB.get(j).remove();
        }
    }

    public void showParksNB() {
        for(int j = 0; j < latLngsParksInPolygon.size(); j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsParksInPolygon.get(j))
                    .title(parksNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
            );
            parksMarkersNB.add(marker);
        }
    }

    public void hideParksNB() {
        for(int j = 0; j < parksMarkersNB.size(); j++) {
            parksMarkersNB.get(j).remove();
        }
    }

    public void showBusStopsNB() {
        for(int j = 0; j < latLngsBusStopsInPolygon.size(); j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsBusStopsInPolygon.get(j))
                    .title(busStopNames[j])
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.reticle)));
            busStopMarkersNB.add(marker);
        }
    }

    public void hideBusStopsNB() {
        for(int j = 0; j < busStopMarkersNB.size(); j++) {
            busStopMarkersNB.get(j).remove();
        }
    }

    public void showRecNB() {
        for(int j = 0; j < latLngsRecreationInPolygon.size(); j++) {
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position((LatLng)latLngsRecreationInPolygon.get(j))
                    .title(recNames[j])
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
            recMarkersNB.add(marker);
        }
    }

    public void hideRecNB() {
        for(int j = 0; j < recMarkersNB.size(); j++) {
            recMarkersNB.get(j).remove();
        }
    }
}