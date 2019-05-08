package com.sana.notesapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    ListView listView;
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> arrayAdapter;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId()==R.id.addNote)
        {
            Intent intent= new Intent(getApplicationContext(), NoteEditorActivity.class);
            startActivity(intent);
            return  true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences=this.getSharedPreferences("com.sana.notesapp", Context.MODE_PRIVATE);

        listView=(ListView) findViewById(R.id.listView);
        HashSet<String> set =(HashSet<String>) sharedPreferences.getStringSet("notes", null);
        if(set==null) {
            notes.add("Example note");
        }
        else{
            notes=new ArrayList(set);
        }
        arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent= new Intent(getApplicationContext(), NoteEditorActivity.class);
                intent.putExtra("noteID", position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_input_delete)
                        .setTitle("Delete note")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notes.remove(position);
                                arrayAdapter.notifyDataSetChanged();
                                HashSet<String> set= new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }
}
