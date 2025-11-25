package br.restaurante.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "pedido_id")
    @JsonIgnore
    private Pedido pedido;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_restaurante_id")
    @JsonIgnoreProperties({"avaliacoes", "restaurante"})
    private ItemRestaurante itemRestaurante;

    @Column(nullable = false)
    private Integer quantidade;

    @Column(length = 500)
    private String observacoes;

    // Campos livres para requisitos do front
    @Column(length = 1000)
    private String ingredientesRemovidos;

    @Column(length = 1000)
    private String ingredientesAdicionados;

    public Long getId() { return id; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public ItemRestaurante getItemRestaurante() { return itemRestaurante; }
    public void setItemRestaurante(ItemRestaurante itemRestaurante) { this.itemRestaurante = itemRestaurante; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }
    public String getIngredientesRemovidos() { return ingredientesRemovidos; }
    public void setIngredientesRemovidos(String ingredientesRemovidos) { this.ingredientesRemovidos = ingredientesRemovidos; }
    public String getIngredientesAdicionados() { return ingredientesAdicionados; }
    public void setIngredientesAdicionados(String ingredientesAdicionados) { this.ingredientesAdicionados = ingredientesAdicionados; }
}

