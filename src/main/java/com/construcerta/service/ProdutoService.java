package com.construcerta.service;

import com.construcerta.dto.ProdutoForm;
import com.construcerta.model.Produto;
import com.construcerta.repository.ProdutoRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> listarAtivos() {
        return produtoRepository.findAllByAtivoTrueOrderByNomeAsc();
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public List<Produto> listarTodos() {
        return produtoRepository.findAllByOrderByNomeAsc();
    }

    @PreAuthorize("hasAnyRole('VENDEDOR', 'ADMIN')")
    public Produto salvar(ProdutoForm form) {
        Produto produto = (form.getId() != null && !form.getId().isBlank())
                ? produtoRepository.findById(form.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado."))
                : new Produto();

        produto.setNome(form.getNome().trim());
        produto.setCategoria(form.getCategoria().trim());
        produto.setUnidadeMedida(form.getUnidadeMedida().trim());
        produto.setPreco(form.getPreco());
        produto.setEstoque(form.getEstoque());
        produto.setAtivo(form.isAtivo());

        return produtoRepository.save(produto);
    }
}
