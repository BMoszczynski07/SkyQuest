package com.example.loty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText departureEditText;
    private EditText arrivalEditText;
    private EditText departureDateEditText;
    private EditText returnDateEditText;

    public boolean twoWay = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicjalizacja elementów widoku
        departureEditText = findViewById(R.id.editTextDeparture);
        arrivalEditText = findViewById(R.id.editTextArrival);
        departureDateEditText = findViewById(R.id.editTextDepartureDate);
        returnDateEditText = findViewById(R.id.editTextReturnDate);
    }

    public void onCheckBoxChecked(View view) {
        CheckBox checkBoxRoundTrip = (CheckBox) view;

        twoWay = !twoWay;

        Log.d("twoWay", twoWay ? "true" : "false");

        // Zmiana widoczności pola "Data powrotu" w zależności od stanu checkboxa
        if (checkBoxRoundTrip.isChecked()) {
            returnDateEditText.setVisibility(View.VISIBLE);
        } else {
            returnDateEditText.setVisibility(View.GONE);
        }
    }

    public ArrayList<JSONObject> sortJSONArray(JSONArray tab) {
        ArrayList<JSONObject> list = new ArrayList<>();

        // Konwersja tablicy JSON na listę ArrayList
        for (int i = 0; i < tab.length(); i++) {
            try {
                list.add(tab.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        // Sortowanie listy
        Collections.sort(list, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject o1, JSONObject o2) {
                try {
                    // Porównanie obiektów JSON na podstawie wybranego klucza lub atrybutu
                    // Załóżmy, że sortujemy względem klucza "some_key"
                    String val1 = o1.getString("departureDateTime");
                    String val2 = o2.getString("departureDateTime");

                    // Przykładowe sortowanie rosnące wg. wartości String
                    return val1.compareTo(val2);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });

        return list;
    }

    public void addFlightsToLayout(JSONObject flightObject, boolean makePadding) {
        LinearLayout layout = findViewById(R.id.results);

        try {
                View flightBox = getLayoutInflater().inflate(R.layout.flight_box, null);

                // Ustaw informacje o locie w nowym boxie
                TextView textModel = flightBox.findViewById(R.id.textModel);
                textModel.setText(flightObject.optString("model"));

                // Ustaw informacje o locie w nowym boxie
                TextView textModel1 = flightBox.findViewById(R.id.textDepartureAirport);
                textModel1.setText(flightObject.optString("departureAirportName"));

                // Ustaw informacje o locie w nowym boxie
                TextView textModel2 = flightBox.findViewById(R.id.textArrivalAirport);
                textModel2.setText(flightObject.optString("arriveAirportName"));

                // Ustaw informacje o locie w nowym boxie
                TextView textModel3 = flightBox.findViewById(R.id.textDepartureDateTime);
                textModel3.setText(flightObject.optString("departureDateTime"));

                // Ustaw informacje o locie w nowym boxie
                TextView textModel4 = flightBox.findViewById(R.id.textArrivalDateTime);
                textModel4.setText(flightObject.optString("arriveDateTime"));

                if (makePadding) {
                    View line = flightBox.findViewById(R.id.divider);
                    line.setVisibility(View.VISIBLE);
                }

                // Ustawienie innych informacji o locie (wyjście, przylot itp.)

                layout.addView(flightBox);

                if (makePadding) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) flightBox.getLayoutParams();
                    params.setMargins(0, 0, 0, 8);
                    flightBox.setLayoutParams(params);
                }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public void onSearchFlightsClicked(View view) {
        // Pobranie wartości wprowadzonych przez użytkownika
        String departure = departureEditText.getText().toString();
        String arrive = arrivalEditText.getText().toString();
        String departureDate = departureDateEditText.getText().toString();
        String returnDate = returnDateEditText.getText().toString();

        setContentView(R.layout.flights);

        System.out.println(departure + "\n" + arrive + "\n" + departureDate + "\n" + returnDate);

        // Tutaj możesz wykonać dalsze operacje z pobranymi wartościami formularza
        // np. wysłanie żądania do serwera w celu wyszukania lotów z danymi parametrami

        // Tworzenie kolejki żądań Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);


// Adres URL Twojego serwera
        String url = "http://192.168.1.42:8000/api/get-flights";

        JSONObject flightJSON = new JSONObject();
        try {
            flightJSON.put("departureDateTime1", departureDate);
            if (returnDate != "") flightJSON.put("departureDateTime2", returnDate);
            flightJSON.put("departureAirport", departure);
            flightJSON.put("arriveAirport", arrive);
            flightJSON.put("twoWay", twoWay);
            // Dodaj inne dane, jeśli są wymagane
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Tworzenie żądania GET
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url, flightJSON,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Obsługa odpowiedzi z serwera (response)
                        try {
                            JSONArray flightsArray = response.getJSONArray("res");

                            // Pobranie dwuwymiarowej tablicy JSON z lotami
                            JSONArray flightsArrayOneWay = flightsArray.getJSONArray(0); // Pierwsza tablica
                            JSONArray flightsArraySecondWay = flightsArray.getJSONArray(1); // Druga tablica

                            // Sortowanie obu tablic JSON
                            ArrayList<JSONObject> flightsArrayOneWaySorted = sortJSONArray(flightsArrayOneWay);
                            ArrayList<JSONObject> flightsArraySecondWaySorted = sortJSONArray(flightsArraySecondWay);

                            // Przytnij obie tablice do długości minLength
                            JSONArray trimmedFlightsArrayOneWay = new JSONArray();
                            JSONArray trimmedFlightsArraySecondWay = new JSONArray();

                            if (!twoWay) {
                                // Jeśli twoWay jest false, dodaj jedną z tablic do widoku
                                for (JSONObject flight : flightsArrayOneWaySorted) {
                                    addFlightsToLayout(flight, false);
                                }
                                return;
                            } else {
                                // Jeśli twoWay jest true, sprawdź i dopasuj obie tablice
                                int minLength = Math.min(flightsArrayOneWaySorted.size(), flightsArraySecondWaySorted.size());

                                for (int i = 0; i < minLength; i++) {
                                    String firstDepartureDate = flightsArrayOneWaySorted.get(i).getString("departureDateTime");
                                    String secondDepartureDate = flightsArraySecondWaySorted.get(i).getString("departureDateTime");

                                    String firstArriveDate = flightsArrayOneWaySorted.get(i).getString("arriveDateTime");
                                    String secondArriveDate = flightsArraySecondWaySorted.get(i).getString("arriveDateTime");

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                    try {
                                        Date date1 = format.parse(firstDepartureDate);
                                        Date date2 = format.parse(secondDepartureDate);
                                        Date date3 = format.parse(firstArriveDate);
                                        Date date4 = format.parse(secondArriveDate);

                                        if (date2.compareTo(date1) > 0 && date4.compareTo(date3) > 0) {
                                            trimmedFlightsArrayOneWay.put(flightsArrayOneWaySorted.get(i));
                                            trimmedFlightsArraySecondWay.put(flightsArraySecondWaySorted.get(i));
                                        }
                                    } catch (ParseException e) {
                                        Log.d("Error", e.getMessage());
                                    }
                                }

                                // Tutaj możesz wykonać jakieś operacje na dopasowanych tablicach
                                // np. wyświetlenie ich w widoku lub inna logika aplikacji
                            }

                            for (int i = 0; i < trimmedFlightsArrayOneWay.length(); i++) {
                                addFlightsToLayout(trimmedFlightsArrayOneWay.getJSONObject(i), false);
                                addFlightsToLayout(trimmedFlightsArraySecondWay.getJSONObject(i), true);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Obsługa błędu w przypadku niepowodzenia żądania
                        Log.e("Error", "Błąd żądania: " + error.toString());
                    }
                });

        // Dodanie żądania do kolejki
        requestQueue.add(stringRequest);
    }
}