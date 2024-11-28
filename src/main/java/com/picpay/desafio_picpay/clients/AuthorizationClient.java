package com.picpay.desafio_picpay.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://util.devi.tools/api/v2/authorize", name = "authorization")
public interface AuthorizationClient {

    @GetMapping
    public ResponseEntity<String> getAuthorization ();
}
