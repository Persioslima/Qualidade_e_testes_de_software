package br.restaurante.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 20)
    private String telefone;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Column(length = 9)
    private String cep;

    // Campos de endereço
    @Column(length = 100)
    private String rua;

    @Column(length = 50)
    private String bairro;

    @Column(length = 50)
    private String cidade;

    @Column(length = 50)
    private String estado;

    @Column(length = 10)
    private String numero;

    // Campos booleanos
    private boolean aceitaProtecaoDados;
    private boolean aceitaMarketing;
    private boolean aceitaAtendimento;

    // Construtor, getters e setters (gerados manualmente ou pelo Lombok)

    public Cliente() {
    }

    public Cliente(Long id, String nome, String telefone, String cpf, String email, String senha,String cep,
                   String rua, String bairro, String cidade, String estado, String numero, boolean aceitaProtecaoDados,
                   boolean aceitaMarketing, boolean aceitaAtendimento) {
        this.id = id;
        this.nome = nome;
        this.telefone = telefone;
        this.cpf = cpf;
        this.email = email;
        this.senha = senha;
        this.cep = cep;
        this.rua = rua;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.numero = numero;
        this.aceitaProtecaoDados = aceitaProtecaoDados;
        this.aceitaMarketing = aceitaMarketing;
        this.aceitaAtendimento = aceitaAtendimento;
    }

    // Getters e Setters gerados
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public boolean isAceitaProtecaoDados() {
        return aceitaProtecaoDados;
    }

    public void setAceitaProtecaoDados(boolean aceitaProtecaoDados) {
        this.aceitaProtecaoDados = aceitaProtecaoDados;
    }

    public boolean isAceitaMarketing() {
        return aceitaMarketing;
    }

    public void setAceitaMarketing(boolean aceitaMarketing) {
        this.aceitaMarketing = aceitaMarketing;
    }

    public boolean isAceitaAtendimento() {
        return aceitaAtendimento;
    }

    public void setAceitaAtendimento(boolean aceitaAtendimento) {
        this.aceitaAtendimento = aceitaAtendimento;
    }
}