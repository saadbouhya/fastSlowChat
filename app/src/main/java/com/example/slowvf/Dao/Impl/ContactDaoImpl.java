package com.example.slowvf.Dao.Impl;

import android.content.Context;

import com.example.slowvf.Dao.ContactDao;
import com.example.slowvf.Model.Contact;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class ContactDaoImpl implements ContactDao {
    Gson gson;
    public ContactDaoImpl(Context context) {
        gson = new Gson();
        try {
            File file = new File(context.getFilesDir(), "Contacts.json");
            if (!file.exists()) {
                file.createNewFile();

                JSONArray jsonArray = new JSONArray();

                // Encapsulate the JSONArray in a JSONObject with a name
                JSONObject rootJsonObject = new JSONObject();
                rootJsonObject.put("Contacts", jsonArray);


                // Write the JSONObject to the file
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                outputStreamWriter.write(rootJsonObject.toString());
                outputStreamWriter.flush();
                outputStreamWriter.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void create(Contact contact, Context context) {
        try {
            File file = new File(context.getFilesDir(), "Contacts.json");
            if (!file.exists()) {
                return;
            }
            // Read the existing JSON array from the file, or create a new one if it doesn't exist
            JSONArray jsonArray = readFileContact(file, context);

            // Add the new object to the existing array
            String contactString = gson.toJson(contact);
            jsonArray.put(new JSONObject(contactString));

            // Encapsulate the updated array in a JSONObject with a name
            JSONObject rootJsonObject = new JSONObject();
            rootJsonObject.put("Contacts", jsonArray);

            // Write the updated JSONObject to the file
            writeFile(file, rootJsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(Context context, Contact oldContact, Contact newContact) {
        try {
            File file = new File(context.getFilesDir(), "Contacts.json");
            if (!file.exists()) {
                return;
            }

            // Read the existing JSON array from the file, or create a new one if it doesn't exist
            JSONArray jsonArray = readFileContact(file, context);

            // Find the object to update in the existing array
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("id").equals(oldContact.getId())) {
                    // Update the object
                    jsonObject.put("id", newContact.getId());
                    jsonObject.put("lastName", newContact.getLastName());
                    jsonObject.put("firstName", newContact.getFirstName());
                    break;
                }
            }

            // Encapsulate the updated array in a JSONObject with a name
            JSONObject rootJsonObject = new JSONObject();
            rootJsonObject.put("Contacts", jsonArray);

            // Write the updated JSONObject to the file
            writeFile(file, rootJsonObject);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public void delete(Context context, Contact contactToDelete) {
        try {
            File file = new File(context.getFilesDir(), "Contacts.json");
            if (!file.exists()) {
                return;
            }

            // Read the existing JSON array from the file, or create a new one if it doesn't exist
            JSONArray jsonArray = readFileContact(file, context);

            // Find the index of the object to delete in the existing array
            int indexToDelete = -1;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("id").equals(contactToDelete.getId())) {
                    indexToDelete = i;
                    break;
                }
            }

            // Remove the object from the array if it exists
            if (indexToDelete != -1) {
                jsonArray.remove(indexToDelete);
            }

            // Encapsulate the updated array in a JSONObject with a name
            JSONObject rootJsonObject = new JSONObject();
            rootJsonObject.put("Contacts", jsonArray);

            // Write the updated JSONObject to the file
            writeFile(file, rootJsonObject);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public Contact find(Context context, String id) {
        Contact contact = null;
        try {
            File file = new File(context.getFilesDir(), "Contacts.json");
            if (!file.exists()) {
                // The file doesn't exist, so the object can't be found
                return contact;
            }

            // Read the existing JSON array from the file, or create a new one if it doesn't exist
            JSONArray jsonArray = readFileContact(file, context);

            // Iterate over the array to find the object
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("id").equals(id)) {
                    // We found the object we're looking for
                    contact = new Contact(id, jsonObject.getString("lastName"), jsonObject.getString("firstName"));
                    // Do something with the object
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contact;
    }

    public ArrayList<Contact> findAll(Context context) {
        ArrayList<Contact> contacts = new ArrayList<>();

        try {
            File file = new File(context.getFilesDir(), "Contacts.json");
            if (!file.exists()) {
                // The file doesn't exist, so there are no objects to return
                return contacts;
            }

            // Read the existing JSON array from the file, or create a new one if it doesn't exist
            JSONArray jsonArray = readFileContact(file, context);

            // Iterate over the array and add each object to the list
            for (int i = 0; i < jsonArray.length(); i++) {
                String id = jsonArray.getJSONObject(i).getString("id");
                String lastName = jsonArray.getJSONObject(i).getString("lastName");
                String firstName = jsonArray.getJSONObject(i).getString("firstName");
                Contact contact = new Contact(id, lastName, firstName);

                contacts.add(contact);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contacts;
    }
    private JSONArray readFileContact(File file, Context context) throws JSONException {
        StringBuilder stringBuilder = new StringBuilder();
        if (file.length() != 0) {
            try {
                // Ouvrir le fichier pour lecture
                FileInputStream inputStream = context.openFileInput("Contacts.json");

                // Lire le contenu du fichier
                int data2;
                while ((data2 = inputStream.read()) != -1) {
                    stringBuilder.append((char) data2);
                }
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject rootJsonObject = new JSONObject(stringBuilder.toString());
            return rootJsonObject.getJSONArray("Contacts");
        } else {
            return new JSONArray();
        }
    }

    private void writeFile(File file, JSONObject rootJsonObject) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(rootJsonObject.toString());
        outputStreamWriter.flush();
        outputStreamWriter.close();
        fileOutputStream.close();
    }
}