package ru.ravel.mikrotikparcer.services

import me.legrange.mikrotik.ApiConnection
import org.springframework.stereotype.Service
import ru.ravel.mikrotikparcer.domains.Connection
import ru.ravel.mikrotikparcer.domains.HostConnection
import ru.ravel.mikrotikparcer.reposetory.ConnectionRepository

@Service
class ConnectionsService {

	private ApiConnection connect
	private ConnectionRepository connectionRepository
	private String ignoreVpnListName = "ignoreVpn"


	ConnectionsService(ConnectionRepository connectionRepository) {
		connect = ApiConnection.connect(System.getenv("GATEWAY"))
		connect.login(System.getenv("MIKROTIK_USER"), System.getenv("MIKROTIK_PASSWORD"))
		this.connectionRepository = connectionRepository
	}


	void closeConnect() {
		connect.close()
	}


	List<Connection> getConnections() {
		def connections = new ArrayList<Connection>()
		try {
			def dns = connect
					.execute("/ip/dns/cache/print")
					.groupBy { it.data }
			def hostNames = connect
					.execute("/ip/dhcp-server/lease/print")
					.groupBy { it.address }
			connections = connect
					.execute("/ip/firewall/connection/print")
					.collect {
						new Connection(it, dns, hostNames)
					}
		} catch (ignore) {
		}
		return connections
	}


	List<HostConnection> getConnectionsByHost() {
		def connections = getConnections()
				.groupBy { [srcIP: it.srcIP, hostName: it.hostName] }
				.collect { new HostConnection(it.value) }
		return connections
	}


	List<Connection> getSrc(String srcIp, boolean dnsOnly) {
		def connections = getConnectionsByHost().findAll {
			it.srcIP == srcIp
		}.collect {
			it.connections
		}.flatten() as List<Connection>
		if (dnsOnly) {
			connections = connections.findAll { it.dstDNS != null }
		}
		return connections
	}


	void postDnsToIgnoreList(String name, boolean enabled) {
		def execute = connect.execute("/ip/firewall/address-list/print")
		name.split(', ').each {
			def id = execute.find { e -> e.address == it && e.list == ignoreVpnListName }?[".id"]
			try {
				if (enabled) {
					if (isIgnoreVpn(it) && id) {
						connect.execute("/ip/firewall/address-list/set disabled=no .id=$id")
					} else {
						connect.execute("/ip/firewall/address-list/add list=$ignoreVpnListName address=$it")
					}
				} else {
					connect.execute("/ip/firewall/address-list/set disabled=yes .id=$id")
				}
			} catch (ignore) {
				ignore.printStackTrace()
			}
		}
	}

	boolean isIgnoreVpn(String dns) {
		return connect.execute("/ip/firewall/address-list/print")
				.findAll { it.list == ignoreVpnListName }
				.findAll { it.dynamic == 'false' }
				.findAll { it.disabled == 'false' }
				.any { it.address.contains(dns) }
	}

}
