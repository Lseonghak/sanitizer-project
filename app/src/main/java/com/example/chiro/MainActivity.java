package com.example.chiro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RetrofitClient retrofitClient;
    private initMyApi initMyApi;

    EditText idText = findViewById(R.id.email);
    EditText passwordText = findViewById(R.id.password);
    CheckBox vaccin = findViewById(R.id.checkbox1);
    CheckBox negative = findViewById(R.id.checkbox2);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TODO Auto-generated method stub

        setContentView(R.layout.activity_main);

        Button summit = (Button) findViewById(R.id.summit);



//        //제일 처음 생성한 Retrofit class
//        RetrofitAPI retrofitAPI = RetrofitClient.getClient().create(RetrofitAPI.class);
//
//        //그 다음 생성한 인터페이스와 php로 넘길 데이터들
//        Call<List<TestItem>> call = retrofitAPI.getTestData("test");
//
//        //큐에 삽입
//        call.enqueue(new Callback<List<TestItem>>() {
//            //통신 성공
//            @Override
//            public void onResponse(@NonNull Call<List<TestItem>> call, @NonNull Response<List<TestItem>> response) {
//                if(response.isSuccessful()) {
//                    List<TestItem> items = response.body();
//                    for (TestItem item : items) {
//                        HashMap<String, String> tempData = new HashMap<>();
//                        tempData.put("email", item.getEmail());
//                        tempData.put("password",  item.getPassword());
//                        listViewAdapter.addItem(tempData);
//                    }
//                    listView.setAdapter(listViewAdapter);
//                }
//            }
//
//            //통신 실패
//            @Override
//            public void onFailure(@NonNull Call<List<TestItem>> call, @NonNull Throwable t) {
//                Toast.makeText(MainActivity.this, "ERROR: "+t.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        });

        summit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), nfcTest.class);
                startActivity(intent);
            }
        });
    }
    public void LoginResponse() {
        String userID = idText.getText().toString().trim();
        String userPassword = passwordText.getText().toString().trim();

        //loginRequest에 사용자가 입력한 id와 pw를 저장
        LoginRequest loginRequest = new LoginRequest(userID, userPassword);

        //retrofit 생성
        retrofitClient = RetrofitClient.getInstance();
        initMyApi = RetrofitClient.getRetrofitInterface();

        //loginRequest에 저장된 데이터와 함께 init에서 정의한 getLoginResponse 함수를 실행한 후 응답을 받음
        initMyApi.getLoginResponse(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                Log.d("retrofit", "Data fetch success");

                //통신 성공
                if (response.isSuccessful() && response.body() != null) {

                    //response.body()를 result에 저장
                    LoginResponse result = response.body();

                    //받은 코드 저장
                    String resultCode = result.getResultCode();

                    //받은 토큰 저장
                    String token = result.getToken();

                    String success = "200"; //로그인 성공
                    String errorId = "300"; //아이디 일치x
                    String errorPw = "400"; //비밀번호 일치x


                    if (resultCode.equals(success)) {
                        String userID = idText.getText().toString();
                        String userPassword = passwordText.getText().toString();

//                        //다른 통신을 하기 위해 token 저장
//                        setPreference(token,token);

//                        //자동 로그인 여부
//                        if (checkBox.isChecked()) {
//                            setPreference(autoLoginId, userID);
//                            setPreference(autoLoginPw, userPassword);
//                        } else {
//                            setPreference(autoLoginId, "");
//                            setPreference(autoLoginPw, "");
//                        }

                        Toast.makeText(MainActivity.this, userID + "님 환영합니다.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        intent.putExtra("userId", userID);
                        startActivity(intent);
                        MainActivity.this.finish();

                    } else if (resultCode.equals(errorId)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("알림")
                                .setMessage("아이디가 존재하지 않습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    } else if (resultCode.equals(errorPw)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("알림")
                                .setMessage("비밀번호가 일치하지 않습니다.\n 고객" +
                                        "센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("알림")
                                .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();

                    }
                }
            }


            //통신 실패
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("알림")
                        .setMessage("예기치 못한 오류가 발생하였습니다.\n 고객센터에 문의바랍니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
            }
        });
    }
//    //데이터를 내부 저장소에 저장하기
//    public void setPreference(String key, String value){
//        SharedPreferences pref = getSharedPreferences(DATA_STORE, MODE_PRIVATE);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString(key, value);
//        editor.apply();
//    }
//
//    //내부 저장소에 저장된 데이터 가져오기
//    public String getPreferenceString(String key) {
//        SharedPreferences pref = getSharedPreferences(DATA_STORE, MODE_PRIVATE);
//        return pref.getString(key, "");
//    }
    //키보드 숨기기
    private void hideKeyboard()
    {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(idText.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(passwordText.getWindowToken(), 0);
    }

    //화면 터치 시 키보드 내려감
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) ev.getX(), y = (int) ev.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}