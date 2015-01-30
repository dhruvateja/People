package com.people;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.internal.in;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.people.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import adapters.PlacesAdapter;
import beans.Places;
import util.LocationUtils;
import util.Utilities;

public class Location extends Activity implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener{
    // Google Map
    private GoogleMap googleMap;
    private LocationClient mLocationClient;
    private LinearLayout current_loc_layout;
    private LinearLayout close_layout;
    private ListView loc_listView;
    private String clientId;
    private String secretId;
    private ProgressBar pb;
    private RelativeLayout mapLayout;
    private LinearLayout mapFullScreenLayout;
    private boolean isFullMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        mLocationClient = new LocationClient(this, this, this);
        current_loc_layout = (LinearLayout)findViewById(R.id.send_my_loc);
        close_layout       = (LinearLayout)findViewById(R.id.close_layout);
        loc_listView       = (ListView)findViewById(R.id.loc_list);
        pb                 = (ProgressBar)findViewById(R.id.progress_bar);
        mapLayout          = (RelativeLayout)findViewById(R.id.map_area);
        mapFullScreenLayout = (LinearLayout)findViewById(R.id.map_full_screen);
        clientId = getResources().getString(R.string.fourthsqaure_id);
        secretId = getResources().getString(R.string.fourthsquare_secert_id);
        initializeMap();
        mapFullScreenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFullMode){
                    isFullMode = true;
                    RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,
                            RelativeLayout.LayoutParams.MATCH_PARENT );

                    mapLayout.setLayoutParams(layout_description);
                }else {
                    isFullMode = false;
                    int Height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, getResources().getDisplayMetrics());

                    RelativeLayout.LayoutParams layout_description = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT ,
                            Height );

                    mapLayout.setLayoutParams(layout_description);
                }
            }
        });
        current_loc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mLocationClient.isConnected()){
                    android.location.Location location = mLocationClient.getLastLocation();
                        if(location!= null){
                              double lat = location.getLatitude();
                              double lang = location.getLongitude();
                        }
                }
            }
        });
        close_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        loc_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Places data = (Places)adapterView.getItemAtPosition(i);
                if(data!=null){
                    double lat = data.getLatitude();
                    double lang = data.getLongitude();
                    String name = data.getName();
                    String add  = data.getAddress();
                    System.out.println("phani add = "+add);
                    System.out.println("phani name "+name+ "lang = "+lang +"lat = "+lat);
                }
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                   MarkerOptions options = new MarkerOptions();
                options.position(new LatLng(latLng.latitude,latLng.longitude));
                options.title("hi");
                googleMap.addMarker(options);

            }
        });
    }

    /**
     * function to load map. If map is not created it will create it for you
     * */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap.getUiSettings().setRotateGesturesEnabled(true);
            googleMap.getUiSettings().setZoomGesturesEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(false);

            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(getApplicationContext(),
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(servicesConnected()){
              if(Utilities.getInstance().isLocationEnabled(this)){
                  mLocationClient.connect();
              }else {
                  loc_listView.setVisibility(View.GONE);
                  current_loc_layout.setVisibility(View.GONE);
                  Utilities.getInstance().showLocationServiceDisabledAlertToUser(this);
              }

        }else {
            loc_listView.setVisibility(View.GONE);
            current_loc_layout.setVisibility(View.GONE);
            int resultCode =  GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
            if(ConnectionResult.SERVICE_MISSING == resultCode){
                Utilities.getInstance().showGooglePlayServiceDisabledAlertToUser(this, getResources().getString(R.string.locations_service_missing_txt));

            }else if(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED == resultCode){
                Utilities.getInstance().showGooglePlayServiceDisabledAlertToUser(this,getResources().getString(R.string.common_google_play_services_notification_needs_update_title));


            }
        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        if(loc_listView.getVisibility() == View.GONE){
            showDetails();
        }

    }

    @Override
    public void onDisconnected() {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mLocationClient.isConnected()){
            mLocationClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode =  GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            return false;
        }
    }

    public void showDetails(){
       // loc_listView.setVisibility(View.GONE);
        current_loc_layout.setVisibility(View.VISIBLE);
        if(googleMap!=null){
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        if(mLocationClient.isConnected()){
            android.location.Location location = mLocationClient.getLastLocation();
            if(location!=null){
                System.out.println("phani lat = "+location.getLatitude() +"longi = "+location.getLongitude());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(
                        new LatLng(location.getLatitude(), location.getLongitude())).zoom(18).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                String latLng = String.valueOf(location.getLatitude())+"," +String.valueOf(location.getLongitude());
                String url = getResources().getString(R.string.nearbyplaces_api)+"client_id="+clientId+"&client_secret="+secretId+"&v="+"20150129"+"&ll="+latLng;
               System.out.println("phani url "+url);
               new RetrieveNearByPlaces().execute(url);
            }
        }
    }
    public class RetrieveNearByPlaces extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
           String data = Utilities.getInstance().loadDataFromUrl(strings[0]);
           if(data!=null){
               return data;
           }else {
               return null;
           }

        }

        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if(data!= null && data.length()>0){
                if(data.startsWith("Error")){
                    System.out.println("phani Error ");
                }else if(data.equals("No Data Found")){
                    System.out.println("phani No Data Found");
                }else {
                   //    System.out.println("phani data "+data);
                       parsePlacesJson(data);
                }

            }
        }
    }

    public void parsePlacesJson(String response){
        try {
            JSONObject rootObj = new JSONObject(response);
            JSONObject metaObj = rootObj.getJSONObject("meta");
            JSONObject responseObj = rootObj.getJSONObject("response");
            if(metaObj.has("code")){
                int code = metaObj.getInt("code");

            }
            ArrayList<Places> placesArrayList = new ArrayList<Places>();
            if(responseObj.optJSONArray("venues")!=null){
                JSONArray venuesArray = responseObj.getJSONArray("venues");

                if(venuesArray!=null && venuesArray.length()>0){
                    for(int i =0;i< venuesArray.length();i++){
                        Places places = new Places();
                        JSONObject venueObj = venuesArray.getJSONObject(i);
                        if(venueObj.has("name")){
                            places.setName(venueObj.getString("name"));
                        }
                        JSONObject locObj = venueObj.getJSONObject("location");
                        if(locObj!=null && locObj.length() >0){
                            if(locObj.has("address")){
                                places.setAddress(locObj.getString("address"));
                            }else {
                                places.setAddress("");
                            }
                            if(locObj.has("lat")){
                                places.setLatitude(locObj.getDouble("lat"));
                            }
                            if(locObj.has("lng")){
                                places.setLongitude(locObj.getDouble("lng"));
                            }
                            if(locObj.has("distance")){
                                places.setDistance(locObj.getInt("distance"));
                            }
                        }

                         placesArrayList.add(places);
                    }
                }else{ // No results found

                }
                System.out.println("phani size "+placesArrayList.size());
                    if(placesArrayList!=null && placesArrayList.size()>0){
                        loc_listView.setVisibility(View.VISIBLE);
                        PlacesAdapter adapter = new PlacesAdapter(this,placesArrayList);
                        loc_listView.setAdapter(adapter);
                        pb.setVisibility(View.GONE);
                        addMarkersOnMap(placesArrayList);
                    }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public  void addMarkersOnMap(ArrayList<Places> placesArrayList){
        for(int i =0 ;i<placesArrayList.size() ;i++){
            // create marker
            MarkerOptions marker = new MarkerOptions();

            if(placesArrayList.get(i).getLatitude()!=0 && placesArrayList.get(i).getLongitude()!=0 ){
                marker.position(new LatLng(placesArrayList.get(i).getLatitude(), placesArrayList.get(i).getLongitude()));
            }
              if(placesArrayList.get(i).getName()!=null && placesArrayList.get(i).getName().length()>0){
                  marker.title(placesArrayList.get(i).getName());
              }

            // adding marker
            googleMap.addMarker(marker);
        }

    }

}
