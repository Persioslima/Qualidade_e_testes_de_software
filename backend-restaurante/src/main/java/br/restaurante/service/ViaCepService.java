package br.restaurante.service;

import br.restaurante.model.Endereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.InputMismatchException;

@Service
public class ViaCepService {

    @Autowired
    private ConsomeApi consomeApi;

    @Autowired
    private IConverteDados conversor;

    private final String VIA_CEP_URL = "https://viacep.com.br/ws/";

    public Endereco buscaEnderecoPorCep(String cep) {
        if (cep == null || cep.isBlank()) {
            throw new InputMismatchException("O CEP não pode ser vazio.");
        }

        String cepLimpo = cep.replaceAll("[^0-9]", "");
        if (cepLimpo.length() != 8) {
            throw new InputMismatchException("O CEP deve conter 8 dígitos.");
        }

        String url = VIA_CEP_URL + cepLimpo + "/json/";
        String json = consomeApi.retornaJson(url);

        Endereco endereco = conversor.converteDados(json, Endereco.class);

        // O ViaCEP retorna um JSON com a chave "erro" se o CEP não for encontrado
        if (endereco.cep() == null) {
            throw new InputMismatchException("CEP não encontrado.");
        }

        return endereco;
    }
}