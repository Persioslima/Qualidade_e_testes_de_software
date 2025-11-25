package br.restaurante.model;

import jakarta.persistence.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;

@Entity
@Data
public class AvaliacaoPrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double nota;

    @Column(length = 500)
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "prato_id", nullable = false)
    @JsonIgnoreProperties({"avaliacoes", "restaurante"})
    private ItemRestaurante itemRestaurante;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = true)
    @JsonIgnoreProperties({"senha"})
    private Cliente cliente;

    private LocalDateTime dataAvaliacao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public ItemRestaurante getPrato() {
        return itemRestaurante;
    }

    public void setPrato(ItemRestaurante itemRestaurante) {
        this.itemRestaurante = itemRestaurante;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}