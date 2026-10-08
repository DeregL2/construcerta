package com.construcerta.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class ProdutoForm {
    private String id;

    @NotBlank(message = "Informe o nome do produto.")
    private String nome;

    @NotBlank(message = "Informe a categoria.")
    private String categoria;

    @NotBlank(message = "Informe a unidade de medida (ex.: saco, m2, unidade).")
    private String unidadeMedida;

    @NotNull(message = "Informe o preco.")
    @DecimalMin(value = "0.0", inclusive = true, message = "O preco nao pode ser negativo.")
    private BigDecimal preco;

    @Min(value = 0, message = "O estoque nao pode ser negativo.")
    private int estoque;

    private boolean ativo = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
}
