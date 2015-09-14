package fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.orhanobut.logger.Logger;
import com.sejukstudio.qittoalert.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import helper.API;
import helper.VolleyHelper;
import model.Data;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link QittoMap.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link QittoMap# newInstance} factory method to
 * create an instance of this fragment.
 */
public class QittoMap extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;

    public QittoMap() {
        // Required empty public constructor
    }

    private Context ctx;

    private GoogleMap gMap;
    private Marker marker;
    //GPS
    private LocationManager locationManager;
    private String provider;
    private Location location;
    //HeatMap
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    //Others
    private FloatingActionButton fab;
    private Location myLocation;
    private String myAdress;
    private FragmentManager fragmentManager;
    private ProgressDialog pDialog;

    private VolleyHelper volleyHelper;
    private List<Data> listData;
    private List<LatLng> listLatLng;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        myAdress = "";
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Processing...");
        listLatLng = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ctx = getActivity();

        //getData();

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initializeElements(view);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocationAddress();
                requestDialog(view);
            }
        });

        return view;
    }


    private void initializeElements(View view) {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void getMyLocationAddress() {

        Geocoder geocoder = new Geocoder(ctx, Locale.ENGLISH);

        try {
            //Place your latitude and longitude
            List<Address> addresses = geocoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1);

            if (addresses != null) {

                Address fetchedAddress = addresses.get(0);
                StringBuilder strAddress = new StringBuilder();
                Logger.d(fetchedAddress.toString());

                for (int i = 0; i < fetchedAddress.getMaxAddressLineIndex(); i++) {
                    if (i == fetchedAddress.getMaxAddressLineIndex() - 1) {
                        strAddress.append(fetchedAddress.getAddressLine(i));
                    } else {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append(",").append("\n");
                    }
                    Logger.d(strAddress.toString());
                }
                myAdress = strAddress.toString();
                Logger.d(strAddress.toString());
            } else
                Logger.d("No location found.");


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(ctx, "Could not get address..!", Toast.LENGTH_LONG).show();
        }
    }

    private void addHeatMap() {
        //List<LatLng> list = new ArrayList<>();

        listLatLng.add(new LatLng(2.929996, 101.658730));
        listLatLng.add(new LatLng(2.930210, 101.655726));
        listLatLng.add(new LatLng(2.927896, 101.657357));
        listLatLng.add(new LatLng(2.930232, 101.660747));
        listLatLng.add(new LatLng(2.926525, 101.657035));
        listLatLng.add(new LatLng(2.921053, 101.657464));
        listLatLng.add(new LatLng(2.929396, 101.657743));
        listLatLng.add(new LatLng(2.930703, 101.658838));

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        HeatmapTileProvider mProvider = new HeatmapTileProvider.Builder()
                .data(listLatLng)
                .build();

        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = gMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("gmap", "map ready");
        gMap = googleMap;
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setCompassEnabled(false);
        getCurrentLocation();
        getData();

    }

    private void animateCamera(LatLng latLng) {
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15).build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);

        LocationListener listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation = location;

                Log.d("gmap", "location changed!");

                String lat = Double.toString(location.getLatitude());
                String lng = Double.toString(location.getLongitude());

                Log.d("gmap", "current_location:" + lat + " " + lng);
                Logger.d(myLocation.toString());

                drawMaker(new LatLng(location.getLatitude(), location.getLongitude()));

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (location != null) {
            listener.onLocationChanged(location);
        } else {
            locationManager.requestLocationUpdates(provider, 0, 0, listener);
        }

    }

    private void drawMaker(LatLng latLng) {

        //gMap.clear();
        if (marker != null)
            marker.remove();
        marker = gMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_main)));
        animateCamera(latLng);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public void requestDialog(final View view) {


        View dialog = getActivity().getLayoutInflater().inflate(R.layout.dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Request for fogging?");
        alertDialogBuilder.setMessage("Your location:\n" + myAdress);
        alertDialogBuilder.setView(dialog);

        final EditText editText = (EditText)dialog.findViewById(R.id.etPhone);


        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //submit to API.
                String phone = editText.getText().toString();
                if(phone.equals("")){
                    editText.setError("Please fill this form.");
                    submitRequest("");
                }else {
                    submitRequest(phone);
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //do nothing
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void submitRequest(String phone) {
        String ACTION = "request_fogging";

        String ADDRESS = myAdress.replace('\n', '+').replace(' ', '+');
        String LAT = Double.toString(myLocation.getLatitude());
        String LNG = Double.toString(myLocation.getLongitude());

        pDialog.show();
        String requestUrl = API.URL + ACTION + "&address=" + ADDRESS + "&lat=" + LAT + "&lng=" + LNG + "&phone=" + phone;
        Logger.d(requestUrl);
        JsonObjectRequest req = new JsonObjectRequest(requestUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String result;
                        pDialog.dismiss();
                        Logger.d(response.toString());

                        Log.d("reponse", response.toString());

                        try {
                            result = response.getString("result");
                            if (result.equals("ok")) {
                                Snackbar.make(getView(), "Your request has been sent!", Snackbar.LENGTH_LONG).show();
                            } else {
                                Snackbar.make(getView(), "Request failed!", Snackbar.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

        volleyHelper = new VolleyHelper(ctx);
        volleyHelper.addToRequestQueue(req);

    }

    public void getData() {

        final List<LatLng> list = new ArrayList<>();

        pDialog.show();
        String requestUrl = API.URL_STATISTICS;
        Logger.d(requestUrl);

        JsonArrayRequest req = new JsonArrayRequest(requestUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        pDialog.dismiss();
                        Logger.d(response.toString());
//                        Gson gson = new Gson();
//
//                        //GSON
//
//                        //method 1
//                        Data[] dataArray = gson.fromJson(response.toString(), Data[].class);
//                        listData = Arrays.asList(dataArray);
//
//                        //method 2
//                        Type listType = new TypeToken<ArrayList<Data>>() {
//                        }.getType();
//                        //listData = new Gson().fromJson(response.toString(), listType);
//
//                        Logger.d(listData.toString());
//
//                        for (Data data : listData) {
//                            Log.d("data", data.getLatitude() + data.getLongitude());
//                            double lat = Double.parseDouble(data.getLatitude());
//                            double lng = Double.parseDouble(data.getLongitude());
//                            listLatLng.add(new LatLng(lat, lng));
//                        }


                        //JSON

                        for(int i = 0 ; i < response.length();i++){

                            try {
                                JSONObject jsonObj = response.getJSONObject(i);
                                double lat = Double.parseDouble(jsonObj.getString("latitude"));
                                double lng = Double.parseDouble(jsonObj.getString("longitude"));
                                listLatLng.add(new LatLng(lat,lng));

                                //Logger.d(response.toString());
                                Log.d("data", "lat - "+ lat + "lng - "+ lng);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        addHeatMap();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Logger.e(error.getMessage());
                pDialog.dismiss();
            }
        });

        new VolleyHelper(ctx).addToRequestQueue(req);
        //volleyHelper.addToRequestQueue(req);

    }

}

