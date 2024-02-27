package ru.ravel.mikrotikparcer.reposetory


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import ru.ravel.mikrotikparcer.domains.Connection

interface ConnectionRepository extends JpaRepository<Connection, Long> {

	@Query(value = "SELECT DISTINCT * FROM connection WHERE dst_dns like %?1%", nativeQuery = true)
	List<Connection> findByDstDNS(String dstDns)

}