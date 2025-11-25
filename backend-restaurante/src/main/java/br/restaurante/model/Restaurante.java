package br.restaurante.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Entidade Restaurante — representa um restaurante cadastrado no sistema.
 */
@Entity
@Table(name = "restaurantes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(onlyExplicitlyIncluded = true) // Lombok gera apenas campos incluídos manualmente
public class Restaurante {

    // 🔹 Identificador único
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ToString.Include
    private Long id;

    // 🔹 Informações principais
    @Column(nullable = false, length = 100)
    @ToString.Include
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    @ToString.Include
    private String cnpj;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, unique = true, length = 100)
    @ToString.Include
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // impede envio em JSON de saída
    @ToString.Exclude // evita inclusão automática
    private String senha;

    // 🔹 Endereço
    private String rua;
    private Integer numero;
    private String bairro;
    @ToString.Include
    private String cidade;
    @ToString.Include
    private String estado;
    private String cep;

    // 🔹 Informações adicionais
    private String descricao;
    private String horario;
    private Integer lotacao;
    private String site;
    private String facebook;
    private String instagram;
    private String whatsapp;

    // 🔹 URLs de imagens / documentos
    private String cardapioUrl;
    private String logoUrl;
    private String bannerUrl;

    // 🔹 Flags de consentimento
    @ToString.Include
    private boolean aceitaComunicacao;
    @ToString.Include
    private boolean aceitaMarketing;
    @ToString.Include
    private boolean aceitaProtecaoDados;

    // 🔹 Relacionamentos
    @OneToMany(mappedBy = "restaurante", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Avaliacao> avaliacoes;

    // 🔹 Calcula média das avaliações
    public Double getAvaliacaoMedia() {
        if (avaliacoes == null || avaliacoes.isEmpty()) {
            return 0.0;
        }
        double soma = 0.0;
        for (Avaliacao avaliacao : avaliacoes) {
            soma += avaliacao.getNota();
        }
        return soma / avaliacoes.size();
    }

    // 🔹 toString seguro — garante que senha nunca aparece
    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cnpj='" + cnpj + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", aceitaProtecaoDados=" + aceitaProtecaoDados +
                '}';
    }
}
