package br.com.alura.screensound.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ConsultaAPI {
    public static String obterInformacao(String nomeArtista) {
        try {
            String artistaFormatado = nomeArtista.replace(" ", "+");
            String endereco = "https://www.theaudiodb.com/api/v1/json/2/search.php?s=" + artistaFormatado;

            HttpClient client = HttpClient.newBuilder()
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();
                    
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .header("User-Agent", "Mozilla/5.0")
                    .timeout(Duration.ofSeconds(10))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            String json = response.body();
            
            if (json.contains("null") || json.contains("<html>")) {
                return "Artista não encontrado ou API indisponível no momento.";
            }
            
            return formatarResposta(json);
            
        } catch (Exception e) {
            return "Não foi possível buscar informações. A funcionalidade de pesquisa externa está opcional.";
        }
    }
    
    private static String formatarResposta(String json) {
        try {
            if (json.contains("strArtist")) {
                String info = "\n=== INFORMAÇÕES DO ARTISTA ===\n";
                
                if (json.contains("strArtist")) {
                    String nome = extrairCampo(json, "strArtist");
                    info += "Nome: " + nome + "\n";
                }
                
                if (json.contains("strGenre")) {
                    String genero = extrairCampo(json, "strGenre");
                    info += "Gênero: " + genero + "\n";
                }
                
                if (json.contains("strCountry")) {
                    String pais = extrairCampo(json, "strCountry");
                    info += "País: " + pais + "\n";
                }
                
                if (json.contains("intFormedYear")) {
                    String ano = extrairCampo(json, "intFormedYear");
                    info += "Ano de formação: " + ano + "\n";
                }
                
                if (json.contains("strBiographyEN") && !json.contains("\"strBiographyEN\":null")) {
                    String bio = extrairCampo(json, "strBiographyEN");
                    if (bio.length() > 300) {
                        bio = bio.substring(0, 300) + "...";
                    }
                    info += "\nBiografia: " + bio + "\n";
                }
                
                return info;
            }
            return "Artista não encontrado.";
        } catch (Exception e) {
            return "Erro ao processar resposta da API.";
        }
    }
    
    private static String extrairCampo(String json, String campo) {
        try {
            String busca = "\"" + campo + "\":\"";
            int inicio = json.indexOf(busca);
            if (inicio == -1) return "N/A";
            inicio += busca.length();
            int fim = json.indexOf("\"", inicio);
            if (fim == -1) return "N/A";
            return json.substring(inicio, fim);
        } catch (Exception e) {
            return "N/A";
        }
    }
}
