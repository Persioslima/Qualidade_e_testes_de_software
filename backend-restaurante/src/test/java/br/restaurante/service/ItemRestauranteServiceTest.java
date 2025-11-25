package br.restaurante.service;

import br.restaurante.model.ItemRestaurante;
import br.restaurante.model.Restaurante;
import br.restaurante.repository.ItemRestauranteRepository;
import br.restaurante.repository.RestauranteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.InputMismatchException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ItemRestauranteServiceTest {

    private ItemRestauranteRepository itemRepo;
    private RestauranteRepository restRepo;
    private ItemRestauranteService service;

    @BeforeEach
    void setUp() {
        itemRepo = mock(ItemRestauranteRepository.class);
        restRepo = mock(RestauranteRepository.class);
        service = new ItemRestauranteService();
        // injetar via reflexão simples (sem Spring)
        inject(service, "itemRestauranteRepository", itemRepo);
        inject(service, "restauranteRepository", restRepo);
    }

    @Test
    @DisplayName("Deve lançar exceção quando nome vazio ou preço negativo")
    void deveValidarNomePreco() {
        ItemRestaurante item = new ItemRestaurante();
        item.setNome("   "); // vazio
        item.setPreco(-10.0); // negativo
        item.setRestaurante(new Restaurante());
        item.getRestaurante().setId(1L);

        assertThatThrownBy(() -> service.cadastrarItem(item))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("nome e o preço");
        verifyNoInteractions(itemRepo);
    }

    @Test
    @DisplayName("Deve lançar quando restaurante não informado")
    void deveValidarRestauranteNuloOuSemId() {
        ItemRestaurante item = new ItemRestaurante();
        item.setNome("Pizza");
        item.setPreco(50.0);
        // restaurante nulo
        assertThatThrownBy(() -> service.cadastrarItem(item))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("associar o item a um restaurante");

        // restaurante sem id
        item.setRestaurante(new Restaurante());
        assertThatThrownBy(() -> service.cadastrarItem(item))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("associar o item a um restaurante");
        verifyNoInteractions(itemRepo);
    }

    @Test
    @DisplayName("Deve lançar quando restaurante não encontrado no repositório")
    void deveValidarRestauranteInexistente() {
        ItemRestaurante item = new ItemRestaurante();
        item.setNome("Pizza");
        item.setPreco(50.0);
        item.setRestaurante(new Restaurante());
        item.getRestaurante().setId(99L);

        when(restRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.cadastrarItem(item))
                .isInstanceOf(InputMismatchException.class)
                .hasMessageContaining("Restaurante não encontrado");
        verifyNoInteractions(itemRepo);
    }

    @Test
    @DisplayName("Deve salvar item quando dados são válidos")
    void deveSalvarComSucesso() {
        ItemRestaurante item = new ItemRestaurante();
        item.setNome("Pizza");
        item.setPreco(50.0);
        Restaurante r = new Restaurante();
        r.setId(7L);
        item.setRestaurante(r);

        when(restRepo.findById(7L)).thenReturn(Optional.of(r));
        when(itemRepo.save(any(ItemRestaurante.class))).thenAnswer(inv -> inv.getArgument(0));

        ItemRestaurante salvo = service.cadastrarItem(item);

        assertThat(salvo.getRestaurante()).isSameAs(r);
        ArgumentCaptor<ItemRestaurante> captor = ArgumentCaptor.forClass(ItemRestaurante.class);
        verify(itemRepo).save(captor.capture());
        assertThat(captor.getValue().getNome()).isEqualTo("Pizza");
    }

    // helper para injetar mocks em campos @Autowired
    private static void inject(Object target, String field, Object value) {
        try {
            var f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
