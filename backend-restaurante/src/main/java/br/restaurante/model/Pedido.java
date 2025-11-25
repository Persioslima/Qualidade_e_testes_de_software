package br.restaurante.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Pedido
 * Representa um pedido realizado por um cliente em um restaurante.
 */
@Entity
@Table(name = "pedidos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Cliente que realizou o pedido */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"pedidos", "senha"})
    private Cliente cliente;

    /** Restaurante responsável pelo pedido */
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurante_id")
    @JsonIgnoreProperties({"pedidos", "itens"})
    private Restaurante restaurante;

    /** Itens do pedido (com relacionamento bidirecional) */
    @Builder.Default
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<ItemPedido> itens = new ArrayList<>();


    /** Observações gerais adicionadas pelo cliente */
    @Column(length = 255)
    private String observacoesGerais;

    /** Data e hora em que o pedido foi criado */
    @Column(nullable = false)
    private LocalDateTime criadoEm;

    /** Status atual do pedido */
    @Column(length = 20, nullable = false)
    private String status; // NOVO, EM_PREPARO, CONCLUIDO, CANCELADO

    /** Define valores padrão antes de persistir o registro */
    @PrePersist
    public void prePersist() {
        this.criadoEm = LocalDateTime.now();
        if (this.status == null) {
            this.status = "NOVO";
        }
    }

    // 🔹 Métodos utilitários para manipular a lista de itens com segurança
    public void adicionarItem(ItemPedido item) {
        item.setPedido(this);
        this.itens.add(item);
    }

    public void removerItem(ItemPedido item) {
        this.itens.remove(item);
        item.setPedido(null);
    }

    // 🔹 equals e hashCode — importantes para entidades gerenciadas pelo JPA
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido pedido)) return false;
        return id != null && id.equals(pedido.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // 🔹 toString resumido — evita recursão em relações bidirecionais
    @Override
    public String toString() {
        return "Pedido{id=" + id +
                ", cliente=" + (cliente != null ? cliente.getId() : null) +
                ", restaurante=" + (restaurante != null ? restaurante.getId() : null) +
                ", status='" + status + '\'' +
                ", criadoEm=" + criadoEm +
                '}';
    }
}
