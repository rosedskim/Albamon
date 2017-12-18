package javaproject;
import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.GeocoderCallback;
import com.teamdev.jxmaps.GeocoderRequest;
import com.teamdev.jxmaps.GeocoderResult;
import com.teamdev.jxmaps.GeocoderStatus;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.Map;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapReadyHandler;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.swing.MapView;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicTextFieldUI;
import java.awt.*;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This example demonstrates how to geocode coordinates by an address and vice versa.
 *
 * @author Vitaly Eremenko
 * @author Sergei Piletsky
 */
    
public class GeocoderExample extends MapView {

    private static final String INITIAL_LOCATION = "영통동";


    public GeocoderExample() {
        // Setting of a ready handler to MapView object. onMapReady will be called when map initialization is done and     
        // the map object is ready to use. Current implementation of onMapReady customizes the map object.
        setOnMapReadyHandler(new MapReadyHandler() {
            @Override
            public void onMapReady(MapStatus status) {
                // Getting the associated map object
                final Map map = getMap();
                // Setting initial zoom value
                map.setZoom(12.0);
                // Creating a map options object
                MapOptions options = new MapOptions(map);
                // Creating a map type control options object
                MapTypeControlOptions controlOptions = new MapTypeControlOptions(map);
                // Changing position of the map type control
                controlOptions.setPosition(ControlPosition.TOP_RIGHT);
                // Setting map type control options
                options.setMapTypeControlOptions(controlOptions);
                // Setting map options
                map.setOptions(options);

                LatLng a = new LatLng(37.248325, 127.077537);
                map.setCenter(a);
            }
        });
    }

    public void performGeocode(String text, Job job) {
        final Map map = getMap();
        GeocoderRequest request = new GeocoderRequest(map);
        // Setting address to the geocode request
        request.setAddress(text);

        // Geocoding position by the entered address
        getServices().getGeocoder().geocode(request, new GeocoderCallback(map) {
            @Override
            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
                // Checking operation status
                if ((status == GeocoderStatus.OK) && (results.length > 0)) {
                    // Getting the first result
                    GeocoderResult result = results[0];
                    // Getting a location of the result
                    LatLng location = result.getGeometry().getLocation();
                    // Setting the map center to result location
                    map.setCenter(location);
                    // Creating a marker object
                    Marker marker = new Marker(map);
                    // Setting position of the marker to the result location
                    marker.setPosition(location);
                    // Creating an information window
                    InfoWindow infoWindow = new InfoWindow(map);
                    // Putting the address and location to the content of the information window
                    infoWindow.setContent("<b>" + job.getTitle() + "</b><br>" + job.getSalary());
                    // Moving the information window to the result location
                    infoWindow.setPosition(location);
                    // Showing of the information window
                    infoWindow.open(map, marker);
                }
            }
        });
    }
}
//    `
//    public void addMarker(String text,Job job)
//    {
//        final Map map = getMap();
//        GeocoderRequest request = new GeocoderRequest(map);
//        request.setAddress(text);
//        getServices().getGeocoder().geocode(request, new GeocoderCallback(map) {
//            @Override
//            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
//                // Checking operation status
//                if ((status == GeocoderStatus.OK) && (results.length > 0)) {
//                    // Getting the first result
//                    GeocoderResult result = results[0];
//                    // Getting a location of the result
//                    LatLng location = result.getGeometry().getLocation();
//                    // Creating a marker object
//                    Marker marker = new Marker(map);
//                    // Setting position of the marker to the result location
//                    marker.setPosition(location);
//                    // Creating an information window
//                    InfoWindow infoWindow = new InfoWindow(map);
//                    // Putting the address and location to the content of the information window
//                    infoWindow.setContent("<b>" + result.getFormattedAddress() + "</b><br>" + location.toString());
//                    // Moving the information window to the result location
//                    infoWindow.setPosition(location);
//                    // Showing of the information window
//                    infoWindow.open(map, marker);
//                }
//            }
//        });
//    }
//    public LatLng getLatLng(String text)
//    {
//        final Map map = getMap();
//        GeocoderRequest request = new GeocoderRequest(map);
//        request.setAddress(text);
//        getServices().getGeocoder().geocode(request, new GeocoderCallback(map) {
//            @Override
//            public void onComplete(GeocoderResult[] results, GeocoderStatus status) {
//                // Checking operation status
//                if ((status == GeocoderStatus.OK) && (results.length > 0)) {
//                    // Getting the first result
//                    GeocoderResult result = results[0];
//                    // Getting a location of the result
//                    LatLng location = result.getGeometry().getLocation();
//                    place = location;
//                }
//            }
//        });
//        return place;
//    }
//    public void moveCam(LatLng position)
//    {
//        final Map map = getMap();
//        map.setCenter(position);
//    }
