package com.example.terveyssovellus;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;

/**
 * Luokka on käyttäjän asetuksien näyttö
 *
 * @author Henri Vuento, Tuomo Muttonen
 * @version 12.12.2021
 */
public class UserSettings extends AppCompatActivity {

    private double weight = 0;                                                                          //muuttuja painolle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * Hakee kaloritavoitteen muistista ja asettaa sen textViewiin
         * Hakee painon muistista ja asettaa sen textViewiin
         * Hakee pituuden muistista ja asettaa sen textViewiin
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editWeight = findViewById(R.id.et_weight);                                             //haetaan ui elementit myöhempää käyttöä varten
        EditText editFood = findViewById(R.id.et_food_goal);

        SharedPreferences prefs3 = getDefaultSharedPreferences(getApplicationContext());                //määritetään paikka muistissa jossa haetaan tiedot
        Gson gson = new Gson();

        if (prefs3.getString("paino", null) != null) {                                      //ehto jolla tarkastetaan että kyseinen data on tallennettu muistiin
            String paino = prefs3.getString("paino", "0");                                  //asetetaan muistista haettu data muuttujaan käsittelyä varten
            String s1 = paino.substring(paino.indexOf(":") + 1);                                          //pilkotaan muistista haetusta Stringistä pelkkä paino numerona
            paino = s1.replace(" kg", "");
            s1 = paino.replace("\"]", "");
            paino = s1.split("\"")[0];
            weight = Double.parseDouble(paino);

            editWeight.setText(String.valueOf(weight));                                                 //asetetaan paino näkyville ui:hin
        }
        /**
         * tarkkailee millon käyttäjä on syöttänyt kenttään päivittäisen kaloritavoitteen ja reagoi entterin painallukseen suorittamalla changeUserFood funktion
         */
        editFood.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserFood();                                                           //funktio kutsu jossa tallennetaan datan ja asetetaan se näkyville
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        /**
         * tarkkailee millon käyttäjä on syöttänyt kenttään pituuden ja reagoi entterin painallukseen suorittamalla changeUserHeight funktion
         */

