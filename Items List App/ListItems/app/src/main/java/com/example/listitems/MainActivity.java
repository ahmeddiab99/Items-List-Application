package com.example.listitems;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class MainActivity extends AppCompatActivity {


    String url = " https://fetch-hiring.s3.amazonaws.com/hiring.json";

    TableView tableView;

    String[] tableHeaders = {"List Id", "Id", "Name"};

    ArrayList<Item> items;

    ArrayList<Integer> listIds;

    ArrayList<Item> allItemsGrouped;

    String[][] tableData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //listItems = findViewById(R.id.listItems);

        tableView = findViewById(R.id.tableView);
        tableView.setColumnCount(3);
        tableView.setBackgroundColor(Color.WHITE);

        items = new ArrayList<>();

        listIds = new ArrayList<>();

        allItemsGrouped = new ArrayList<>();

        final RequestQueue queue = Volley.newRequestQueue(this);

        // make a jsonarray request to get that jsonarray data from the url specified.
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    Item item;
                    @Override
                    public void onResponse(JSONArray response) {

                        // loop through the response.
                        for(int i = 0; i < response.length(); i++){
                            try {
                                // create a jsonobject and add the response data to it.
                                JSONObject object = response.getJSONObject(i);

                                // create an object from the class item and pass the response data to it.
                                item = new Item();
                                item.setId(Integer.parseInt(object.getString("id")));
                                item.setListId(Integer.parseInt(object.getString("listId")));

                                // if name is not empty and is not equal to null, split the name and get the number only.
                                if(!object.getString("name").isEmpty() && object.getString("name") != "null"){
                                    String segments[] =  object.getString("name").split(" ");
                                    int temp = Integer.parseInt(segments[1]);
                                    item.setName(temp);

                                    // add item to the list of items.
                                    items.add(item);
                                }

                                // if the list of ids doesn't contin the listid, add the listid to it, so that we don't have any repeated listid in the list.
                                if(!listIds.contains(item.getListId())){
                                    listIds.add(item.getListId());
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        for(int i = 0; i < items.size(); i++){
                            Log.d("request", "Response: " + items.get(i));
                        }

                        // sort the list of ids.
                        selectionSort(listIds);
                        for(int i = 0; i < listIds.size(); i++){
                            Log.d("request", "Response: " + listIds.get(i));
                        }



                        for(int i = 0; i < listIds.size(); i++){
                            ArrayList<Item> groupItems = new ArrayList<>();

                            for(int j = 0; j < items.size(); j++){
                                // if the listid of item in index j is equal to the listid in idex i, add the item to groupItems.
                                if(items.get(j).getListId() == listIds.get(i)){
                                    groupItems.add(items.get(j));
                                }
                            }

                            // sort the group of items with the same listid by name.
                            sortItemsByName(groupItems);

                            // add the group of items to the list of all groups of items.
                            allItemsGrouped.addAll(groupItems);

                        }

                        // call the method that populates the 2d array with the items data.
                        populateData();

                        // set the header and the data for the table.
                        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(MainActivity.this, tableHeaders));
                        tableView.setDataAdapter(new SimpleTableDataAdapter(MainActivity.this, tableData));

                        Log.d("request", "Response: " + "succeeded");

                    }

                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("request", "Error: " + error.getMessage());
                    }
                }
        );


        queue.add(request);


    }

    // method that sort the arraylist of integers in ascending order.
    public void selectionSort(ArrayList<Integer> list){
        for(int i = 0; i < list.size() - 1; i++){
            for(int j = i + 1; j < list.size(); j++){
                int temp = 0;

                // if number at index j is smaller than at index i, swap them.
                if(list.get(j) < list.get(i)){

                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }

        }
    }

    // method that sort the arraylist of items by name.
    public void sortItemsByName(ArrayList<Item> group_items){
        for(int i = 0; i < group_items.size() - 1; i++){
            for(int j = i + 1; j < group_items.size(); j++){
                Item temp;

                // if name at index j is smaller than at index i, swap them.
                if(group_items.get(j).getName() < group_items.get(i).getName()){

                    temp = group_items.get(i);
                    group_items.set(i, group_items.get(j));
                    group_items.set(j, temp);
                }
            }

        }
    }

    // method that populates the 2d array with the items data, so that we can populate the table with the 2d array.
    public void populateData(){
        tableData = new String[allItemsGrouped.size()][3];

        for(int i = 0; i < allItemsGrouped.size(); i++){

            Item item = allItemsGrouped.get(i);

            tableData[i][0] = String.valueOf(item.getListId());
            tableData[i][1] = String.valueOf(item.getId());
            tableData[i][2] = "Item " + item.getName();


        }

    }

}