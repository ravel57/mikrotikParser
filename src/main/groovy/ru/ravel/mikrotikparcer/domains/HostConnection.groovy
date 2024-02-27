package ru.ravel.mikrotikparcer.domains

class HostConnection {

	List<Connection> connections

	String hostName

	String srcIP


	HostConnection(List<Connection> connections) {
		this.connections = connections
		this.hostName = connections?[0]?.hostName
		this.srcIP = connections?[0]?.srcIP
	}
}