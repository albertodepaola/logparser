-- Filters a certain datetime range in the subquery and a number of ocurrences in the outer query. 
select a.* from (
	select count(ipv4) as requests, ipv4, logFile 
	from log_entry le
	where le.date between "2017-01-01.13:00:00" and "2017-01-01.14:00:00"
	group by ipv4, logFile
	order by requests desc) a
where a.requests > 100;

--Filters by ip
select * from log_entry 
where ipv4 = '192.168.228.188';