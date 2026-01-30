package br.com.alura.screensound;

import br.com.alura.screensound.principal.Principal;
import br.com.alura.screensound.repository.ArtistaRepository;
import br.com.alura.screensound.repository.MusicaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação Screen Sound Músicas.
 * 
 * @SpringBootApplication - Anotação que marca esta classe como ponto de entrada do Spring Boot
 *   Combina 3 anotações:
 *   - @Configuration: Define que a classe contém configurações do Spring
 *   - @EnableAutoConfiguration: Habilita configuração automática do Spring Boot
 *   - @ComponentScan: Escaneia pacotes em busca de componentes (@Component, @Service, @Repository)
 * 
 * CommandLineRunner - Interface que permite executar código após a aplicação iniciar
 *   - Ideal para aplicações de console (sem interface web)
 *   - O método run() é executado automaticamente após o Spring inicializar
 */
@SpringBootApplication
public class ScreensoundApplication implements CommandLineRunner {

	/**
	 * Injeção de dependência do ArtistaRepository.
	 * 
	 * @Autowired - O Spring cria e injeta automaticamente uma instância do repository
	 *   - NÃO precisa fazer: new ArtistaRepository()
	 *   - O Spring gerencia o ciclo de vida do objeto
	 *   - Facilita testes (pode injetar mocks)
	 * 
	 * Como funciona?
	 * 1. Spring escaneia o pacote e encontra a interface ArtistaRepository
	 * 2. Spring cria automaticamente uma implementação da interface
	 * 3. Spring injeta a implementação neste atributo
	 */
	@Autowired
	private ArtistaRepository artistaRepository;

	/**
	 * Injeção de dependência do MusicaRepository.
	 * Mesmo processo do ArtistaRepository.
	 */
	@Autowired
	private MusicaRepository musicaRepository;

	/**
	 * Método main - Ponto de entrada da aplicação Java.
	 * 
	 * SpringApplication.run():
	 * 1. Inicializa o contexto do Spring
	 * 2. Configura o banco de dados (usando application.properties)
	 * 3. Cria os repositories
	 * 4. Executa o método run() do CommandLineRunner
	 * 
	 * @param args - argumentos da linha de comando
	 */
	public static void main(String[] args) {
		SpringApplication.run(ScreensoundApplication.class, args);
	}

	/**
	 * Método executado automaticamente após o Spring inicializar.
	 * 
	 * Fluxo de execução:
	 * 1. Spring inicializa (lê application.properties, conecta no banco, etc.)
	 * 2. Spring injeta os repositories (artistaRepository e musicaRepository)
	 * 3. Este método run() é executado
	 * 4. Cria uma instância de Principal passando os repositories
	 * 5. Chama exibeMenu() que mostra o menu interativo para o usuário
	 * 
	 * Por que passar os repositories para Principal?
	 * - A classe Principal precisa acessar o banco de dados
	 * - Passamos os repositories já configurados pelo Spring
	 * - Evita criar conexões duplicadas com o banco
	 * 
	 * @param args - argumentos da linha de comando (não usado nesta aplicação)
	 * @throws Exception - qualquer exceção que possa ocorrer
	 */
	@Override
	public void run(String... args) throws Exception {
		// Cria instância de Principal com os repositories injetados
		Principal principal = new Principal(artistaRepository, musicaRepository);
		
		// Exibe o menu interativo para o usuário
		principal.exibeMenu();
	}
}
