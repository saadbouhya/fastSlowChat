package com.example.slowvf.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.slowvf.Controller.ContactController;
import com.example.slowvf.Dao.Impl.ReceivedSentEchangelDaoImpl;
import com.example.slowvf.Dao.Impl.ReceivedSentLocalDaoImpl;
import com.example.slowvf.Model.Contact;
import com.example.slowvf.R;
import com.example.slowvf.View.Contact.ContactDetails;
import com.example.slowvf.databinding.ActivityMainNavigationBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivityNavigation extends AppCompatActivity {

    private ActivityMainNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainNavigationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_chat, R.id.navigation_echange, R.id.navigation_carte, R.id.navigation_contacts, R.id.navigation_reglages)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        try {
            ReceivedSentLocalDaoImpl.createFileOnInternalStorage(getApplicationContext());
            ReceivedSentLocalDaoImpl.readInternalFile(getApplicationContext(),"Local.json");
            ReceivedSentEchangelDaoImpl.createFileOnInternalStorage(getApplicationContext());
            ReceivedSentEchangelDaoImpl.readInternalFile(getApplicationContext(),"Echange.json");
           ContactController contactController = new ContactController(getApplicationContext());
            Contact contact = new Contact("5E:FF:56:A2:AF:15Mouad16-04-2023", "D. Monkey", "Mouad");
            contactController.create(contact,getApplicationContext());

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}