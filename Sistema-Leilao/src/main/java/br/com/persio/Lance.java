package br.com.persio;

/**
 * Representa um lance dado em um leilão.
 * Contém o usuário que fez o lance e o valor ofertado.
 */
public class Lance {

    private Usuario usuario;
    private double valor;

    public Lance(Usuario usuario, double valor) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo.");
        }
        if (valor <= 0) {
            throw new IllegalArgumentException("O valor do lance deve ser maior que zero.");
        }
        this.usuario = usuario;
        this.valor = valor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return usuario.getNome() + " ofertou R$" + valor;
    }
}
