package br.com.persio;

/**
 * Representa um usuário que participa de um leilão.
 * Cada usuário tem um nome (identificação).
 */
public class Usuario {

    private String nome;

    public Usuario(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do usuário não pode ser vazio ou nulo.");
        }
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return "Usuário: " + nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Usuario outro = (Usuario) obj;
        return nome.equalsIgnoreCase(outro.nome);
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }
}
