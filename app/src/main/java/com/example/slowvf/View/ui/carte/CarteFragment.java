package com.example.slowvf.View.ui.carte;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.slowvf.R;
import com.example.slowvf.databinding.FragmentCarteBinding;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.android.util.AndroidUtil;
import org.mapsforge.map.android.view.MapView;
import org.mapsforge.map.datastore.MapDataStore;
import org.mapsforge.map.layer.cache.TileCache;
import org.mapsforge.map.layer.renderer.TileRendererLayer;
import org.mapsforge.map.reader.MapFile;
import org.mapsforge.map.rendertheme.InternalRenderTheme;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class CarteFragment extends Fragment {
    private MapView mapView;
    private Button downloadMap;
    private static final int SELECT_MAP_FILE = 0;
    private int STORAGE_PERMISSION_CODE = 1000;

    private FragmentCarteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarteViewModel carteViewModel =
                new ViewModelProvider(this).get(CarteViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_carte, container, false);

        AndroidGraphicFactory.createInstance(getContext());

        mapView = rootView.findViewById(R.id.map);
        downloadMap = rootView.findViewById(R.id.downloadMap);

//        // Get the authority from the manifest file provider
//        String authority = "com.example.slowvf.fileprovider";
//
//        File file = new File(requireContext().getFilesDir(), "berlin.map");
//
//        // Create the content URI using FileProvider
//        Uri fileUri = FileProvider.getUriForFile(requireContext(), authority, file);
//
//        openMap(fileUri);

        downloadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If OS is Marshmallow or above, handle runtime permission sdk between 23 and 29
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                   if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                       // Permission denied, request it
                       Log.i("I", "Request Permission");

                       String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                       // Show pop for runtime permission
                       ActivityCompat.requestPermissions(requireActivity(), permissions, STORAGE_PERMISSION_CODE);

                   } else {
                       // Permission already granted, launch download
                       Log.i("I", "Permission granted so launch download!");
                   }
               } else {
                   // System OS is less then marshmallow, perform download
                   Log.i("I", "No need for permission so launch download!");

               }
            }
        });


//        File file = new File(requireContext().getFilesDir(), "ile-de-france.map");
//
//        if (file.exists()) {
//            Log.i("I", "File exists");
//
//            // Get the authority from the manifest file provider
//            String authority = "com.example.slowvf.fileprovider";
//
//            // Create the content URI using FileProvider
//            Uri fileUri = FileProvider.getUriForFile(requireContext(), authority, file);
//
//            openMap(fileUri);
//
//            String filePath = file.getAbsolutePath();
//        } else {
//            String fileUrl = "https://download.mapsforge.org/maps/v5/europe/france/ile-de-france.map";
//            String fileName = "ile-de-france.map";
//        }

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void openMap(Uri uri) {
        try {
            /*
             * We then make some simple adjustments, such as showing a scale bar and zoom controls.
             */
            mapView.getMapScaleBar().setVisible(true);
            mapView.setBuiltInZoomControls(true);

            /*
             * To avoid redrawing all the tiles all the time, we need to set up a tile cache with an
             * utility method.
             */
            TileCache tileCache = AndroidUtil.createTileCache(getContext(), "mapcache",
                    mapView.getModel().displayModel.getTileSize(), 1f,
                    mapView.getModel().frameBufferModel.getOverdrawFactor());

            /*
             * Now we need to set up the process of displaying a map. A map can have several layers,
             * stacked on top of each other. A layer can be a map or some visual elements, such as
             * markers. Here we only show a map based on a mapsforge map file. For this we need a
             * TileRendererLayer. A TileRendererLayer needs a TileCache to hold the generated map
             * tiles, a map file from which the tiles are generated and Rendertheme that defines the
             * appearance of the map.
             */
            FileInputStream fis = (FileInputStream) requireActivity().getContentResolver().openInputStream(uri);
            MapDataStore mapDataStore = new MapFile(fis);
            TileRendererLayer tileRendererLayer = new TileRendererLayer(tileCache, mapDataStore,
                    mapView.getModel().mapViewPosition, AndroidGraphicFactory.INSTANCE);
            tileRendererLayer.setXmlRenderTheme(InternalRenderTheme.DEFAULT);

            /*
             * On its own a tileRendererLayer does not know where to display the map, so we need to
             * associate it with our mapView.
             */
            mapView.getLayerManager().getLayers().add(tileRendererLayer);

            /*
             * The map also needs to know which area to display and at what zoom level.
             * Note: this map position is specific to Berlin area.
             */
            mapView.setCenter(new LatLong(52.517037, 13.38886));
            mapView.setZoomLevel((byte) 12);
        } catch (Exception e) {
            /*
             * In case of map file errors avoid crash, but developers should handle these cases!
             */
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        /*
         * Whenever your activity exits, some cleanup operations have to be performed lest your app
         * runs out of memory.
         */
        mapView.destroyAll();
        AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted from popup, launch download
                Log.i("I", "Launch Download");
            } else {
                // Permission denied from popup, show error message
                Toast.makeText(requireContext(), "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startDownloading(String url) {
        // Create download request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // Alow type of network to download files
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Download Map");
//        request.setDescription("Download Map");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "ileDeFrance.map");

        // Get download service and enque file
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }
}