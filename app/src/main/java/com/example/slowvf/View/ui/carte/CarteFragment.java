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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
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
import java.util.Set;

//
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class CarteFragment extends Fragment {
    private MapView mapView;
    private Button downloadMap;
    private static final int SELECT_MAP_FILE = 0;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final String TAG = "PERMISSION_TAG";

    private FragmentCarteBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CarteViewModel carteViewModel =
                new ViewModelProvider(this).get(CarteViewModel.class);

        View rootView = inflater.inflate(R.layout.fragment_carte, container, false);

        mapView = rootView.findViewById(R.id.map);
        downloadMap = rootView.findViewById(R.id.downloadMap);

        // Get the authority from the manifest file provider
        String authority = "com.example.slowvf.fileprovider";

        String fileUrl = "https://drive.google.com/uc?export=download&id=1aGbGcC1zRa5_Pg44tnFs3CgiTFwyW53v&confirm=t&uuid=cf2223cc-b057-49ac-ba9c-b6da9298d3d7&at=AKKF8vxUq4y5eOzMy0EJ6V_BUw8r:1687169137822";
        String fileName = "nord-pas-de-calais-rebuilt.map"; // Replace with the actual file name
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir, fileName);

        if (checkPermission()) {
            Log.d(TAG, "onClick: Permission alrady Granted");

        } else {
            Log.d(TAG, "onClick: Permission not granted");

            requestPermission();
        }

         if(file.exists()) {
            downloadMap.setVisibility(View.GONE);

            // Create the content URI using FileProvider
            Uri fileUri = FileProvider.getUriForFile(requireContext(), authority, file);

            AndroidGraphicFactory.createInstance(getContext());

            openMap(fileUri);
         }

        downloadMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startDownloading(fileUrl, fileName);
            }
        });

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
            mapView.setCenter(new LatLong(50.433456, 2.819352));
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
        //mapView.destroyAll();
        //AndroidGraphicFactory.clearResourceMemoryCache();
        super.onDestroy();
    }



    private void startDownloading(String url, String fileName) {
        // Create download request
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

        // Alow type of network to download files
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setTitle("Téléchargement de la carte");
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        // Get download service and enqueue file
        DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);

    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Log.d(TAG, "reqestPermission: try");

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                intent.setData(uri);

                storageActivityResultLauncher.launch(intent);
            } catch (Exception e) {
                Log.d(TAG, "RequestPermission: catch", e);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        } else {
            // Android is below R
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requireActivity().requestPermissions(
                        new String []{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_CODE
                );
            }
        }
    }

    private final ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");

                    // Handle the result of our intent
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                       if (Environment.isExternalStorageManager()) {
                           Log.d(TAG, "onActivityResult: Manage External Storage Permission is granted");
                           // Launch download
                       } else {
                           Log.d(TAG, "onActivityResult: Manager External Storage Permission is denied");
                           Toast.makeText(requireActivity(), "Permission refusé", Toast.LENGTH_SHORT).show();
                       }
                    } else {

                    }

                }
            }
    );

    public boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        }
    }

   // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted from popup, launch download
                Log.i("I", "Launch Download");
            } else {
                // Permission denied from popup, show error message
                Toast.makeText(requireContext(), "Permission refusée", Toast.LENGTH_SHORT).show();
            }
        }
    }
}