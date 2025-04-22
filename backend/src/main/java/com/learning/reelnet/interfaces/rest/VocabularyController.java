package com.learning.reelnet.interfaces.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/vocabulary")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Vocabulary", description = "API endpoints for managing vocabulary")
public class VocabularyController {
    
}
