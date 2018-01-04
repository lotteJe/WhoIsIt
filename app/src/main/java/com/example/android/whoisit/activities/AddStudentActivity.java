package com.example.android.whoisit.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.whoisit.R;
import com.example.android.whoisit.WhoIsItApplication;
import com.example.android.whoisit.models.Student;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import io.objectbox.Box;

public class AddStudentActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Box<Student> studentbox;
    private ImageView imageView;
    private Student student;
    private Bitmap imageBitmap;
    private long studentId;
    private EditText naamEt;
    private EditText trait1Et;
    private EditText trait2Et;
    private EditText trait3Et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        WhoIsItApplication app = (WhoIsItApplication) getApplication();
        studentbox = app.getBoxStore().boxFor(Student.class);

        Intent intent = getIntent();
        studentId = intent.getLongExtra("studentId", 0);

        naamEt = (EditText) findViewById(R.id.studentName);
        trait1Et = (EditText) findViewById(R.id.trait1);
        trait2Et = (EditText) findViewById(R.id.trait2);
        trait3Et = (EditText) findViewById(R.id.trait3);
        imageView = findViewById(R.id.studentImage);

        imageView.setImageResource(R.drawable.ic_camera);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        if (studentId == 0) setTitle(getString(R.string.add_activity_new_student));
        else {
            setTitle(getString(R.string.add_activity_edit_student));
            updateView();
        }
    }

    private void updateView() {

        student = studentbox.get(studentId);

        naamEt.setText(student.getName());
        trait1Et.setText(student.getTraits().get(0));
        trait2Et.setText(student.getTraits().get(1));
        trait3Et.setText(student.getTraits().get(2));

        imageBitmap = loadImageBitmap(this, student.getImage());
        imageView.setImageBitmap(imageBitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_student:
                saveStudent();
                return true;
            case R.id.delete_student:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteStudent();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteStudent() {
        if (studentId != 0) {
            studentbox.remove(studentId);
        }
        finish();
    }

    private void saveStudent() {
        String naam = naamEt.getText().toString().trim().toLowerCase();
        String trait1 = trait1Et.getText().toString().trim().toLowerCase();
        String trait2 = trait2Et.getText().toString().trim().toLowerCase();
        String trait3 = trait3Et.getText().toString().trim().toLowerCase();

        if (TextUtils.isEmpty(naam)) {
            naamEt.setError("Naam verplicht");
            return;
        }
        if (studentId == 0) {
            for (Student student : studentbox.getAll()) {
                if (naam.equals(student.getName().trim().toLowerCase())) {
                    naamEt.setError("Deze student bestaat al");
                    return;
                }
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
        if (studentId == 0) {
            student = new Student(0, naam, naam, Arrays.asList(trait1, trait2, trait3));
        } else {
            student = studentbox.get(studentId);
            student.setImage(naam);
            student.setName(naam);
            student.setTraits(Arrays.asList(trait1, trait2, trait3));
        }

        studentbox.put(student);
        finish();
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

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            String path = imageName.replaceAll(".png|.jpg", "");
            File file = context.getApplicationContext().getFileStreamPath(path);
            if (file.exists()) Log.d("file", imageName);
            fiStream = context.openFileInput(path);
            bitmap = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
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
