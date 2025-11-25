package br.restaurante.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para FileStorageService.
 * Foca no comportamento de upload, sanitização e geração de nome único.
 */
class FileStorageServiceTest {

    private final FileStorageService fileStorageService = new FileStorageService();
    private final Path uploadsRoot = Path.of("uploads");

    @AfterEach
    void limparUploads() throws IOException {
        // Remove diretórios de teste criados durante os testes
        if (Files.exists(uploadsRoot)) {
            Files.walk(uploadsRoot)
                    .sorted((a, b) -> b.compareTo(a)) // remove primeiro os arquivos
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException e) {
                            // Ignorar erros de deleção em limpeza
                        }
                    });
        }
    }

    // ✅ TESTE: upload bem-sucedido
    @Test
    void deveSalvarArquivoComSucesso() throws IOException {
        MockMultipartFile arquivo = new MockMultipartFile(
                "file", "imagem.jpg", "image/jpeg", "conteudo fake".getBytes()
        );

        String caminho = fileStorageService.saveFile(arquivo, "fotos-produtos");

        assertNotNull(caminho);
        assertTrue(caminho.contains("/uploads/fotos-produtos/"));
        assertTrue(caminho.endsWith(".jpg"));

        Path destino = Path.of("uploads/fotos-produtos")
                .toAbsolutePath()
                .resolve(Path.of(caminho).getFileName());
        assertTrue(Files.exists(destino));
    }

    // ❌ TESTE: deve lançar erro se arquivo estiver vazio
    @Test
    void deveLancarErroAoSalvarArquivoVazio() {
        MockMultipartFile arquivoVazio = new MockMultipartFile(
                "file", "vazio.txt", "text/plain", new byte[0]
        );

        IOException ex = assertThrows(IOException.class, () ->
                fileStorageService.saveFile(arquivoVazio, "textos"));

        assertEquals("Arquivo vazio", ex.getMessage());
    }

    // ⚙️ TESTE: sanitização de tipo de pasta
    @Test
    void deveSanitizarTipoDeArquivoComCaracteresEspeciais() throws IOException {
        MockMultipartFile arquivo = new MockMultipartFile(
                "file", "foto.png", "image/png", "dados".getBytes()
        );

        String caminho = fileStorageService.saveFile(arquivo, "fôto#cliente!");
        assertTrue(caminho.contains("/uploads/f-to-cliente-"));
    }

    // 🧩 TESTE: deve gerar nomes únicos com extensão correta
    @Test
    void deveGerarNomeUnicoComExtensaoCorreta() throws IOException {
        MockMultipartFile arquivo1 = new MockMultipartFile(
                "file", "imagem1.jpg", "image/jpeg", "data1".getBytes()
        );
        MockMultipartFile arquivo2 = new MockMultipartFile(
                "file", "imagem2.jpg", "image/jpeg", "data2".getBytes()
        );

        String caminho1 = fileStorageService.saveFile(arquivo1, "galeria");
        String caminho2 = fileStorageService.saveFile(arquivo2, "galeria");

        assertNotEquals(caminho1, caminho2); // garante nomes únicos
        assertTrue(caminho1.endsWith(".jpg"));
        assertTrue(caminho2.endsWith(".jpg"));
    }
}
