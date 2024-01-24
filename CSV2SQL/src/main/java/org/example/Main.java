package org.example;

import org.example.database.MySQLConnection;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String input;
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please, enter the path of the csv-file. >");
        input = scanner.nextLine();

        System.out.println(input);

        try {
            while(true){
                Path pfad = Paths.get(input);

                while (input == ""){
                    System.out.println("Das Eingabefeld ist leer. Bitte geben Sie einen Pfad ein");
                    input = scanner.nextLine();
                }

                // Überprüfen der Existenz
                if (Files.exists(pfad) && input.toLowerCase().endsWith(".csv")) {
                    System.out.println("Der Pfad existiert.");

                    break;
                }
                System.out.println("Der Pfad ist inkorrekt oder ist keine CSV-Datei. " +
                        "Bitte geben Sie einen Pfad ein.");
                input = scanner.nextLine();
            }

        }catch (Exception e){
            System.out.println(e);
        }


        //TODO: wenn Datei leer ist....


        //Daten aus Datei auslesen und als Text ausgeben
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(input))) {
            String line;

//            if (br.readLine() == null) {
//                System.out.println("Die Datei ist leer.");
//            } else {

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); //teilt wörter und setzt Komma dazwischen, in Datei immer drei zusammengehörige
                //Hochkomma herausnehmen
                records.add(Arrays.asList(values));
                //System.out.println(Arrays.asList(values));

            }
            //}
        }catch (Exception e){
            System.out.println(e);
        }

        System.out.println(records.get(0).get(0));
        //Daten via insert in SQL transformieren

        // System.out.println(records.size());
        MySQLConnection connection = new MySQLConnection(null); //da noch keine -> null



        String insertSql = "insert into cities (country, city, function )" + "values(%s, %s, %s)";
        for (List<String> row : records) { //records übergeben, solange etwas vorhanden ist
            String readySql = String.format(insertSql, row.get(0), row.get(1), row.get(2));
            System.out.println(readySql);
            connection.addCity(row.get(0), row.get(1), row.get(2));
        }

        //DB-Verbindung aufbauen

/*
        MySQLConnection connection = new MySQLConnection(null); //da noch keine -> null

        connection.getConnection(); //da Variable deklariert nicht nochmal MYSQLC... schreiben, wird versuchen Verbindung aufzubauen
*/

        //Daten in DB speichern
    }
}