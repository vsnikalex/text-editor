package com.epam.texteditor.service;

import com.epam.texteditor.model.Node;

import java.util.List;

public interface NodeService {

    List<Node> getAllNodes();

    void saveNode(Node node);

}
