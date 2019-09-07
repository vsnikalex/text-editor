package com.epam.texteditor.controller;

import com.epam.texteditor.model.Node;
import com.epam.texteditor.service.NodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    private final NodeService nodeService;

    public MainController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        // Mocked Nodes
        try {
            nodeService.saveNode(new Node("Node1", "first node text describing the main problem that is necessary to solve"));
            Thread.sleep(10);
            nodeService.saveNode(new Node("Node2", "second node text describing the main problem that is necessary to solve"));
            Thread.sleep(10);
            nodeService.saveNode(new Node("Node3", "third node text describing the main problem that is necessary to solve"));
            Thread.sleep(10);
            nodeService.saveNode(new Node("Node4", "fourth node text describing the main problem that is necessary to solve"));
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }

        String pageTitle = "EPAM Text Editor";
        String appName = "Text Editor Home Page";

        model.addAttribute("appName", appName);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("nodes", nodeService.getAllNodes());

        return "index";
    }

}
