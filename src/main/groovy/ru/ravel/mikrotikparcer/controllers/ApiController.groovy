package ru.ravel.mikrotikparcer.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.ravel.mikrotikparcer.services.CollectService
import ru.ravel.mikrotikparcer.services.ConnectionsService


@CrossOrigin
@RestController
@RequestMapping('/api/v1/')
class ApiController {

	ConnectionsService connectionsService

	CollectService collectService


	ApiController(ConnectionsService connectionsService, CollectService collectService) {
		this.connectionsService = connectionsService
		this.collectService = collectService
	}


	@GetMapping("/src")
	ResponseEntity<Object> getSrc(@RequestParam String srcIp) {
		return ResponseEntity.status(HttpStatus.OK).body(connectionsService.getBySrc(srcIp))
	}


	@GetMapping("/dns")
	ResponseEntity<Object> getByDns(@RequestParam("find") String name) {
		return ResponseEntity.status(HttpStatus.OK).body(collectService.getByDns(name))
	}


	@PostMapping("/dns")
	ResponseEntity<Object> postDnsToIgnoreList(@RequestParam String dns, @RequestParam boolean enabled) {
		return ResponseEntity.status(HttpStatus.OK).body(connectionsService.postDnsToIgnoreList(dns, enabled))
	}

}
