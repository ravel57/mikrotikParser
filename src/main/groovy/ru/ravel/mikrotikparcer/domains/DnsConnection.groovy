package ru.ravel.mikrotikparcer.domains

import jakarta.persistence.Column
import jakarta.persistence.Id
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor


@NoArgsConstructor
@AllArgsConstructor
class DnsConnection {

	@Column(name = "dst_ip")
	@Id
	String dstIP

	@Column(name = "dst_dns", length = 1024)
	String dstDNS

	boolean isIgnoreVpn

	DnsConnection(String dstIP, String dstDNS) {
		this.dstIP = dstIP
		this.dstDNS = dstDNS
	}

	DnsConnection() {
	}

	boolean equals(o) {
		if (this.is(o)) return true
		if (o == null || getClass() != o.class) return false

		DnsConnection that = (DnsConnection) o

		if (dstDNS != that.dstDNS) return false
		if (dstIP != that.dstIP) return false

		return true
	}

	int hashCode() {
		int result
		result = (dstIP != null ? dstIP.hashCode() : 0)
		result = 31 * result + (dstDNS != null ? dstDNS.hashCode() : 0)
		return result
	}
}
