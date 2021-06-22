package com.pucpr.realtimefirebase.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pucpr.realtimefirebase.R;
import com.pucpr.realtimefirebase.model.Colecao;
import com.pucpr.realtimefirebase.model.DataModel;
import com.pucpr.realtimefirebase.model.Volume;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FormActivity extends AppCompatActivity {

    static final int CAMERA_PERMISSION_CODE = 2001;
    static final int CAMERA_INTENT_CODE = 3001;
    static final int SELECT_PICTURE = 200;

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    ImageView imageViewCamera;
    String picturePath;

    EditText editTextTitulo;
    EditText editTextAutor;
    EditText editTextGenero;
    EditText editTextUltimo;
    EditText editTextComprado;

    CheckBox checkBoxCompleto;
    CheckBox checkBoxUnico;

    Button buttonSelecionar;

    int position;
    Colecao colecao = new Colecao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        setTitle("Coleção");

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        imageViewCamera = findViewById(R.id.imageViewList);

        editTextTitulo = findViewById(R.id.editTextTitulo);
        editTextAutor = findViewById(R.id.editTextAutor);
        editTextGenero = findViewById(R.id.editTextGenero);
        editTextUltimo = findViewById(R.id.editTextUltimo);
        editTextComprado = findViewById(R.id.editTextComprado);

        checkBoxCompleto = findViewById(R.id.checkBoxCompeleto);
        checkBoxUnico = findViewById(R.id.checkBoxUnico);

        buttonSelecionar = findViewById(R.id.buttonSelecionar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getInt("position");

            colecao = DataModel.getInstance().getColecoes().get(position);
            editTextTitulo.setText(colecao.getTitulo());
            editTextAutor.setText(colecao.getAutor());
            editTextGenero.setText(colecao.getGenero());
            editTextUltimo.setText(String.valueOf(colecao.getUltimoLido()));
            editTextComprado.setText(String.valueOf(colecao.getUltimoComprado()));
            checkBoxCompleto.setChecked(colecao.getCompleto() != 0);
            checkBoxUnico.setChecked(colecao.getVolumeUnico() != 0);

        }else{
            position = -1;
        }

        buttonSelecionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(colecao.getCaminhoImg() != null) {
            File file = new File(colecao.getCaminhoImg());
            if (file.exists()) {
                imageViewCamera.setImageURI(Uri.fromFile(file));
            }
        }
    }


    public void buttonCameraClicked(View view){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestCameraPermission();
        }else{
            sendCameraIntent();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestCameraPermission(){
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)){
            if(checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{ Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
            }else{
                sendCameraIntent();
            }
        }else{
            Toast.makeText(FormActivity.this,"Nenhuma câmera disponível",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == CAMERA_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                sendCameraIntent();
            }else{
                Toast.makeText(FormActivity.this,"Permissão da câmera negada",Toast.LENGTH_LONG).show();
            }
        }
    }

    public void sendCameraIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION,true);

        if(intent.resolveActivity(getPackageManager()) != null){
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            String picName = "pic_"+timeStamp;

            File dir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File pictureFile = null;

            try {
                pictureFile = File.createTempFile(picName,".jpg",dir);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(pictureFile != null){
                picturePath = pictureFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(FormActivity.this,
                        "com.pucpr.realtimefirebase.fileprovider", pictureFile
                );
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
                startActivityForResult(intent,CAMERA_INTENT_CODE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_INTENT_CODE){
            if(resultCode == RESULT_OK){
                File file = new File(picturePath);
                if(file.exists()){
                    imageViewCamera.setImageURI(null);
                    imageViewCamera.setImageURI(Uri.fromFile(file));
                    colecao.setCaminhoImg(picturePath);
                }
            }else{
                Toast.makeText(FormActivity.this,"Problema ao pegar a foto da câmera", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imageViewCamera.setImageURI(null);
                    imageViewCamera.setImageURI(selectedImageUri);

                    String[] filePath = { MediaStore.Images.Media.DATA };
                    Cursor c = getContentResolver().query(selectedImageUri,filePath, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePath[0]);
                    picturePath = c.getString(columnIndex);
                    c.close();
                    colecao.setCaminhoImg(picturePath);
                }
            }
        }
    }

    public void btnSaveClicked(View view){
        boolean checkedCompleto = checkBoxCompleto.isChecked();
        boolean checkedUnico = checkBoxUnico.isChecked();
        int ultimoComprado = Integer.parseInt(editTextComprado.getText().toString());
        int ultimoLido = Integer.parseInt(editTextUltimo.getText().toString());

        Colecao c = new Colecao(colecao.getId(), editTextTitulo.getText().toString(),
                        picturePath == null ? colecao.getCaminhoImg() : picturePath,
                        checkedCompleto ? 1 : 0, checkedUnico ? 1 : 0,
                        ultimoLido, ultimoComprado,
                        editTextGenero.getText().toString(), editTextAutor.getText().toString());

        DatabaseReference rootReference = firebaseDatabase.getReference();

        if (position >= 0) {
            c.setVolumes(DataModel.getInstance().getColecoes().get(position).getVolumes());
        } else {
            c.setVolumes(new ArrayList<>());

            for (int i = 1; i <= ultimoComprado; i++) {
                Volume v = new Volume(i, i <= ultimoLido ? "Lido" : "Comprado");
                if (position >= 0 && i <= c.getVolumes().size()) {
                    c.getVolumes().set(i - 1, v);
                } else {
                    c.getVolumes().add(v);
                }
            }
        }

        if(position >= 0)
            DataModel.getInstance().getColecoes().set(position, c);
        else
            DataModel.getInstance().getColecoes().add(c);

        rootReference.child(firebaseUser.getUid()).setValue(DataModel.getInstance().getColecoes())
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Item adicionado com sucesso", Toast.LENGTH_LONG).show();
                    }
                });

        int p = position >=0 ? position : (DataModel.getInstance().getColecoes().size()-1);
        DataModel.getInstance().setPosicao(p);

        Intent intent = new Intent(FormActivity.this, VolumeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void imageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PICTURE);
    }
}