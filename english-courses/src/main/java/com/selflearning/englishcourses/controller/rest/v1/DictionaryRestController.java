package com.selflearning.englishcourses.controller.rest.v1;

import com.selflearning.englishcourses.service.dto.DictionaryDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class DictionaryRestController {


    @GetMapping(value = "/dictionary", params = "all")
    public ResponseEntity<DictionaryDto> result(@RequestParam("search") String keyword){
        return null;
    }

}
