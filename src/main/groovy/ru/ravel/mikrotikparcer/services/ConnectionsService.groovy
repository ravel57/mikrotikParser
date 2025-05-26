package ru.ravel.mikrotikparcer.services

import me.legrange.mikrotik.ApiConnection
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.ravel.mikrotikparcer.dto.GroupedDnsConnection
import ru.ravel.mikrotikparcer.model.Connection
import ru.ravel.mikrotikparcer.model.DnsConnection
import ru.ravel.mikrotikparcer.model.HostConnection
import ru.ravel.mikrotikparcer.reposetory.ConnectionRepository


@Service
class ConnectionsService {

	private ConnectionRepository connectionRepository

	private ApiConnection connect
	private Logger log = LoggerFactory.getLogger(this.class)

	private String ignoreVpnListName = "ignoreVpn"
	@Value('${app.mikrotik.gateway}')
	private String gateway
	@Value('${app.mikrotik.user}')
	private String user
	@Value('${app.mikrotik.password}')
	private String password

	ConnectionsService(ConnectionRepository connectionRepository) {
		connect = ApiConnection.connect(gateway)
		connect.login(user, password)
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


	List<GroupedDnsConnection> getBySrc(String srcIp) {
		def connections = getConnectionsByHost().findAll {
			it.srcIP == srcIp
		}.collect {
			it.connections
		}.flatten() as List<Connection>
		List<GroupedDnsConnection> collect = connections.groupBy {
			it.dstDNS
		}.collect {
			def dst = it.value.collect {
				new DnsConnection(it.dstIP, it.dstDNS)
			}
			new GroupedDnsConnection(it.key, dst, isIgnoreVpn(it.key))
		}
		return collect
	}


	void postDnsToIgnoreList(String domains, boolean enabled) {
		def execute = connect.execute("/ip/firewall/address-list/print  where list=\"$ignoreVpnListName\"")
		domains.split(', ').each { domain ->
			Map<String, String> result = execute.find { e -> e.address == domain }
			def id = result?[".id"]
			try {
				if (enabled) {
					if (isIgnoreVpn(domain) && id) {
						connect.execute("/ip/firewall/address-list/set disabled=no .id=$id")
					} else if (result != null) {
						connect.execute("/ip/firewall/address-list/set disabled=no .id=$id")
					} else {
						connect.execute("/ip/firewall/address-list/add list=$ignoreVpnListName address=$domain")
					}
				} else {
					connect.execute("/ip/firewall/address-list/set disabled=yes .id=$id")
				}
			} catch (Exception e) {
				log.error(e.localizedMessage, e)
			}
		}
	}


	boolean isIgnoreVpn(String dns) {
		List<Map<String, String>> addresses = connect.execute("/ip/firewall/address-list/print  where list=\"$ignoreVpnListName\"")
		boolean every = dns?.split(", ")?.every { address ->
			addresses.findAll {
				it.dynamic == 'false'
			}.findAll {
				it.disabled == 'false'
			}.any {
				it.address == address
			}
		} ?: false
		return every
	}

}
