package com.tempo.test;

import java.util.List;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
public class IndiceController 
{
	@Autowired
	private IndiceDaoImpl indiceDao;
	
	@GetMapping(value = "/Indices", produces = "application/json")
    public ResponseEntity<List<Indice>> getIndices() 
	{
		List<Indice> indices = indiceDao.getIndices();
        return new ResponseEntity<List<Indice>>(indices,HttpStatus.OK);
    }
	
	@GetMapping(value = "/IndicesVals", produces = "application/json")
    public ResponseEntity<List<IndiceVal>> getIndicesVals(@RequestParam int ID) 
	{
		List<IndiceVal> indices = indiceDao.getIndicesVals(ID);
        return new ResponseEntity<List<IndiceVal>>(indices,HttpStatus.OK);
    }
}
