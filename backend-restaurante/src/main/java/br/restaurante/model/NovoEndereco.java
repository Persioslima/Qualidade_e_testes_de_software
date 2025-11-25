package br.restaurante.model;

public class NovoEndereco {
    private Long id;

    private String cep;
    private String rua;
    private String bairro;
    private String cidade;
    private String estado;


    public NovoEndereco(){}

    public NovoEndereco(Endereco endereco){
        this.cep = endereco.cep();
        this.rua = endereco.rua();
        this.bairro = endereco.bairro();
        this.cidade = endereco.cidade();
        this.estado = endereco.estado();
    }

    @Override
    public String toString() {
        return "NovoEndereco{" +
                "id=" + id +
                ", cep='" + cep + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

