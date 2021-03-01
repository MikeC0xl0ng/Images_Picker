package com.example.imagespickerjava;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ImageSwitcher imageIs;
    private Button previousBtn, nextBtn, pickImagesBtn;
    
    private ArrayList<Uri> imagesUris;
    
    private static final int PICK_IMAGES_CODE_= 0;
    
    int position = 0;           //contatore dell'immagine
    
    @Override //Override
    protected void onCreate(Bundle savedInstanceState) {        //onCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        imageIs = findViewById(R.id.imageIs);                   //imageIs - campo per visualizzare immagini
        previousBtn = findViewById(R.id.previousBtn);
        nextBtn = findViewById(R.id.nextBtn);
        pickImagesBtn = findViewById(R.id.pickImagesBtn);
        
        imagesUris = new ArrayList<>();
        
        imageIs.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });
        pickImagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImagesIntent();
            }
        });
        
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0){
                    position--;
                    imageIs.setImageURI(imagesUris.get(position));
                }else{
                    Toast.makeText(MainActivity.this, "No previous images..", Toast.LENGTH_SHORT).show();
                }
            }
        });
        
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < imagesUris.size() - 1) {
                    position++;
                    imageIs.setImageURI(imagesUris.get(position));               
                }
                else {
                    Toast.makeText(MainActivity.this, "No more images...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    private void pickImagesIntent(){
        Intent intent = new Intent();  //An intent is an abstract description of an operation to be performed
        intent.setType("image/*");  // specifichiamo il tipo di data di risposta, nel nostro caso voglio avere un output di image di tutti i tipi (*)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleziona Immagine"), PICK_IMAGES_CODE_);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if(requestCode == PICK_IMAGES_CODE_){
            
            if(resultCode == Activity.RESULT_OK){
                if (data.getClipData() != null){
                    int count = data.getClipData().getItemCount();      //int count = il numero di immagini presenti nel dispositivo
                    for (int i =0; i<count; i++){
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        imagesUris.add(imageUri);
                    }
                    imageIs.setImageURI(imagesUris.get(0));
                    position = 0;
                }else{
                    Uri imageUri = data.getData();
                    imagesUris.add(imageUri);
                    imageIs.setImageURI(imagesUris.get(0));
                    position = 0;
                }
            }
        }
    }
}