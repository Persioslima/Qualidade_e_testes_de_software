package br.restaurante.dto;

public class ItemPedidoRequest {

    private Long itemRestauranteId;
    private Integer quantidade;
    private String observacoes;
    private String ingredientesRemovidos; // pode ser CSV/JSON
    private String ingredientesAdicionados; // pode ser CSV/JSON

    public Long getItemRestauranteId() {
        return itemRestauranteId;
    }

    public void setItemRestauranteId(Long itemRestauranteId) {
        this.itemRestauranteId = itemRestauranteId;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public String getIngredientesRemovidos() {
        return ingredientesRemovidos;
    }

    public void setIngredientesRemovidos(String ingredientesRemovidos) {
        this.ingredientesRemovidos = ingredientesRemovidos;
    }

    public String getIngredientesAdicionados() {
        return ingredientesAdicionados;
    }

    public void setIngredientesAdicionados(String ingredientesAdicionados) {
        this.ingredientesAdicionados = ingredientesAdicionados;
    }
}

