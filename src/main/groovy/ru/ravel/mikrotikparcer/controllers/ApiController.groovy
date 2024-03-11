package ru.ravel.mikrotikparcer.controllers

import jakarta.servlet.http.HttpSession
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


	@GetMapping("")
	ResponseEntity<Object> getAll(HttpSession httpSession) {
		return ResponseEntity.status(HttpStatus.OK).body(connectionsService.getConnectionsByHost())
	}


	@GetMapping("/src/{srcIp}")
	ResponseEntity<Object> getSrc(
			HttpSession httpSession,
			@PathVariable("srcIp") String srcIp,
			@RequestParam(name = "dns_only", required = false, defaultValue = "false") Boolean dnsOnly
	) {
		return ResponseEntity.status(HttpStatus.OK).body(connectionsService.getSrc(srcIp, dnsOnly))
	}


	@GetMapping("/dns")
	ResponseEntity<Object> getByDns(
			@RequestParam("find") String name
	) {
		return ResponseEntity.status(HttpStatus.OK).body(collectService.getByDns(name))
	}


	@PostMapping("/dns")
	ResponseEntity<Object> postDnsToIgnoreList(
			@RequestParam("add") String name,
			@RequestParam("enabled") boolean enabled
	) {
		return ResponseEntity.status(HttpStatus.OK).body(connectionsService.postDnsToIgnoreList(name, enabled))
	}

}
