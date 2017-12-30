package com.example.android.whoisit.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.whoisit.R;
import com.example.android.whoisit.WhoIsItApplication;
import com.example.android.whoisit.models.Student;

import java.io.FileOutputStream;
import java.util.Arrays;

import io.objectbox.Box;

public class AddStudentActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Box<Student> studentbox;
    private ImageView imageView;
    private Student student;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        WhoIsItApplication app = (WhoIsItApplication) getApplication();
        studentbox = app.getBoxStore().boxFor(Student.class);

        imageView = findViewById(R.id.studentImage);

        imageView.setImageResource(R.drawable.ic_camera);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_student:
                saveStudent();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStudent() {
        EditText naamEt = (EditText) findViewById(R.id.studentName);
        EditText trait1Et = (EditText) findViewById(R.id.trait1);
        EditText trait2Et = (EditText) findViewById(R.id.trait2);
        EditText trait3Et = (EditText) findViewById(R.id.trait3);

        String naam = naamEt.getText().toString().trim().toLowerCase();
        String trait1 = trait1Et.getText().toString().trim().toLowerCase();
        String trait2 = trait2Et.getText().toString().trim().toLowerCase();
        String trait3 = trait3Et.getText().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(naam)) {
            naamEt.setError("Naam verplicht");
            return;
        }
        for (Student student : studentbox.getAll()) {
            if (naam.equals(student.getName().trim().toLowerCase())) {
                naamEt.setError("Deze student bestaat al");
                return;
            }

        }
        if (TextUtils.isEmpty(trait1)) {
            trait1Et.setError("Kenmerken verplicht");
            return;
        }
        if (TextUtils.isEmpty(trait2)) {
            trait2Et.setError("Kenmerken verplicht");
            return;
        }
        if (TextUtils.isEmpty(trait3)) {
            trait3Et.setError("Kenmerken verplicht");
            return;
        }

        if (imageBitmap != null)
            new SaveFile().execute(naam);
        else {
            Toast.makeText(this.getBaseContext(), "Foto verplicht", Toast.LENGTH_SHORT).show();
            return;
        }

        student = new Student(0, naam, naam, Arrays.asList(trait1, trait2, trait3));
        studentbox.put(student);

        Intent intent = new Intent(AddStudentActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

    public class SaveFile extends AsyncTask<String, String, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... aurl) {
            FileOutputStream foStream;
            try {
                foStream = getApplicationContext().openFileOutput(aurl[0], Context.MODE_PRIVATE);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, foStream);
                foStream.flush();
                foStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);

        }
    }

    public void savePersonalPicture(Bitmap bitmap) {
        new SaveFile().execute("profielfoto" + bitmap);
    }
}
