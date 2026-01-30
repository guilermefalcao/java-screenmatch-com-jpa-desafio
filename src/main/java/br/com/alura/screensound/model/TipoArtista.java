package br.com.alura.screensound.model;

/**
 * Enum que representa os tipos de artistas musicais.
 * 
 * Um enum é uma classe especial que representa um conjunto fixo de constantes.
 * Neste caso, temos 3 tipos possíveis de artistas:
 * - SOLO: Artista individual (ex: Madonna, Michael Jackson)
 * - DUPLA: Dois artistas (ex: Chitãozinho & Xororó)
 * - BANDA: Grupo musical (ex: Beatles, Coldplay)
 * 
 * Por que usar Enum?
 * - Garante que apenas valores válidos sejam usados
 * - Evita erros de digitação ("solo" vs "Solo" vs "SOLO")
 * - Facilita manutenção do código
 */
public enum TipoArtista {
    // Constantes do enum com seus valores em minúsculo
    SOLO("solo"),
    DUPLA("dupla"),
    BANDA("banda");

    // Atributo que guarda o valor em texto do tipo
    private String tipoArtista;

    /**
     * Construtor do enum.
     * Define o valor em texto para cada constante.
     * 
     * @param tipoArtista - valor em texto ("solo", "dupla", "banda")
     */
    TipoArtista(String tipoArtista) {
        this.tipoArtista = tipoArtista;
    }

    /**
     * Método que converte uma String em um valor do enum TipoArtista.
     * 
     * Exemplo de uso:
     * String entrada = "solo";
     * TipoArtista tipo = TipoArtista.fromString(entrada); // Retorna SOLO
     * 
     * Por que esse método é útil?
     * - Permite converter a entrada do usuário (texto) em um valor do enum
     * - Ignora maiúsculas/minúsculas ("SOLO", "solo", "Solo" funcionam)
     * - Lança exceção se o texto não corresponder a nenhum tipo válido
     * 
     * @param text - texto digitado pelo usuário
     * @return TipoArtista correspondente
     * @throws IllegalArgumentException se o texto não for válido
     */
    public static TipoArtista fromString(String text) {
        // Percorre todos os valores do enum (SOLO, DUPLA, BANDA)
        for (TipoArtista tipo : TipoArtista.values()) {
            // Compara ignorando maiúsculas/minúsculas
            if (tipo.tipoArtista.equalsIgnoreCase(text)) {
                return tipo;
            }
        }
        // Se não encontrou nenhum tipo válido, lança exceção
        throw new IllegalArgumentException("Nenhum tipo encontrado para a string fornecida: " + text);
    }
}
