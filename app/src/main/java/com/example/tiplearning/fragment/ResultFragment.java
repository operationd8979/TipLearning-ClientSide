package com.example.tiplearning.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tiplearning.R;
import com.example.tiplearning.model.runtime.QuizResponse;
import com.example.tiplearning.model.runtime.RequestFindQuiz;
import com.example.tiplearning.retrofit.RetrofitInstance;
import com.example.tiplearning.service.UserService;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultFragment extends Fragment {

    private Button btnTake;
    private Button btnCreateQuiz;
    private static EditText etRecognizedText;
    private static String userId;

    private Uri imageUri = null;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermission;
    private String[] storagePermission;

    private ProgressDialog progressDialog;
    private TextRecognizer textRecognizer;

    private RadioGroup radioGroup;
    private String type = "ENGLISH";


    public ResultFragment(String userId) {
        this.userId = userId;
    }

    public static QuizFragment newInstance(String userId) {
        QuizFragment fragment = new QuizFragment(userId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result, container, false);
        this.btnTake = view.findViewById(R.id.btnTakeImageCreateQuiz);
        this.btnCreateQuiz = view.findViewById(R.id.btnCreateQuiz);
        this.etRecognizedText = view.findViewById(R.id.etCreateQuiz);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        btnTake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputImageDialog();
            }
        });

        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = etRecognizedText.getText().toString();
                if(query.equals("")){
                    Toast.makeText(getActivity(), "Please recognize text", Toast.LENGTH_SHORT).show();
                }else{
                    callApiCreateQuiz(query);
                }
            }
        });

        radioGroup = view.findViewById(R.id.rdGroupTypeResult);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rdEnglistR) {
                    type = "ENGLISH";
                } else if (checkedId == R.id.rdItR) {
                    type = "IT";
                } else if (checkedId == R.id.rdDifferentR) {
                    type = "DIFFERENT";
                }
            }
        });

        return view;
    }

    private static void extractQuestionsAndAnswers(String text) {
        Pattern pattern = Pattern.compile("^\\d+\\..*$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(text);
        List<String> groupList = new ArrayList<>();
        while (matcher.find()) {
            String questionAndOptions = matcher.group();
            groupList.add(questionAndOptions);
        }
        groupList.add("end");
        List<String> mutiSe = Arrays.stream(text.split("\\n")).collect(Collectors.toList());
        int i = 0;
        String question = "";
        for(String s:mutiSe){
            if(s.contains(groupList.get(i))){
                i++;
                question+=s+"\n";
            }
            else{
                question+=s.substring(2)+"\n";
            }
        }
//        String question = "";
//        int i = 0;
//        List<String> finalList = new ArrayList<>();
//        int k = 0;
//        for(String s:mutiSe){
//            if(s.contains(groupList.get(i))){
//                if(k==5){
//                    finalList.add(question);
//                }
//                k = 0;
//                question = "";
//                i++;
//            }
//            if(k==0){
//                question+= "\n"+s;
//            }
//            else{
//                question+= "\n"+s.substring(2);
//            }
//            k++;
//        }
//        if(k==5){
//            finalList.add(question);
//        }
//        String questions = "";
//        for(String s:finalList){
//            questions+=s+"\n";
//            Log.d("ChipEnd", s);
//        }
        etRecognizedText.setText(question);
    }

    private void callApiCreateQuiz(String quizContent) {
        Call<String> call = RetrofitInstance.getRetrofitInstance().create(UserService.class).createQuiz(quizContent,type,userId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Toast.makeText(getActivity(), "Đã gửi lên máy chủ", Toast.LENGTH_SHORT).show();
                etRecognizedText.setText("");
//                if (response.isSuccessful()) {
//                    showToast("Đã gửi báo cáo thành công");
//                } else {
//                    showToast("Đã gửi báo cáo thất bại");
//                }
            }
            @Override//                Log.e("Network Request", "Error", t);

            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getActivity(), "Đã gửi lên máy chủ", Toast.LENGTH_SHORT).show();
                etRecognizedText.setText("");
            }
        });
    }

    private void recognizeTextFromImage() {
        Log.d("TAG", "recognizeTextFromImage: ");
        progressDialog.setMessage("Preparing image...");
        progressDialog.show();
        try {
            InputImage inputImage = InputImage.fromFilePath(getActivity(),imageUri);
            progressDialog.setMessage("Recognizing text...");
            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(text -> {
                        progressDialog.dismiss();
                        Log.d("TAG", "onSuccess: recognizedText: "+text.getText());
                        etRecognizedText.setText(text.getText());
                        //extractQuestionsAndAnswers(text.getText());
                        radioGroup.setVisibility(View.VISIBLE);
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Log.e("TAG", "onFailure: "+e.getMessage());
                        Toast.makeText(getActivity(), "Failed recognizing text due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }catch (Exception ex){
            progressDialog.dismiss();
            Log.e("TAG", "recognizeTextFromImage: "+ex.getMessage() );
            Toast.makeText(getActivity(), "Failed preparing image dua to "+ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), btnTake);
        popupMenu.getMenu().add(Menu.NONE,1,1,"Camera");
        popupMenu.getMenu().add(Menu.NONE,2,2,"Gallery");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if(id==1){
                Log.d("TAG", "onMenuItemClick: Camera Clicked");
                if(!checkCameraPermission()){
                    requestCameraPermission();
                }else{
                    pickImageCamera();
                }
            }else if(id==2){
                Log.d("TAG", "onMenuItemClick: Gallery Clicked");
                if(!checkStoragePermission()){
                    requestStoragePermission();
                }else{
                    pickFromGallery();
                }
            }
            return false;
        });
    }

    private void pickFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    imageUri = result.getData().getData();
                    if(imageUri==null){
                        Toast.makeText(getActivity(), "Please take image", Toast.LENGTH_SHORT).show();
                    }else{
                        recognizeTextFromImage();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                }
            });

    private void pickImageCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image To Text");
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        cameraActivityResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == Activity.RESULT_OK){
                    if(imageUri==null){
                        Toast.makeText(getActivity(), "Please take image", Toast.LENGTH_SHORT).show();
                    }else{
                        recognizeTextFromImage();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "Cancelled...", Toast.LENGTH_SHORT).show();
                }
            });

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        requestPermissions(storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        requestPermissions(cameraPermission,CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        pickImageCamera();
                    }else{
                        Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickFromGallery();
                    }else{
                        pickFromGallery();
                        //Toast.makeText(getActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }
}