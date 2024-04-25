import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import java.io.IOException;
import java.util.Scanner;

public class ConversorMoeda {
    private static final String API_KEY = "40b79a1f8c63dd74194c5642";
    private static final String BASE_URL = "https://v6.exchangeratesapi.io/latest";
    private static OkHttpClient httpClient = new OkHttpClient();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bem-vindo ao Conversor de Moedas!");
        System.out.println("Selecione a partir das opções de conversão desejada:");

        String[] currencies = {"USD", "EUR", "GBP", "JPY", "CAD", "AUD"};
        for (int i = 0; i < currencies.length; i++) {
            System.out.println((i + 1) + ". " + currencies[i]);
        }

        System.out.print("Escolha a moeda de origem (1-" + currencies.length + "): ");
        int sourceCurrencyIndex = scanner.nextInt() - 1;
        System.out.print("Escolha a moeda de destino (1-" + currencies.length + "): ");
        int targetCurrencyIndex = scanner.nextInt() - 1;

        System.out.print("Digite o valor que deseja converter: ");
        double amount = scanner.nextDouble();

        try {
            double result = convertCurrency(currencies[sourceCurrencyIndex], currencies[targetCurrencyIndex], amount);
            System.out.println("O valor convertido é: " + result + " " + currencies[targetCurrencyIndex]);
        } catch (IOException e) {
            System.out.println("Erro ao obter as taxas de câmbio: " + e.getMessage());
        }
    }

    private static double convertCurrency(String sourceCurrency, String targetCurrency, double amount) throws IOException {
        String url = BASE_URL + "?access_key=" + API_KEY + "&base=" + sourceCurrency + "&symbols=" + targetCurrency;
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            JSONObject jsonResponse = new JSONObject(response.body().string());
            double exchangeRate = jsonResponse.getJSONObject("rates").getDouble(targetCurrency);

            return amount * exchangeRate;
        }
    }
}
