package com.example.catalogservice.controller;

import com.example.catalogservice.entity.Catalog;
import com.example.catalogservice.service.CatalogService;
import com.example.catalogservice.vo.CatalogResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/catalog-service")
public class CatalogController {

    private final CatalogService catalogService;
    private final Environment environment;
    private final ModelMapper modelMapper;

    @GetMapping("/health-check")
    public String check() {
        return String.format("It's working in User-service on Port %s", environment.getProperty("local.server.port"));
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<CatalogResponse>> getCatalogs() {
        Iterable<Catalog> catalogs = catalogService.getAllCatalogs();
        List<CatalogResponse> result = new ArrayList<>();
        catalogs.forEach(catalog -> result.add(modelMapper.map(catalog, CatalogResponse.class)));

        return ResponseEntity.status(HttpStatus.OK)
                             .body(result);
    }

}
