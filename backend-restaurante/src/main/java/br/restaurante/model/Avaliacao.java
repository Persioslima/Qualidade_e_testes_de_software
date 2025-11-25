package br.restaurante.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDateTime;

/**
 * Representa uma avaliação feita por um cliente a um restaurante.
 */
@Entity
@Table(name = "avaliacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A nota da avaliação é obrigatória.")
    @DecimalMin(value = "0.0", message = "A nota mínima é 0.")
    @DecimalMax(value = "5.0", message = "A nota máxima é 5.")
    @Column(nullable = false)
    private Double nota; // Avaliação numérica (0 a 5)

    @Size(max = 500, message = "O comentário deve ter no máximo 500 caracteres.")
    @Column(length = 500)
    private String comentario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "restaurante_id", nullable = false)
    @JsonIgnoreProperties({"avaliacoes", "senha", "itens"})
    private Restaurante restaurante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @JsonIgnoreProperties({"senha", "pedidos"})
    private Cliente cliente;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataAvaliacao;

    @PrePersist
    public void prePersist() {
        this.dataAvaliacao = LocalDateTime.now();
    }
}
