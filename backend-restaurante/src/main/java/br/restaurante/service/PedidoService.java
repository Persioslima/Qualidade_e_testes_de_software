package br.restaurante.service;

import br.restaurante.dto.ItemPedidoRequest;
import br.restaurante.dto.PedidoRequest;
import br.restaurante.model.*;
import br.restaurante.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;
import java.util.List;

@Service
public class PedidoService {

    @Autowired private PedidoRepository pedidoRepository;
    @Autowired private ClienteRepository clienteRepository;
    @Autowired private RestauranteRepository restauranteRepository;
    @Autowired private ItemRestauranteRepository itemRestauranteRepository;

    public Pedido criarPedidoParaCliente(String emailCliente, PedidoRequest request) {
        if (request.getRestauranteId() == null) {
            throw new InputMismatchException("É necessário informar o restaurante do pedido.");
        }
        if (request.getItens() == null || request.getItens().isEmpty()) {
            throw new InputMismatchException("O pedido deve conter pelo menos um item.");
        }

        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new InputMismatchException("Cliente não encontrado."));

        Restaurante restaurante = restauranteRepository.findById(request.getRestauranteId())
                .orElseThrow(() -> new InputMismatchException("Restaurante não encontrado."));

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setRestaurante(restaurante);
        pedido.setObservacoesGerais(request.getObservacoesGerais());

        for (ItemPedidoRequest ireq : request.getItens()) {
            if (ireq.getItemRestauranteId() == null || ireq.getQuantidade() == null || ireq.getQuantidade() <= 0) {
                throw new InputMismatchException("Cada item deve conter 'itemRestauranteId' e 'quantidade' > 0.");
            }
            ItemRestaurante item = itemRestauranteRepository.findById(ireq.getItemRestauranteId())
                    .orElseThrow(() -> new InputMismatchException("Item do cardápio não encontrado: " + ireq.getItemRestauranteId()));

            if (!item.getRestaurante().getId().equals(restaurante.getId())) {
                throw new InputMismatchException("Item " + ireq.getItemRestauranteId() + " não pertence ao restaurante informado.");
            }

            ItemPedido ip = new ItemPedido();
            ip.setPedido(pedido);
            ip.setItemRestaurante(item);
            ip.setQuantidade(ireq.getQuantidade());
            ip.setObservacoes(ireq.getObservacoes());
            ip.setIngredientesRemovidos(ireq.getIngredientesRemovidos());
            ip.setIngredientesAdicionados(ireq.getIngredientesAdicionados());
            pedido.getItens().add(ip);
        }

        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidosDoCliente(String emailCliente) {
        Cliente cliente = clienteRepository.findByEmail(emailCliente)
                .orElseThrow(() -> new InputMismatchException("Cliente não encontrado."));
        return pedidoRepository.findByClienteOrderByCriadoEmDesc(cliente);
    }

    public Pedido atualizarStatus(Long idPedido, String status, String emailCliente) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new InputMismatchException("Pedido não encontrado."));
        if (!pedido.getCliente().getEmail().equalsIgnoreCase(emailCliente)) {
            throw new InputMismatchException("Pedido não pertence ao cliente logado.");
        }
        pedido.setStatus(status);
        return pedidoRepository.save(pedido);
    }
}

