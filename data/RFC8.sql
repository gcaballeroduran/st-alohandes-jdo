with tiempo as(
select count( cli.reservas )as num, (res.fecha_fin - res.fecha_inicio ) as noches
FROM  a_Cliente cli
INNER JOIN  a_Reserva res ON(res.id = cli.reservas)
group by (res.fecha_fin - res.fecha_inicio ) 

)
select cl.*
FROM a_cliente cl, tiempo t
where t.num >= 3 OR noches >= 15


        

