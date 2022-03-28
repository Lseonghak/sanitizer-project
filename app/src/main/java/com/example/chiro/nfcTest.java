package com.example.chiro;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class nfcTest extends Activity {

    /*
     * 위젯 관련 변수
     */
    private TextView text; //텍스트 뷰 변수

    /*
     * NFC 통신 관련 변수
     */
    private NfcAdapter         nfcAdapter;
    private NdefMessage         mNdeMessage; //NFC 전송 메시지



    /*
     * 액티비티 화면을 만들고 xml 파일을 호출하는 메소드이다.
     * 이 함수내에 사용하는 위젯을 선언한다.
     * 메소드의 소스 형식과 순서는 다음과 같다.
     * 1. 위젯 지정
     * 2. NFC 단말기 정보 가져오기
     * 3. NdefMessage 타입 mNdeMessage 변수에 NFC 단말기에 보낼 정보를 넣는다.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc_test);
        // TODO Auto-generated method stub

        /*
         * 1. 위젯 지정
         */
        text = (TextView)findViewById(R.id.text);                                                              // 텍스트뷰




        /*
         * 2. NFC 단말기 정보 가져오기
         */
        nfcAdapter = NfcAdapter.getDefaultAdapter(this); // nfc를 지원하지않는 단말기에서는 null을 반환.

        if(nfcAdapter != null)
        {
            text.setText("NFC 단말기를 접촉해주세요"+nfcAdapter+"");
        }
        else
        {
            text.setText("NFC 기능이 꺼져있습니다. 켜주세요"+nfcAdapter+"");
        }

        /*
         * 3. NdefMessage 타입 mNdeMessage 변수에 NFC 단말기에 보낼 정보를 넣는다.
         */
        mNdeMessage=new NdefMessage(
                new NdefRecord[]{
                        createNewTextRecord("이름 : 홍길동", Locale.ENGLISH, true),                        //텍스트 데이터
                        createNewTextRecord("전화번호 : 010-1234-5678", Locale.ENGLISH, true),    //텍스트 데이터
                        createNewTextRecord("자격증번호 : 123456", Locale.ENGLISH, true),           //텍스트 데이터
                        createNewTextRecord("전화번호 : 111-2222-3333", Locale.ENGLISH, true),    //텍스트 데이터
                        createNewTextRecord("된다!! : 우왕!!", Locale.ENGLISH, true),                  //텍스트 데이터

                }
        );

    }


    /*
     * 액티비티 화면이 나오기 전에 실행되는 메소드이다.
     * onCreate 에서 정한 mNdeMessage 의 데이터를 NFC 단말기에 전송한다.
     */
    @Override
    protected void onResume() {

        super.onResume();

        if (nfcAdapter != null) {

            nfcAdapter.enableForegroundNdefPush(this, mNdeMessage);

        }

    }

    /*
     * 액티비티 화면이 종료되면 NFC 데이터 전송을 중단하기 위해 실행되는 메소드이다.
     */
    @Override
    protected void onPause() {

        super.onPause();

        if (nfcAdapter != null) {

            nfcAdapter.disableForegroundNdefPush(this);

        }

    }

    /*
     * 텍스트 형식의 데이터를 mNdeMessage 변수에 넣을 수 있도록 변환해 주는 메소드이다.
     */
    public static NdefRecord createNewTextRecord(String text, Locale locale, boolean encodelnUtf8){

        byte[] langBytes = locale.getLanguage().getBytes(Charset.forName("US-ASCII"));

        Charset utfEncoding = encodelnUtf8 ? Charset.forName("UTF-8"):Charset.forName("UTF-16");

        byte[] textBytes = text.getBytes(utfEncoding);

        int utfBit = encodelnUtf8 ? 0:(1<<7);
        char status = (char)(utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        data[0] = (byte)status;
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,NdefRecord.RTD_TEXT, new byte[0], data);
    }

}

//public class nfcTest extends AppCompatActivity implements NfcAdapter.CreateNdefMessageCallback{
//
//    NfcAdapter mNfcAdapter;
//    EditText editText;
//    String webAdress;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        editText = (EditText) findViewById(R.id.editText);
//        mNfcAdapter =NfcAdapter.getDefaultAdapter(this);
//        if (mNfcAdapter == null) {
//            Toast.makeText(this, "NFC is not available", Toast.LENGTH_LONG).show();
//            finish();
//            return;
//        }
//        mNfcAdapter.setNdefPushMessageCallback(this, this);
//    }
//
//    @Override
//    public NdefMessage createNdefMessage(NfcEvent event) {
//        String telnum = editText.getText().toString();
//        NdefMessage msg = null;
//        try {
//            msg = new NdefMessage(
//                    new NdefRecord[]{ createMimeRecord(telnum, Locale.KOREAN, true)});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return msg;
//    }
//
//    public NdefRecord createMimeRecord(String text, Locale locale, boolean encodeInUtf8) throws IOException {
//        final byte[] langBytes = locale.getLanguage().getBytes(
//                StandardCharsets.US_ASCII);
//        final Charset utfEncoding = encodeInUtf8 ? StandardCharsets.UTF_8 : Charset.forName("UTF-16");
//        final byte[] textBytes = text.getBytes(utfEncoding);
//        final int utfBit = encodeInUtf8 ? 0 : (1<<7);
//        final char status = (char) (utfBit + langBytes.length);
////        final byte[] data = Bytes.concat(new byte[]{(byte) status}, langBytes, textBytes);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        baos.write(new byte[]{(byte)status});
//        baos.write(langBytes);
//        baos.write(textBytes);
//        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN,   NdefRecord.RTD_TEXT,
//                new byte[0], baos.toByteArray());
//    }
//
//}