package com.codearti.controller;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codearti.model.dto.ProductCreateRequestDto;
import com.codearti.model.dto.ProductResponseDto;
import com.codearti.model.dto.ProductUpdateRequestDto;
import com.codearti.model.dto.ProductUpdateStockRequestDto;
import com.codearti.model.entity.ProductStatus;
import com.codearti.service.ProductService;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "v1.0")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService service;
	
	private final ServletWebServerApplicationContext webServerAppCtxt;
	
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<List<ProductResponseDto>> findAll(@RequestParam(required = false) ProductStatus status){
		var port = webServerAppCtxt.getWebServer().getPort();
		var result = service.findAll(status, port);
		if (result.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(result);
	}
	
	@GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<ProductResponseDto> findById(@PathVariable Long id) throws InterruptedException{
		var port = webServerAppCtxt.getWebServer().getPort();		
		if (id.equals(7L)) {
			TimeUnit.SECONDS.sleep(5L);
		}		
		var result = service.findById(id, port);
		return ResponseEntity.ok(result);
	}
	
	@PostMapping(produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<ProductResponseDto> create(@Valid @RequestBody ProductCreateRequestDto productCreateRequestDto){
		var port = webServerAppCtxt.getWebServer().getPort();
		var result = service.create(productCreateRequestDto, port);
		return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
	}
	
	@PutMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<ProductResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateRequestDto productUpdateRequestDto){
		var port = webServerAppCtxt.getWebServer().getPort();
		var result = service.update(id, productUpdateRequestDto, port);
		return ResponseEntity.ok(result);
	}
	
	@PatchMapping(value = "/{id}/stock", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<ProductResponseDto> updateStock(@PathVariable Long id, @Valid @RequestBody ProductUpdateStockRequestDto productUpdateStockRequestDto){
		var port = webServerAppCtxt.getWebServer().getPort();
		var result = service.updateStok(id, productUpdateStockRequestDto, port);
		return ResponseEntity.ok(result);
	}	
	
	@DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE },
			consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_PROBLEM_JSON_VALUE })
	public ResponseEntity<ProductResponseDto> delete(@PathVariable Long id){
		var port = webServerAppCtxt.getWebServer().getPort();
		service.delete(id, port);
		return ResponseEntity.noContent().build();
	}
}
