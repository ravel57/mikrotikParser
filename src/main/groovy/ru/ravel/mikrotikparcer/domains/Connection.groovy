package ru.ravel.mikrotikparcer.domains

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import lombok.AccessLevel
import lombok.NoArgsConstructor

import java.time.ZonedDateTime

@Entity
@NoArgsConstructor(access = AccessLevel.PRIVATE)
class Connection {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "id")
	Long id

	@Column(name = "src_ip")
	String srcIP

	@Column(name = "src_port")
	Integer srcPort

	@Column(name = "src_dns", length = 1024)
	String srcDNS

	@Column(name = "dst_ip")
	String dstIP

	@Column(name = "dst_port")
	Integer dstPort

	@Column(name = "dst_dns", length = 1024)
	String dstDNS

	@Column(name = "host_name", length = 1024)
	String hostName

	@Column(name="time")
	ZonedDateTime time

	@Column(name = "orig_rate")
	long origRate

	@Column(name = "repl_rate")
	long replRate


	Connection() {
	}

	Connection(def it, def dns, Map<String, List<Map<String, String>>> hostNames) {
		String srcIP = it["src-address"]
		String dstIP = it["dst-address"]
		def srcSplit = srcIP.split(":")
		this.srcIP = srcSplit[0]
		if (srcSplit.length > 1) {
			this.srcPort = (srcSplit[1] as String).toInteger()
		}
		def dstSplit = dstIP.split(":")
		this.dstIP = dstSplit[0]
		if (dstSplit.length > 1) {
			this.dstPort = (dstSplit[1] as String).toInteger()
		}

		if (dns[this.srcIP]) {
			this.srcDNS = (dns[this.srcIP]["name"] as List).join(', ')
		}
		if (dns[this.dstIP]) {
			this.dstDNS = (dns[this.dstIP]["name"] as List).join(', ')
		}
		if (hostNames[this.srcIP]) {
			this.hostName = hostNames[this.srcIP]?["host-name"]?[0]
		}
		this.time = ZonedDateTime.now()
		this.origRate= it["orig-rate"].toString().toInteger().intdiv(1024)
		this.replRate= it["repl-rate"].toString().toInteger().intdiv(1024)
	}
}