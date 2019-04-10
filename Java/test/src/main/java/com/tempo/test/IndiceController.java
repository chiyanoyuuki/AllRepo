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
    public ResponseEntity<List<IndiceVal>> getIndicesVals(@RequestParam int ID, @RequestParam String TIME) 
	{
		List<IndiceVal> indices = indiceDao.getIndicesVals(ID, TIME);
        return new ResponseEntity<List<IndiceVal>>(indices,HttpStatus.OK);
    }
	
	@GetMapping(value = "/IndicesNewVals", produces = "application/json")
    public ResponseEntity<List<IndiceVal>> getIndicesNewVals(@RequestParam int ID, @RequestParam String DATE) 
	{
		List<IndiceVal> indices = indiceDao.getIndicesNewVals(ID,DATE);
        return new ResponseEntity<List<IndiceVal>>(indices,HttpStatus.OK);
    }
	
	@GetMapping(value = "/IndicesTotal", produces = "application/json")
    public ResponseEntity<List<String>> getIndicesTotal(@RequestParam int ID) 
	{
		List<String> indices = indiceDao.getIndicesTotal(ID);
        return new ResponseEntity<List<String>>(indices,HttpStatus.OK);
    }
}
