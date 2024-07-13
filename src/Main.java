import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Boolean leave = false;
        Scanner scanner = new Scanner(System.in);
        String answer;
        String wellcomeMessage = """
        Bienvenidos a la app conversor de monedas
        *****************************************
        Una aplicación desarrollada para convertir
        de una divisa a otra.
        """;
        String actions = """
        1. Convertir una moneda
        2. Salir
                """;
        String options = """
        1. Peso colombiano (COP).
        2. Dolar estadounidence (USD).
        3. Peso mexicano (MXN).
        4. Real Brasileño (BRL).
        5. Peso Chileno (CLP).
        6. Peso argentino (ARS).
        7. Euro (EUR).
                """;

        Map<String,String> equivalencesReferences = new HashMap<>();
        equivalencesReferences.put("1","COP");
        equivalencesReferences.put("2","USD");
        equivalencesReferences.put("3","MXN");
        equivalencesReferences.put("4","BRL");
        equivalencesReferences.put("5","CLP");
        equivalencesReferences.put("6","ARS");
        equivalencesReferences.put("7","EUR");

        System.out.println(wellcomeMessage);
        System.out.println("por favor elija lo que quiere hacer");
        System.out.println(actions);

        answer = scanner.next();

        if (answer.equals("2")) {
            leave = true;
        }
        System.out.println(answer);

        while(!leave){
            System.out.println("Escoja su divisa de origen");
            System.out.println(options);
            String base = scanner.next();
            System.out.println("Escoja la cantidad a cambiar");
            Double amount = scanner.nextDouble();

            System.out.println("Escoja su divisa de a convertir");
            System.out.println(options);
            String quote = scanner.next();

            base = equivalencesReferences.get(base);
            quote = equivalencesReferences.get(quote);

            Double rate = fetch(base,quote);
            System.out.println(String.format("%.2f (%s) equivale a %.2f (%s)",amount,base,rate*amount,quote));
            //System.out.println(rate*amount);


            System.out.println("por favor elija lo que quiere hacer");
            System.out.println(actions);

            answer = scanner.next();

            if (answer.equals("2")) {
                leave = true;
            }

        }
    }

    public static double fetch(String base,String quote) throws IOException, InterruptedException {
        URI url = URI.create("https://v6.exchangerate-api.com/v6/0aced102f938bd00268e6238/latest/"+base);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String json = response.body();

        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject rates = jsonObject.get("conversion_rates").getAsJsonObject();
        return rates.get(quote).getAsDouble();

    }


}