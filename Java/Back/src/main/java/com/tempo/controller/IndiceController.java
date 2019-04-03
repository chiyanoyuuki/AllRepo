package com.tempo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.tempo.bean.Indice;
import com.tempo.manager.IndiceManager;

@CrossOrigin
@RestController
public class IndiceController 
{
	@Autowired
	private IndiceManager indiceManager;
	
	@GetMapping(value = "/Indices", produces = "application/json")
    public ResponseEntity<List<Indice>> getIndices() 
	{
		System.out.println("CONTROLLER OK");
		List<Indice> indices = indiceManager.getIndices();
        return new ResponseEntity<List<Indice>>(indices,HttpStatus.OK);
    }
}
