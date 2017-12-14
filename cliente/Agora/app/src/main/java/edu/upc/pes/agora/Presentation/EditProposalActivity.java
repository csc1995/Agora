package edu.upc.pes.agora.Presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.upc.pes.agora.Logic.ServerConection.PutAsyncTask;
import edu.upc.pes.agora.R;

import static edu.upc.pes.agora.Logic.Utils.Constants.SH_PREF_NAME;

public class EditProposalActivity extends AppCompatActivity {

    EditText editTitle;
    EditText editDescription;
    Button saveButton;
    Button cancelButton;
    Button editPosButton;

    String newTitle;
    String newDescription;
    String token;

    private ProgressBar prog;

    SharedPreferences prefs;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_proposal);

        editTitle = (EditText) findViewById(R.id.editTitle);
        editDescription = (EditText) findViewById(R.id.editDescription);
        saveButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        editPosButton = (Button) findViewById(R.id.editPosButton);
        prog = (ProgressBar) findViewById(R.id.saveprogressbar);

        prefs = this.getSharedPreferences(SH_PREF_NAME, MODE_PRIVATE);
        edit = prefs.edit();

        Intent i = getIntent();
        if(i.hasExtra("Title")) {
            editTitle.setText(i.getStringExtra("Title"));
        }
        if(i.hasExtra("Description")) {
            editDescription.setText(i.getStringExtra("Description"));
        }

        final int id = i.getIntExtra("id",0);

        final Resources res = this.getResources();

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                startActivity(intent);
            }
        });

        editPosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddLocationActivity.class);
                i.putExtra("Title", editTitle.getText().toString());
                i.putExtra("Description", editDescription.getText().toString());
                i.putExtra("CallingActivity", "Edit");
                if (getIntent().hasExtra("lat") && getIntent().getDoubleExtra("lat",0) != 0){
                    i.putExtra("lat", getIntent().getDoubleExtra("lat",0));
                    i.putExtra("lng", getIntent().getDoubleExtra("lng",0));
                }
                startActivityForResult(i,1);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                if (editTitle.getText().toString().equals("") ){
                    String error = res.getString(R.string.errorTitulo);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                }
                else if (editDescription.getText().toString().equals("")){
                    String error = res.getString(R.string.errorDescripcion);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                }
                else {

                    saveButton.setVisibility(View.GONE);
                    prog.setVisibility(View.VISIBLE);

                    JSONObject values = new JSONObject();
                    JSONObject location = new JSONObject();

                    try {
                        newTitle = editTitle.getText().toString();
                        newDescription = editDescription.getText().toString();
                        if (prefs.contains("token")){
                            token = prefs.getString("token","");
                        }
                        values.put("title", newTitle);
                        values.put("content", newDescription);
                        location.put("lat", getIntent().getDoubleExtra("lat",0));
                        location.put("long", getIntent().getDoubleExtra("lng",0));
                        values.put("location", location);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String editUrl = "https://agora-pes.herokuapp.com/api/proposal/"+id;
                    Log.i("Link",editUrl);

                    // nou server : agora-pes.herokuapp.com/api/proposal
                    new PutAsyncTask(editUrl, EditProposalActivity.this){
                        protected void onPostExecute(JSONObject resObject) {
                            Boolean result = false;
                            String error = res.getString(R.string.errorEdit);

                            try {

                                if (resObject.has("success")) {
                                    result = resObject.getBoolean("success");
                                }
                                if (!result && resObject.has("errorMessage")) {
                                    error = resObject.getString("errorMessage");
                                    Log.i("asdCreacion", error);
                                    if(resObject.getString("errorMessage").equals("Selected location outside of allowed zone.")){
                                        Toast.makeText(getApplicationContext(), res.getString(R.string.errorPosition), Toast.LENGTH_LONG).show();
                                    }else {
                                        Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }.execute(values);

                    Intent myIntent = new Intent(getApplicationContext(), MyProposalsActivity.class);
                    startActivity(myIntent);

                }
            }
        });

    }

}
