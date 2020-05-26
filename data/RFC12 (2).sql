 with semana as (
 SELECT to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')) week_num , MIN(p.dias_reservados) AS personas, p.id as id
 FROM A_RESERVA r
 INNER JOIN A_PROPIEDAD p ON (p.id = r.propiedad) 
 group by to_number(to_char(to_date(r.fecha_inicio,'MM/DD/YYYY'),'WW')),p.id 
 )
 
 SELECT p.*
 FROM A_PROPIEDAD p
 INNER JOIN semana s ON (s.id = p.id)
  