        EditText editHeight = findViewById(R.id.et_height);                                             //määritetään ui elementti muuttujaan
        editHeight.setOnKeyListener(new View.OnKeyListener()                                            //tarkkaillaan millon käyttäjä on syöttänyt uuden pituuden ja reakoidaan entterin painallukseen
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserHeight();                                                         //funktio kutsu jossa tallennetaan datan ja asetetaan se näkyville
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });
        /**
         * tarkkailee millon käyttäjä on syöttänyt kenttään painon ja reagoi entterin painallukseen suorittamalla changeUserWeight funktion
         */

        editWeight.setOnKeyListener(new View.OnKeyListener()                                            //tarkkaillaan millon käyttäjä on syöttänyt uuden painolukeman ja reagoidaan entterin painallukseen
        {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            changeUserWeight();                                                         //tallennetaan paino muistiin ja asetetaan näkyville
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());                 //määritetään paikka muistissa josta haetaan kaloritavoite
        int calories_goal = prefs.getInt("user_food_goal", 0);
        TextView textView = findViewById(R.id.et_food_goal);                                            //haetaan elemennti ui:sta id:llä ja lisätään muuttujaan
        textView.setText(String.valueOf(calories_goal));                                                //asetetaan elemmenttiin uusi arvo

        SharedPreferences prefs2 = getDefaultSharedPreferences(getApplicationContext());                //määritetään paikka muistista josta haetaan pituus
        float user_height = prefs2.getFloat("user_height", 0);
        TextView textView2 = findViewById(R.id.et_height);                                              //haetaan elemennti ui:sta id:llä ja lisätään muuttujaan
        textView2.setText(String.valueOf(user_height));                                                 //asetetaan pituus näkyville ui:hin
    }

    public void changeUserFood() {                                                                       //funktio jolla tallennetaan muistiin kaloritavoite
        /**
         * Tallennetaan käyttäjän syöttämä päivittäinen kaloritavoite muistiin
         */
        //ilmoitetetaan käyttäjälle että kalori tavoite on asetettu
        EditText editText = findViewById(R.id.et_food_goal);                                            //haetaan ui elementti id:llä ja asetetaan muuttujaan
        String temp = editText.getText().toString();                                                    //haetaan ui elementti idellä ja tallennetaan muuttujaan
        if (temp.contains(",") || (temp.contains("."))) {                                               //tarkastetaan käyttäjän syöte
            editText.getText().clear();
            Toast.makeText(getApplicationContext(), "Syötä kokonaisluku", Toast.LENGTH_SHORT).show();
        } else {
            int n = Integer.parseInt(editText.getText().toString());                                        //tallennetaan käyttäjän syöte muistiin
            SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());                 //asetetaan muistipaikka johon halutaan tallentaa tieto
            SharedPreferences.Editor editor = prefs.edit();

            editor.putInt("user_food_goal", n);                                                             //tallennetaan muistiin kaloritavoite
            editor.commit();
            Toast.makeText(getApplicationContext(), "Päivittäinen kaloritavoite asetettu.", Toast.LENGTH_SHORT).show();  //Ilmoitetaan käyttäjälle onnistunut tapahtuma
        }
    }

    public void changeUserHeight() {                                                                      //funktio pituuden tallentamista muistiin varten
        /**
         * Tallennetaan käyttäjän syöttämä pituus muistiin
         */
        Toast.makeText(getApplicationContext(), "Pituus asetettu.", Toast.LENGTH_SHORT).show();      //ilmoitetaan käyttäjälle että pituus on asetettu
        EditText editText = findViewById(R.id.et_height);
        String temp = editText.getText().toString();                                                                                           //haetaan ui elementti idellä ja tallennetaan muuttujaan
        if (temp.contains(",")) {                                                                          //tarkastetaan käyttäjän syöte
            temp = temp.replace(",", ".");                                                    //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
        }
        float n = Float.parseFloat(temp);                                                              //tallennetaan käyttäjän syöte muistiin

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());                 //Valitaan muistipaikka johon tallennetaan
        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat("user_height", n);                                                              //tallennetaan pituus muistiin
        editor.commit();
    }

    public void changeUserWeight() {                                                                     //funktio jolla tallennetaan käyttäjän paino muistiin
        /**
         *
         * Tallennetaan käyttäjän syöttämä paino muistiin
         */
        ArrayList<String> weight;                                                                       //luodaan arraylist weight johon tallennetaan paino
        weight = new ArrayList<String>();
        EditText editWeight = findViewById(R.id.et_weight);                                             //haetaan ui elementti id:llä

        Toast.makeText(getApplicationContext(), "Paino asetettu.", Toast.LENGTH_SHORT).show();      //ilmoitetaan käyttäjälle että paino on tallenettu
        Collections.reverse(weight);                                                                    //käännetään arraylist jotta uusin syöte olisi ensimmäisenä

        Calendar calendar = Calendar.getInstance();                                                     //Haetaan päivä+aika
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());                   //Asetetaan currentDateKey muotoon pp.kk.vvvv, käytetään Keynä sharedPreferensseissä
        String s = currentDate + " Paino: " + editWeight.getText().toString() + " kg";                  //luodaan stringi joka tallennetaan muistiin joka sisältää päivän ja painon arvon

        if (s.contains(",")) {                                                                          //tarkastetaan käyttäjän syöte
            s = s.replace(",", ".");                                                    //vaihdetaan pilkku pisteeseen jolloin estetään ongelmat parsen suhteen
        }

        weight.add(s);                                                                                  //tallennetaan String arraylistiin
        Collections.reverse(weight);                                                                    //käännetään arraylist jotta uusin tieto tulisi ensimmäiseksi

        SharedPreferences prefs = getDefaultSharedPreferences(getApplicationContext());                 //valitaan muistipaikka johon tallennetaan
        SharedPreferences.Editor editor = prefs.edit();
        /** käytetään gson kirjastoa jotta saadaan muutettua java objekti json muotoon*/
        Gson gson = new Gson();
        String json = gson.toJson(weight);
        /** tallennetaan tieto muistiin json muodossa*/
        editor.putString("paino", json);
        editor.apply();


    }
}