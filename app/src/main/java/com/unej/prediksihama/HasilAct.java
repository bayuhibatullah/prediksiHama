package com.unej.prediksihama;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HasilAct extends AppCompatActivity {

    private double suhuX, kelembabanX, curahHujanX;
    private String lokasiSawah, prediksiHama;
    private String[] kelas;
    private double[] distanceSemua;
    private double latitude, longitude;
    private RequestQueue mQueue;
    private Map<String, String> pencegahan = new HashMap<String, String>();
    RecyclerView recyclerView;
    TextView title, subtitle, lokasi, textLoading, caraPencegahan;
    ImageView thumbnail;
    Button btnBack;
    Animation fadeIn;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hasil);

        mQueue = Volley.newRequestQueue(this);

        recyclerView = findViewById(R.id.recycler_view);
        title = findViewById(R.id.title);
        subtitle = findViewById(R.id.subtitle);
        lokasi = findViewById(R.id.lokasi);
        textLoading = findViewById(R.id.textLoading);
        thumbnail = findViewById(R.id.thumbnail);
        btnBack = findViewById(R.id.btnBack);
        caraPencegahan = findViewById(R.id.cara_pencegahan);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        caraPencegahan.setVisibility(View.GONE);
        btnBack.setEnabled(false);

        this.latitude = getIntent().getDoubleExtra("latitude", 0);
        this.longitude = getIntent().getDoubleExtra("longitude", 0);
        this.lokasiSawah = getIntent().getStringExtra("lokasi");
        Log.d("latitude", latitude+"");
        Log.d("longitude", longitude+"");

        getForecastKecamatan(latitude, longitude);
    }

    private void getForecastKecamatan(double lat, double lng){
        String url = "https://api.weatherbit.io/v2.0/forecast/daily?&lat="+lat+"&lon="+lng+"&key="+getString(R.string.WEATHERBIT_KEY);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("data");
                    int length = jsonArray.length();
                    double[] suhu = new double[length];
                    int[] kelembaban = new int[length];
                    double[] curahHujan = new double[length];
                    for (int i=0; i<length; i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        suhu[i] = object.getDouble("temp");
                        kelembaban[i] = object.getInt("rh");
                        curahHujan[i] = object.getDouble("precip");
                        if (i<3){
                            Log.d("suhu forecast", suhu[i]+"");
                            Log.d("kelembaban forecast", kelembaban[i]+"");
                            Log.d("curahH forecast", curahHujan[i]+"");
                        }
                    }
                    averageData(suhu, kelembaban, curahHujan);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private void averageData(double[] suhu, int[] kelembaban, double[] curahHujan){
        double averageSuhu = 0;
        double averageCurahHujan = 0;
        int averageKelembaban = 0;
        for (int i=0; i<suhu.length; i++){
            averageSuhu = averageSuhu + suhu[i];
        }
        for (int i=0; i<kelembaban.length; i++){
            averageKelembaban = averageKelembaban + kelembaban[i];
        }
        for (int i=0; i<curahHujan.length; i++){
            averageCurahHujan = averageCurahHujan + curahHujan[i];
        }

        averageSuhu = averageSuhu/suhu.length;
        averageCurahHujan = averageCurahHujan/curahHujan.length;
        averageKelembaban = averageKelembaban/kelembaban.length;

        Log.d("averageSuhu", averageSuhu+"");
        Log.d("averageCurahHujan", averageCurahHujan+"");
        Log.d("averageKelembaban", averageKelembaban+"");

        convertToOrdinal(averageSuhu, averageKelembaban, averageCurahHujan);
    }

    private void convertToOrdinal(double suhu, int kelembaban, double curahHujan){
        if (suhu<24){
            suhuX = 1;
        }else if(suhu>32){
            suhuX = 3;
        }else{
            suhuX = 2;
        }

        if (kelembaban<45){
            kelembabanX = 1;
        }else  if(kelembaban>65){
            kelembabanX = 3;
        }else{
            kelembabanX = 2;
        }

        if (curahHujan<2.5){
            curahHujanX = 1;
        }else if(curahHujan>7.6){
            curahHujanX = 3;
        }else{
            curahHujanX = 2;
        }

        normasilasiDataX();
    }

    private void normasilasiDataX(){
        suhuX = normalisasi3(suhuX);
        kelembabanX = normalisasi3(kelembabanX);
        curahHujanX = normalisasi3(curahHujanX);

        Log.d("suhuX norm", String.valueOf(suhuX));
        Log.d("kelembabanX norm", String.valueOf(kelembabanX));
        Log.d("curahHX norm", String.valueOf(curahHujanX));

        getTrainingSetAmount();
    }

    private void getTrainingSetAmount(){
        db.document("jumlah data training/jumlah data training").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                int jmlTrainingSet = (int)Math.round(documentSnapshot.getDouble("jumlah data training"));
                Log.d("jml training set", jmlTrainingSet + " ");
                kelas = new String[jmlTrainingSet];
                distanceSemua = new double[jmlTrainingSet];
                getTrainingSetData();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Get Jml Data training", e + "");
            }
        });
    }

    private void getTrainingSetData(){
        db.collection("trainingSet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    int i=-1;
                    for (QueryDocumentSnapshot document : task.getResult()){
                        Log.d("======ITERASI KE-", String.valueOf(i+2)+"======");
                        i++;
                        double suhuDb = document.getDouble("suhu");
                        Log.d("suhuDb", String.valueOf(suhuDb));
                        double suhuDbNorm = normalisasi3(suhuDb);
                        Log.d("suhuDb Norm", String.valueOf(suhuDbNorm));

                        double kelembabanDb = document.getDouble("kelembaban");
                        Log.d("kelembabanDb", String.valueOf(kelembabanDb));
                        double kelembabanDbNorm = normalisasi3(kelembabanDb);
                        Log.d("kelembabanDb Norm", String.valueOf(kelembabanDbNorm));

                        double curahHujanDb = document.getDouble("curah hujan");
                        Log.d("curahHujanDb", String.valueOf(curahHujanDb));
                        double curahHujanDbNorm = normalisasi3(curahHujanDb);
                        Log.d("curahHujanDb Norm", String.valueOf(curahHujanDbNorm));

                        distanceSemua[i] = (hitungEuclideanDistance(suhuX, suhuDbNorm, kelembabanX, kelembabanDbNorm,
                                curahHujanX, curahHujanDbNorm));
                        kelas[i] = (document.getString("hama"));
                        Log.d("Euclidean Distance", String.valueOf(distanceSemua[i]));
                    }
                    printArray();
                    bubbleSort();
                    printArray();
                    prediksiHama = tentukanClass();
                    Log.d("Hasil prediksi", prediksiHama);
                    getDataPencegahan();
                }else {
                    Log.d("Get Training Document", "Gagal mengambil document training");
                }
            }
        });
    }

    private double normalisasi3(double x){ return (x-1)/2; }

    private double hitungEuclideanDistance(double x, double x1, double y, double y1, double z, double z1){
        return Math.sqrt((Math.pow(Double.valueOf(Math.abs(x-x1)), 2)) + (Math.pow(Double.valueOf(Math.abs(y-y1)), 2))
                + (Math.pow(Double.valueOf(Math.abs(z-z1)), 2)));
    }

    private void bubbleSort(){
        Log.d("Array", "======Bubble Sort======");
        for(int i = (distanceSemua.length-1); i>0; i--) {
            for(int j = 0; j<i; j++) {
                if(distanceSemua[j]>distanceSemua[j+1]) {
                    double tempDist = distanceSemua[j+1];
                    String tempClass = kelas[j+1];
                    distanceSemua[(j+1)] = distanceSemua[j];
                    kelas[(j+1)] = kelas[j];
                    distanceSemua[j] = tempDist;
                    kelas[j] = tempClass;
                }
            }
        }
    }

    private void printArray(){
        for (int i=0; i<distanceSemua.length; i++){
            Log.d("Array", String.valueOf(distanceSemua[i]) + " " + kelas[i]);
        }
    }

    private String tentukanClass(){
        String tempKelas = kelas[0];
        for (int i=0; i<3; i++){
            for (int j=i+1; j<3; j++){
                if (kelas[i].equalsIgnoreCase(kelas[j])){
                    tempKelas = kelas[i];
                    break;
                }
            }
        }

        return tempKelas;
    }

    private void getDataPencegahan(){
        db.collection("pencegahan").document(prediksiHama).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("DocumentSnapshotData", "data" + documentSnapshot.getData());
                Map<String, Object> document = documentSnapshot.getData();
                for (Map.Entry<String, Object> entry : document.entrySet()) {
                    pencegahan.put(entry.getKey(), (String) entry.getValue());
                }
                setView();
            }
        });
    }

    private void setView(){
        caraPencegahan.setVisibility(View.VISIBLE);
        adapter adapter = new adapter(this, pencegahan);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        switch (prediksiHama){
            case "tikus":
                thumbnail.setImageResource(getResources().getIdentifier("@drawable/thumbnail_tikus", null, getPackageName()));
                break;
            case "wereng":
                thumbnail.setImageResource(getResources().getIdentifier("@drawable/thumbnail_wereng", null, getPackageName()));
                break;
            case "keong mas":
                thumbnail.setImageResource(getResources().getIdentifier("@drawable/thumbnail_keong_mas", null, getPackageName()));
                break;
            case "penggerek batang":
                thumbnail.setImageResource(getResources().getIdentifier("@drawable/thumbnail_penggerek_batang", null, getPackageName()));
                break;
             default:
                 thumbnail.setImageResource(getResources().getIdentifier("@drawable/pic_lundi", null, getPackageName()));
        }
        thumbnail.startAnimation(fadeIn);
        title.setText(prediksiHama);
        subtitle.setText("Hama " + prediksiHama + " diperkirakan akan menyerang sawah anda dalam dua minggu ke-depan");
        lokasi.setText("Lokasi sawah : " + lokasiSawah);
        textLoading.setVisibility(View.GONE);
        btnBack.setEnabled(true);
    }

    public void btnBackOnClick(View view){
        finish();
    }
}
