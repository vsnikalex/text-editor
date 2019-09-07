package com.epam.texteditor.service;

import com.epam.texteditor.model.Node;
import com.epam.texteditor.repository.NodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeServiceImpl implements NodeService {

    private final NodeRepository nodeRepository;

    public NodeServiceImpl(NodeRepository nodeRepository) {
        this.nodeRepository = nodeRepository;
    }

    @Override
    public List<Node> getAllNodes() {
        return nodeRepository.findAllByOrderByModifiedDesc();
    }

    @Override
        public void saveNode(Node node) {
        nodeRepository.save(node);
    }

}
