package com.construcerta.repository;

import com.construcerta.model.Produto;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ProdutoRepository extends MongoRepository<Produto, String> {
    List<Produto> findAllByAtivoTrueOrderByNomeAsc();

    List<Produto> findAllByOrderByNomeAsc();
}
