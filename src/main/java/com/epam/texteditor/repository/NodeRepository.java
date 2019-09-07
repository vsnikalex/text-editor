package com.epam.texteditor.repository;

import com.epam.texteditor.model.Node;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeRepository extends CrudRepository<Node, Long> {

    List<Node> findAllByOrderByModifiedDesc();

}
