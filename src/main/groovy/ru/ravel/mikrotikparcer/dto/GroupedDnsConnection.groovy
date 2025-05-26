package ru.ravel.mikrotikparcer.dto

import ru.ravel.mikrotikparcer.model.DnsConnection


class GroupedDnsConnection {

	String dstDns

	List<DnsConnection> dnsConnections

	boolean isIgnoreVpn


	GroupedDnsConnection(String dstDns, List<DnsConnection> dnsConnections, boolean isIgnoreVpn) {
		this.dstDns = dstDns
		this.dnsConnections = dnsConnections
		this.isIgnoreVpn = isIgnoreVpn
	}
}
