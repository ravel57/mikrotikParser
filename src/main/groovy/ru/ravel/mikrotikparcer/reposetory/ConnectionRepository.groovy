package ru.ravel.mikrotikparcer.reposetory


import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import ru.ravel.mikrotikparcer.model.Connection

interface ConnectionRepository extends JpaRepository<Connection, Long> {

	@Query(value = "SELECT DISTINCT * FROM connection WHERE dst_dns like %?1%", nativeQuery = true)
	List<Connection> findByDstDNS(String dstDns)

}