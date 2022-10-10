package com.diantongren.controller;

import com.diantongren.service.DictionaryCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dict")
@RequiredArgsConstructor
@Slf4j
public class DictionaryController {
    final DictionaryCacheService dictionaryCacheService;

    @PostMapping("/remove")
    public void removeDictCacheByCode(@RequestParam("dictCode") String dictCode) {
//        dictionaryCacheService.removeDictCache(dictCode);
        dictionaryCacheService.removeDictCacheInRedis(dictCode);
    }
}
