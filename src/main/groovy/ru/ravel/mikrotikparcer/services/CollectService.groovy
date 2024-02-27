package ru.ravel.mikrotikparcer.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import ru.ravel.mikrotikparcer.domains.DnsConnection
import ru.ravel.mikrotikparcer.reposetory.ConnectionRepository

@Service
class CollectService {

	ConnectionsService connectionsService
	ConnectionRepository connectionRepository


	CollectService(ConnectionsService connectionsService, ConnectionRepository connectionRepository) {
		this.connectionsService = connectionsService
		this.connectionRepository = connectionRepository
	}


	@Scheduled(cron = "*/10 * * * * *")
	void collectToDateBase() {
		def connections = connectionsService.getConnections()
		connectionRepository.saveAll(connections as Iterable)
	}


	List<DnsConnection> getByDns(String name) {
		return connectionRepository.findByDstDNS(name)
				.collect { new DnsConnection(it.dstIP, it.dstDNS) }
				.unique { it.dstIP }
				.each { it.isIgnoreVpn = connectionsService.isIgnoreVpn(it.dstDNS) }
	}

}