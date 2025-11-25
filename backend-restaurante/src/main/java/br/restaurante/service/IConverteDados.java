package br.restaurante.service;

public interface IConverteDados {
    public <T> T converteDados(String json, Class<T> tClass);
}